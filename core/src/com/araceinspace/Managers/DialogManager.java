package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.Screens.*;
import com.araceinspace.misc.CustomDialog;
import com.araceinspace.misc.FreetypeFontLoader;
import com.araceinspace.misc.RandomString;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Isaac Assega on 8/6/17.
 * Controls all Pop-up Dialogs that the player interacts with
 */
public class DialogManager {
    /* Static Variables */

    /* Field Variables */
    GameWorld parent;
    public boolean dialogQuestion = false;
    public CustomDialog purchaseDialog;
    public Dialog notEnoughCoinsDialog;
    public Dialog infoDialog;
    public CustomDialog nameDialog;
    public Dialog levelLoadingDialog;
    public CustomDialog levelIntroDialog;
    BitmapFont titleFont;
    static BitmapFont queryFont;

    //Keep track of items used by dialog manager
    public int coinsToSpend;
    public int levelPackToBuy;

    /* Constructor */
    public DialogManager(GameWorld p){
        parent = p;
        titleFont = FreetypeFontLoader.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 25);
        queryFont = FreetypeFontLoader.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 14);
    }

    /* Private Methods */

    /* Public Methods */

    public void setupPurchaseDialog(final Skin skin, final Stage stage, final Screen screen) {
        ArrayList<String> subtitles = new ArrayList<String>();
        subtitles.add("Spending Coins");
        purchaseDialog = new CustomDialog(subtitles, skin, screen.getViewport()) {
            protected void result(Object object) {
                if (object.toString().equals("true")) {
                    dialogQuestion = true;
                } else {
                    dialogQuestion = false;
                }
                if (dialogQuestion) {
                    if (parent.getCoins() >= coinsToSpend) {
                        if(screen instanceof SCOREScreen){
                            parent.levelManager.setLevel(parent.levelManager.getCurrentLevel());
                        }else if(screen instanceof LEADERBOARDScreen){
                            parent.levelManager.setLevel(LEADERBOARDScreen.levelClicked);//set in leaderboard screen
                        }else if(screen instanceof LEVELSELECTScreen){
                            buyLevelPack(levelPackToBuy);
                        }else if(screen instanceof PREGAMEScreen){
                            if (parent.renderManager.placeClicked == RenderManager.PLACES.first) {
                                parent.levelManager.setChallenge(LevelManager.CHALLENGES.first);
                            } else if (parent.renderManager.placeClicked == RenderManager.PLACES.second) {
                                parent.levelManager.setChallenge(LevelManager.CHALLENGES.second);
                            } else if (parent.renderManager.placeClicked == RenderManager.PLACES.third) {
                                parent.levelManager.setChallenge(LevelManager.CHALLENGES.third);
                            } else{
                                parent.levelManager.setChallenge(parent.levelManager.getCurrentChallenge());
                            }
                            parent.setCoins(parent.getCoins() - coinsToSpend);

                            parent.levelManager.playGame(skin, stage, stage.getViewport());
                        }


                    } else {
                        notEnoughCoinsDialog.show(stage);
                    }
                    dialogQuestion = false;//reset the boolean
                }
            }
        };

        ImageTextButton yes = new ImageTextButton("YES", skin);
        ImageTextButton no = new ImageTextButton("NO", skin);
        purchaseDialog.button(yes, "true");
        purchaseDialog.button(no, "false");
        purchaseDialog.getButtonTable().padBottom(300);

        ArrayList<String> newSubtitle = new ArrayList<String>();
        newSubtitle.add("Not Enough Coins!");
        notEnoughCoinsDialog = new CustomDialog(newSubtitle, skin, screen.getViewport());
        notEnoughCoinsDialog.getTitleTable().padBottom(100);
        notEnoughCoinsDialog.getButtonTable().padBottom(300);
        ImageTextButton oh = new ImageTextButton("Oh...", skin);
        notEnoughCoinsDialog.button(oh);
    }

    public void setupInfoDialog(Skin skin, Stage stage, Screen screen){
        ArrayList<String> newSubtitle = new ArrayList<String>();
        newSubtitle.add("");
        infoDialog = new CustomDialog(newSubtitle, skin, screen.getViewport());
        ImageTextButton okButton = new ImageTextButton("OK...", skin);
        infoDialog.button(okButton);

        infoDialog.getTitleTable().padBottom(100);
        infoDialog.getButtonTable().padBottom(300);
    }

    public void setupNameDialog(Skin skin, Stage stage, Viewport viewport){
        final TextArea textArea = new TextArea("", skin);
        textArea.setMaxLength(8);

        TextField.TextFieldStyle style = textArea.getStyle();
        style.font = titleFont;

        ImageTextButton submitButton = new ImageTextButton("SUBMIT", skin);
        final RandomString randomString = new RandomString(4);
        ArrayList<String>subtitles = new ArrayList<String>();
        subtitles.add("What Is");
        subtitles.add("Your Name?");
        nameDialog = new CustomDialog(subtitles, skin, viewport){
            protected void result(Object object) {
                String name = textArea.getText();
                if(name.isEmpty() || name.contains("MongoError")){ //don't allow these to be names, instead force guest name
                    name = "Guest "+randomString.nextString();
                }
                name = name.replaceAll("\\s", "-");
                parent.playerName = name;

                //If we are changing the from level select screen, we will make sure to immediately set
                //our new name on that screen
                if(parent.renderManager.getCurrentScreen() instanceof LEVELSELECTScreen ){
                    ((LEVELSELECTScreen)parent.renderManager.getCurrentScreen()).taunt2.setText(parent.playerName+"!!!");
                }

                System.out.println("Name is set to: " + parent.playerName);
                parent.prefs.putString("com.araceinspace.playerName", name);
                parent.prefs.flush();
                Gdx.input.setOnscreenKeyboardVisible(false);
            }
        };
        nameDialog.getTitleTable().padTop(100);
        nameDialog.getButtonTable().padBottom(300);
        nameDialog.getContentTable().add(textArea).expandX().width(viewport.getScreenWidth()*.50f).height(viewport.getScreenHeight()/14).center();
        nameDialog.button(submitButton);
    }

    /**
     * Used automatically when ghost does not match level.
     * @param skin
     * @param stage
     * @param viewport
     * @param ghostLevel
     * @param jsonOfGhost
     */
    public void setupLoadingLevelDialog(Skin skin, Stage stage, Viewport viewport, final int ghostLevel, final String jsonOfGhost){
        ArrayList<String>subtitles = new ArrayList<String>();
        subtitles.add("Loading Level: " + ghostLevel);
        levelLoadingDialog = new CustomDialog(subtitles, skin, viewport){
            protected void result(Object object) {
                parent.levelManager.setLevel(ghostLevel);
                parent.levelManager.setupGhostFromJson(jsonOfGhost);
                parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level
            }
        };
        levelLoadingDialog.getTitleTable().padTop(200);

        ImageTextButton okButton = new ImageTextButton("OK...", skin);
        levelLoadingDialog.button(okButton);
    }

    /**
     * Dialog used before level to show player the goal
     */
    public void setupLevelIntroDialog(int level, Skin skin, Stage stage, Viewport viewport){
        ArrayList<String>subtitle = parent.levelManager.getLevelsSubtitles(level);
        subtitle.add(0, "Level "+level);
        levelIntroDialog = new CustomDialog(subtitle, skin, viewport){
            public void result(Object object) {
               //we don't need to do anything here, we take care of this in the play buttons listener
            }
        };

        levelIntroDialog.getTitleTable().getCell(levelIntroDialog.getTitleLabel()).expandX().align(Align.center);
        levelIntroDialog.getTitleTable().setDebug(parent.devMode);
        levelIntroDialog.getContentTable().setDebug(parent.devMode);
        levelIntroDialog.getButtonTable().setDebug(parent.devMode);

        levelIntroDialog.align(Align.center);
        String buttonStyle = "level1";//parent.levelManager.getIntroStyleByLevel();
        System.out.println("buttonSytle:" + buttonStyle+":");
        ImageButton introButton = new ImageButton(skin, buttonStyle);
        if(Gdx.files.internal("levels/"+parent.levelManager.currentLevelPack+"/level"+parent.levelManager.getCurrentLevel()+"intro.png").exists()){
            Texture texture1 = new Texture(Gdx.files.internal("levels/"+parent.levelManager.currentLevelPack+"/level"+parent.levelManager.getCurrentLevel()+"intro.png"));
            TextureRegion tr = new TextureRegion(texture1);
            TextureRegionDrawable trd = new TextureRegionDrawable(tr);
            introButton.getStyle().imageUp = trd;
        }

        ImageTextButton playButton = new ImageTextButton("PLAY", skin);
        ImageTextButton backButton = new ImageTextButton("BACK", skin);
        ClickListener playButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("Button Clicked: " + event);
                parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level
            }
        };

        ClickListener backButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                levelIntroDialog.hide();
            }
        };
        playButton.addListener(playButtonListener);
        backButton.addListener(backButtonListener);
        levelIntroDialog.getContentTable().add(introButton).expandX();
        //levelIntroDialog.getContentTable().add(introButton).expandX();
        levelIntroDialog.getContentTable().row();
        levelIntroDialog.getContentTable().add(playButton);
        levelIntroDialog.getContentTable().row();
        levelIntroDialog.getContentTable().add(backButton);

        levelIntroDialog.show(stage);
    }

    /**
     * Called when the user try's to buy a new level pack
     * sets prefs to show the levels are available
     * downloads the levels from the server
     * @param levelPackToBuy
     */
    private void buyLevelPack(int levelPackToBuy){
        System.out.println("Looks like user is buying level pack: "+levelPackToBuy);
        boolean success = downloadLevelPack(levelPackToBuy);
        if(success){
            parent.prefs.putBoolean("com.araceinspace.levelPackUnlocked."+levelPackToBuy, true);
            parent.setCoins(parent.getCoins() - coinsToSpend);
            parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
        }

    }

    private boolean downloadLevelPack(int levelPackToBuy){
        boolean success = parent.httpManager.dlLevelPackFromServer(levelPackToBuy);
        if(success){
            System.out.println("downloaded level pack: " + levelPackToBuy);
        }else{
            System.out.println("error downloading level pack: " + levelPackToBuy);
        }
        return success;
    }
}