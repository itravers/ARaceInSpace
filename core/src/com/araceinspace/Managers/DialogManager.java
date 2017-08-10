package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.Screens.LEADERBOARDScreen;
import com.araceinspace.Screens.SCOREScreen;
import com.araceinspace.Screens.Screen;
import com.araceinspace.misc.RandomString;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

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

    /* Constructor */
    public DialogManager(GameWorld p){
        parent = p;
    }

    /* Private Methods */

    /* Public Methods */

    public void setupDialogs(Skin skin, final Stage stage, final Screen screen) {
        purchaseDialog = new Dialog("Are you sure you want to spend " + parent.renderManager.coinsToSpend + " coins?", skin) {
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

                        parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
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

    public void setupNameDialog(Skin skin, Stage stage, Screen screen){
        final TextArea textArea = new TextArea("", skin);
        textArea.setMaxLength(8);
        ImageTextButton submitButton = new ImageTextButton("Submit", skin);
        final RandomString randomString = new RandomString(4);
        nameDialog = new Dialog("What Is Your Name?", skin, "dialog"){
            protected void result(Object object) {
                String name = textArea.getText();
                if(name.isEmpty()){
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
        // nameDialog.add(textArea);
        nameDialog.getContentTable().add(textArea).expandX();
        nameDialog.button(submitButton);
    }

    public void setupLoadingLevelDialog(Skin skin, Stage stage, Screen screen, final int ghostLevel, final String jsonOfGhost){
        levelLoadingDialog = new Dialog("Loading Level: " + ghostLevel, skin, "dialog"){
            protected void result(Object object) {
                parent.levelManager.setLevel(ghostLevel);
                parent.levelManager.setupGhostFromJson(jsonOfGhost);
                parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level
            }
        };
        ImageTextButton okButton = new ImageTextButton("OK...", skin);
        levelLoadingDialog.button(okButton);
    }
}