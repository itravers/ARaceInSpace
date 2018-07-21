package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.Screens.LEADERBOARDScreen;
import com.araceinspace.Screens.SCOREScreen;
import com.araceinspace.Screens.Screen;
import com.araceinspace.misc.CustomDialog;
import com.araceinspace.misc.FreetypeFontLoader;
import com.araceinspace.misc.RandomString;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Isaac Assega on 8/6/17.
 * Controls all Pop-up Dialogs that the player interacts with
 */
public class DialogManager {
    /* Static Variables */

    /* Field Variables */
    GameWorld parent;
    public boolean dialogQuestion = false;
    public Dialog purchaseDialog;
    public Dialog notEnoughCoinsDialog;
    public Dialog infoDialog;
    public Dialog nameDialog;
    public Dialog levelLoadingDialog;
    public Dialog levelIntroDialog;
    BitmapFont titleFont;
    BitmapFont queryFont;

    /* Constructor */
    public DialogManager(GameWorld p){
        parent = p;
        titleFont = FreetypeFontLoader.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 25);
        queryFont = FreetypeFontLoader.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 14);
    }

    /* Private Methods */

    /* Public Methods */

    public void setupDialogs(final Skin skin, final Stage stage, final Screen screen) {
        purchaseDialog = new CustomDialog("Are you sure you want to spend " + parent.renderManager.coinsToSpend + " coins?", skin, screen.getViewport().getScreenWidth()*.75f, screen.getViewport().getScreenHeight(), queryFont) {
            protected void result(Object object) {
                if (object.toString().equals("true")) {
                    dialogQuestion = true;
                } else {
                    dialogQuestion = false;
                }
                if (dialogQuestion) {
                    if (parent.getCoins() >= parent.renderManager.coinsToSpend) {
                        if(screen instanceof SCOREScreen){
                            parent.levelManager.setLevel(parent.levelManager.getCurrentLevel());
                        }else if(screen instanceof LEADERBOARDScreen){
                            parent.levelManager.setLevel(LEADERBOARDScreen.levelClicked);//set in leaderboard screen
                        }

                        if (parent.renderManager.placeClicked == RenderManager.PLACES.first) {
                            parent.levelManager.setChallenge(LevelManager.CHALLENGES.first);
                        } else if (parent.renderManager.placeClicked == RenderManager.PLACES.second) {
                            parent.levelManager.setChallenge(LevelManager.CHALLENGES.second);
                        } else if (parent.renderManager.placeClicked == RenderManager.PLACES.third) {
                            parent.levelManager.setChallenge(LevelManager.CHALLENGES.third);
                        } else{
                            parent.levelManager.setChallenge(parent.levelManager.getCurrentChallenge());
                        }
                        parent.setCoins(parent.getCoins() - parent.renderManager.coinsToSpend);

                        //parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
                        parent.levelManager.playGame(skin, stage, stage.getViewport());
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

        notEnoughCoinsDialog = new Dialog("Not Enough Coins", skin);
        ImageTextButton oh = new ImageTextButton("Oh...", skin);
        notEnoughCoinsDialog.button(oh);



    }

    public void setupInfoDialog(Skin skin, Stage stage, Screen scren){
        infoDialog = new Dialog("You Are Offline", skin, "dialog");
        ImageTextButton okButton = new ImageTextButton("OK...", skin);
        infoDialog.button(okButton);
    }

    public void setupNameDialog(Skin skin, Stage stage, Viewport viewport){



        final TextArea textArea = new TextArea("", skin);
        textArea.setMaxLength(8);
        textArea.getStyle().font = titleFont;
        ImageTextButton submitButton = new ImageTextButton("SUBMIT", skin);
        final RandomString randomString = new RandomString(4);
        nameDialog = new CustomDialog("What is Your Name?", skin, viewport.getScreenWidth()*.75f, viewport.getScreenHeight(), titleFont){
            protected void result(Object object) {
                String name = textArea.getText();
                if(name.isEmpty() || name.contains("MongoError")){ //don't allow these to be names, instead force guest name
                    name = "Guest "+randomString.nextString();
                }
                name = name.replaceAll("\\s", "-");
                parent.playerName = name;
                System.out.println("Name is set to: " + parent.playerName);
                parent.prefs.putString("com.araceinspace.playerName", name);
                parent.prefs.flush();
                Gdx.input.setOnscreenKeyboardVisible(false);
            }
        };
        nameDialog.getContentTable().add(textArea).expandX().width(viewport.getScreenWidth()*.50f).height(viewport.getScreenHeight()/14);

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
        levelLoadingDialog = new CustomDialog("Loading Level: " + ghostLevel, skin, viewport.getScreenWidth()*.75f, viewport.getScreenHeight(), titleFont){
            protected void result(Object object) {
                parent.levelManager.setLevel(ghostLevel);
                parent.levelManager.setupGhostFromJson(jsonOfGhost);
                parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level
            }
        };
        ImageTextButton okButton = new ImageTextButton("OK...", skin);
        levelLoadingDialog.button(okButton);
    }

    /**
     * Dialog used before level to show player the goal
     */
    public void setupLevelIntroDialog(int level, Skin skin, Stage stage, Viewport viewport){
        levelIntroDialog = new CustomDialog("Level "+level, skin, viewport.getScreenWidth()*1f, viewport.getScreenHeight()*.65f, titleFont){
            public void result(Object object) {
               // parent.levelManager.setLevel(ghostLevel);
                //parent.levelManager.setupGhostFromJson(jsonOfGhost);
               // parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level
            }
        };

        levelIntroDialog.getTitleTable().getCell(levelIntroDialog.getTitleLabel()).expandX().align(Align.center);

        Label introLabel = new Label("Land On the Planet", skin, "taunt_small");
        introLabel.getStyle().font = titleFont;
       // levelIntroDialog.getTitleLabel().setAlignment(Align.center);
       // levelIntroDialog.setDebug(parent.devMode);
        //levelIntroDialog.getTitleTable().getCells().get(0).expandX();
        //levelIntroDialog.getTitleTable().setDebug(parent.devMode);
        levelIntroDialog.getTitleTable().padTop(200);

        //levelIntroDialog.getContentTable().padTop(0);
        levelIntroDialog.getContentTable().add(introLabel).expandX();

        //levelIntroDialog.setFillParent(true);

        //levelIntroDialog.setSize(viewport.getScreenWidth(),viewport.getScreenHeight());
        levelIntroDialog.getContentTable().row();

        levelIntroDialog.getContentTable().setDebug(parent.devMode);
        //levelIntroDialog.getButtonTable().setDebug(parent.devMode);

        levelIntroDialog.align(Align.center);
        ImageButton introButton = new ImageButton(skin, "introtest");
       // levelIntroDialog.getContentTable().setFillParent(true);
        //levelIntroDialog.getButtonTable().add(introButton);
        ImageTextButton button = new ImageTextButton("PLAY!", skin);
        ClickListener buttonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                System.out.println("Button Clicked: " + event);
                parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level
            }
        };
        button.addListener(buttonListener);
        //button(button);
       // levelIntroDialog.getButtonTable().add(button).fillX();
        levelIntroDialog.getContentTable().add(introButton);
        levelIntroDialog.getContentTable().row();
        levelIntroDialog.getContentTable().add(button);

        levelIntroDialog.show(stage);
    }
}
