package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/17/17.
 * Lets the user choose which challenge to try on this particular level
 */
public class SCOREScreen extends Screen{

    int xCoords = 0;
    ArrayList<ImageButton> buyButtons;
    Skin skin;
    int spacer;
    ClickListener coinButtonListener;
    ClickListener backButtonListener;
    ClickListener rewardAdButtonListener;
    ClickListener menuButtonListener;
    ClickListener leaderBoardListener;
    ClickListener challengeListener;
    ClickListener tryAgainListener;
    ClickListener continueListener;
    ClickListener submitGhostButtonListener;
    ImageTextButton submitGhostButton;//must be global so we can hide it
    String challenger; //must be global so we can reference it in buttonpress

    boolean stageLoaded;
    int challengerTime;
    int playerTime;
    int place;
    enum WIN {WIN, LOSE, TIE, NONE, FAIL}
    WIN win;
    LevelManager.CHALLENGES currentChallenge;


    public SCOREScreen(RenderManager parent) {
        super(parent);

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
       // skin.dispose();
    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return null;
    }

    @Override
    public void setup() {
        System.out.println("Settingup storeScreen");

        currentChallenge = parent.parent.levelManager.currentChallenge;
        if(currentChallenge == LevelManager.CHALLENGES.first){
            place = 1;
        }else if(currentChallenge == LevelManager.CHALLENGES.second){
            place = 2;
        }else if(currentChallenge == LevelManager.CHALLENGES.third){
            place = 3;
        }

        challengerTime = parent.parent.levelManager.ghostTime;
        playerTime = parent.parent.levelManager.playerTime;
        if(parent.parent.levelManager.didFail){
            win = WIN.FAIL;
        }else if(challengerTime == 0){
            //there was no challenger, didn't win or lose
            win = WIN.NONE;
        }else if(challengerTime < playerTime){
            win = WIN.LOSE;
        }else if(challengerTime > playerTime){
            win = WIN.WIN;
        }else if(playerTime == challengerTime){
            win = WIN.TIE;
        }

        stage = new Stage(viewport, batch);
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.STORE);
                //parent.parent.devMode = !parent.parent.devMode;
            }

        };

        backButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(parent.parent.gameStateManager.popState());
            }

        };

        rewardAdButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.loadRewardAd();
                monetizationController.showRewardAd();
            }

        };

        menuButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.MENU);
            }

        };

        leaderBoardListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEADERBOARDS);
            }

        };

        submitGhostButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
               //TODO implement submitghostbutton in score screen
                System.out.println("Submit Ghost Button hit");
                int level = parent.parent.levelManager.getCurrentLevel();
                String name = parent.parent.playerName;
                String submitVal = parent.parent.httpManager.submitScore(level, place, name, playerTime);

                if(submitVal == null){
                    parent.parent.dialogManager.infoDialog.getTitleLabel().setText("Couldn't Connect To Backend Server");
                    parent.parent.dialogManager.infoDialog.show(stage);
                }else if(submitVal.startsWith("success")){
                    String id = submitVal.replace("success:", "");
                    parent.parent.httpManager.submitGhostReplay(parent.parent.levelManager.getGhostReplay(), id, level, place, name, playerTime);
                    parent.parent.dialogManager.infoDialog.getTitleLabel().setText("Success!");
                    parent.parent.dialogManager.infoDialog.show(stage);
                    submitGhostButton.setVisible(false);
                }else if(submitVal.equals("Not Fast Enough")){
                    parent.parent.dialogManager.infoDialog.getTitleLabel().setText("You weren't fast enough to place " + challenger + " in Leaderboards!");
                    parent.parent.dialogManager.infoDialog.show(stage);
                    submitGhostButton.setVisible(false);
                }
                //System.out.println(submitVal);
            }

        };

        challengeListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEADERBOARDS);
                System.out.println("Challenge Listener not implemented yet");
                //TODO implement challenge listner in score screen
            }

        };

        tryAgainListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
               // parent.placeClicked = RenderManager.PLACES.second;
                //parent.coinsToSpend = 9;
                LevelManager.CHALLENGES currentChallenge = parent.parent.levelManager.getCurrentChallenge();
                if(currentChallenge == LevelManager.CHALLENGES.bronze || currentChallenge == LevelManager.CHALLENGES.silver || currentChallenge == LevelManager.CHALLENGES.gold){
                    parent.parent.levelManager.setLevel(parent.parent.levelManager.getCurrentLevel());
                    parent.parent.levelManager.setChallenge(parent.parent.levelManager.getCurrentChallenge());
                    parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
                }else{//fir first second and third place challenges
                    parent.parent.dialogManager.purchaseDialog.getTitleLabel().setText("Are you sure you want to spend " + parent.coinsToSpend + " coins?");
                    parent.parent.dialogManager.purchaseDialog.show(stage);
                }
            }

        };

        continueListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
            }

        };

       // TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
       // skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
        skin = parent.parent.resourceManager.getSkin();
        parent.parent.dialogManager.setupInfoDialog(skin, stage, this);

        BitmapFont font = skin.getFont("default-font");
        font.getData().setScale(.13f, .66f);
        spacer = 25;

        setupPortraitGUI(viewport.getScreenWidth(), viewport.getScreenHeight());
        monetizationController.showBannerAd();
    }

    @Override
    public void render(float elapsedTime) {
        xCoords++;
        if(xCoords >= Gdx.graphics.getWidth()){
            xCoords = 0;
        }
        Gdx.gl.glClearColor(.447f, .2784f, .3843f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        parent.parent.inputManager.addInputProcessor(stage);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void setupPortraitGUI(float width, float height){
        boolean devMode = parent.parent.devMode;
        stageLoaded = false;
        float butWidth = width/3.2f;
        float butHeight = height/5f;

        System.out.println("setup portrait    stage w:h " + width + ":" + height);
        // System.out.println("setup portrait viewport w:h " + viewport.getScreenWidth() + ":" + viewport.getScreenHeight());
        Table storeTable;

        //scene2d.ui items
        Table table;
        Table headerTable;
        Table bodyTable;

        ScrollPane scrollPane;

        Label storeTitleLabel;
        ImageButton backButton;
        ImageButton menuButton;
        ImageButton coinButton;

        ImageButton rewardButton;

        table = new Table();
        table.setDebug(devMode);
        table.setWidth(width);
        table.align(Align.center|Align.top);
        table.setPosition(0, height);

        backButton = new ImageButton(skin, "backButton");
        backButton.setDebug(devMode);
        backButton.addListener(tryAgainListener);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(devMode);
        menuButton.addListener(menuButtonListener);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.setDebug(devMode);
        rewardButton.addListener(rewardAdButtonListener);

        System.out.println("density: portrait, " + Gdx.graphics.getDensity());
        storeTitleLabel = new Label("Score", skin, "Store_Title");
        storeTitleLabel.setDebug(devMode);

        String coins = Integer.toString(parent.parent.getCoins());
        coinLabel = new Label(coins, skin, "coinLabel");
        coinLabel.setAlignment(Align.right);

        coinButton = new ImageButton(skin, "coinButton");
        coinButton.addListener(coinButtonListener);

        headerTable = new Table();
        headerTable.setDebug(devMode);
        headerTable.align(Align.center|Align.top);

        headerTable.add(backButton).padLeft(spacer/4).padTop(0).size(width/8, height/10);
        headerTable.add(menuButton).padLeft(spacer/4).padTop(0).align(Align.left).size(width/8, height/10);
        headerTable.add(storeTitleLabel).expandX().align(Align.center).size(width/3.5f, height/12);

        float fontWidth = storeTitleLabel.getStyle().font.getSpaceWidth()*storeTitleLabel.getText().length();

        headerTable.add(coinLabel).size(width/6, height/12).align(Align.right);
        headerTable.add(coinButton).size(width/8, height/10).padTop(0).padRight(spacer);

        headerTable.row();

        Table extraTable = new Table();
        extraTable.setDebug(devMode);
        extraTable.align(Align.center|Align.top);
        extraTable.add(rewardButton).size(width/8, height/12).padLeft(spacer/1).padTop(spacer/4).align(Align.top).spaceLeft(0);

        Table extraTable2 = new Table();
        extraTable2.setDebug(devMode);
        extraTable2.align(Align.center|Align.top);

        String taunt1_S = "";
        String taunt2_S = "";
        String taunt3_S = "";
        String info1S = "";

        if(win == WIN.WIN){
            taunt1_S = " Nice..   ";
            taunt2_S = " You Conquered";
            taunt3_S = " That Level!!!";
            info1S = "You Won Against the";
        }else if(win == WIN.LOSE || win == WIN.FAIL){
            taunt1_S = " Awww..   ";
            taunt2_S = " You Bombed";
            taunt3_S = " That Level!!!";
            info1S = "You Lost Against the";
        }else if(win == WIN.TIE){
            taunt1_S = "  Wow..   ";
            taunt2_S = " You Tied";
            taunt3_S = " That Level!!!";
            info1S = "You Tied Against the";
        }else if(win == WIN.NONE){
            taunt1_S = "  Hmm..   ";
            taunt2_S = " Playing With";
            taunt3_S = " Yourself again";
            info1S = "You Played against";
        }

        Label taunt1 = new Label( taunt1_S, skin, "extra_small");
        extraTable2.add(taunt1).height(height/30).align(Align.left);

        Label taunt2 = new Label(taunt2_S, skin, "coinLabel");
        extraTable2.row();
        extraTable2.add(taunt2).height(height/30).align(Align.top);

        Label taunt3 = new Label(taunt3_S, skin, "extra_small");
        extraTable2.row();
        extraTable2.add(taunt3).height(height/30).align(Align.top);

        extraTable.add(extraTable2).fill().expandX();

        table.add(headerTable).fill().expandX();
        table.row();
        table.add(extraTable).fill().expandX();
        table.row();

        bodyTable = new Table();
        bodyTable.setDebug(devMode);

        bodyTable.align(Align.top|Align.center); //aligns reward video with back button

        buyButtons = new ArrayList<ImageButton>();

        storeTable = new Table();
        storeTable.setDebug(devMode);
        storeTable.align(Align.top|Align.center);

        Table buttonTable = new Table();
        buttonTable.setDebug(devMode);
        buttonTable.align(Align.top|Align.center);

        Window w = new Window("", skin);
        //w.setSize(viewport.getScreenWidth()*.95f, viewport.getScreenHeight()/5);
        w.setMovable(false);

        Table t = new Table();
        t.setDebug(devMode);

        Table t1 = new Table();
        t1.setDebug(devMode);

        Label yourTime = new Label("      Your Time:", skin, "taunt_small");

        Table t1B = new Table();
        t1B.setDebug(devMode);


        //Calculate player time min, sec and ms.
        int time = playerTime;
        int min = (time / 1000) / 60;
        time -= (min * 60 * 1000);
        int sec = time / 1000;
        time -= (sec * 1000);
        int ms = time;
        String playerMin = Integer.toString(min);
        String playerSec = Integer.toString(sec);
        String playerms = Integer.toString(ms);

        // The user exploading during last level, display xx as users time
        if(win == WIN.FAIL){
            playerMin = "XX";
            playerSec = "XX";
            playerms  = "XX ";
        }

        Label minLabel1 = new Label(playerMin + "  Minutes", skin, "taunt_small");
        Label secLabel1 = new Label(playerSec + "  Seconds", skin, "taunt_small");
        Label msLabel1 = new Label( playerms + " MS", skin, "taunt_small");

        t1B.add(minLabel1).align(Align.left).height(minLabel1.getHeight()*.62f);
        t1B.row();
        t1B.add(secLabel1).align(Align.left).height(secLabel1.getHeight()*.62f);;
        t1B.row();
        t1B.add(msLabel1).align(Align.left).height(msLabel1.getHeight()*.62f);;


        t1.add(yourTime).align(Align.top).padRight(spacer/2).height(yourTime.getHeight()*.62f);
        t1.add(t1B);

        //t2
        Table t2 = new Table();
        t1.setDebug(devMode);

        Label competitorTime = new Label("Challenger Time:", skin, "taunt_small");

        //Calculate ghost time min, sec and ms.
        time = challengerTime;
        if(win == WIN.FAIL)time = parent.parent.levelManager.ghostTime;
        min = (time / 1000) / 60;
        time -= (min * 60 * 1000);
        sec = time / 1000;
        time -= (sec * 1000);
        ms = time;
        String ghostMin = Integer.toString(min);
        String ghostSec = Integer.toString(sec);
        String ghostms = Integer.toString(ms);

        Table t2B = new Table();
        t2B.setDebug(devMode);
        Label minLabel2 = new Label(ghostMin + " Minutes", skin, "taunt_small");
        Label secLabel2 = new Label(ghostSec + " Seconds", skin, "taunt_small");
        Label msLabel2 = new Label( ghostms  + " MS", skin, "taunt_small");

        t2B.add(minLabel2).align(Align.left).height(minLabel2.getHeight()*.62f);
        t2B.row();
        t2B.add(secLabel2).align(Align.left).height(secLabel2.getHeight()*.62f);;
        t2B.row();
        t2B.add(msLabel2).align(Align.left).height(msLabel2.getHeight()*.62f);;


        t2.add(competitorTime).align(Align.top).padRight(spacer/2).height(competitorTime.getHeight()*.62f);
        t2.add(t2B);

        t.add(t1).align(Align.top);
        t.row();
        t.add(t2).align(Align.top).padTop(spacer/2).padBottom(spacer);

        w.add(t).align(Align.top).padTop(spacer/2);

        buttonTable.add(w).width(viewport.getScreenWidth()*.80f).height( viewport.getScreenHeight()/4).padTop(spacer);

        storeTable.add(buttonTable).align(Align.center);
        storeTable.row();

        Table infoTable = new Table();
        infoTable.setDebug(devMode);

        challenger = "";

        if(currentChallenge == LevelManager.CHALLENGES.bronze){
            challenger = "Bronze";
        }else if(currentChallenge == LevelManager.CHALLENGES.silver){
            challenger = "Silver";
        }else if(currentChallenge == LevelManager.CHALLENGES.gold){
            challenger = "Gold";
        }else if(currentChallenge == LevelManager.CHALLENGES.first){
            challenger = "First Place";
        }else if(currentChallenge == LevelManager.CHALLENGES.second){
            challenger = "Second Place";
        }else if(currentChallenge == LevelManager.CHALLENGES.third){
            challenger = "Third Place";
        }


        Label info1 = new Label(info1S, skin, "taunt_small");
        Label info2 = new Label(challenger, skin, "coinLabel");
        Label info3 = new Label("Challenger", skin, "taunt_small");

        infoTable.add(info1).height(info1.getHeight()*.60f);
        infoTable.row();
        infoTable.add(info2).height(info2.getHeight()*.5f);
        infoTable.row();
        infoTable.add(info3).height(info3.getHeight()*.65f);

        storeTable.add(infoTable).padTop(spacer);
        storeTable.row();

        submitGhostButton = new ImageTextButton("Submit "+challenger+" Score", skin);
        submitGhostButton.addListener(submitGhostButtonListener);

        ImageTextButton continueButton = new ImageTextButton("Continue", skin);
        continueButton.addListener(continueListener);

        Table bTable = new Table();
        bTable.setDebug(devMode);
        ImageTextButton lButton = new ImageTextButton("LeaderBoards", skin);
        lButton.addListener(leaderBoardListener);

       // Table challengeButtonTable = new Table();
       // challengeButtonTable.setDebug(devMode);

       // ImageTextButton cButton = new ImageTextButton("Challenge First Place", skin);
       // cButton.addListener(challengeListener);
        //ImageButton c = new ImageButton(skin, "coinSmall10");
       // c.setTouchable(Touchable.disabled);

       // challengeButtonTable.add(cButton).width((viewport.getScreenWidth()*.80f)-c.getWidth()).height(viewport.getScreenHeight()/14);;
       // challengeButtonTable.add(c);

        ImageTextButton tryAgainButton = new ImageTextButton("Try Again", skin);
        tryAgainButton.addListener(tryAgainListener);

        if(win == WIN.WIN && (currentChallenge == LevelManager.CHALLENGES.first || currentChallenge == LevelManager.CHALLENGES.second || currentChallenge == LevelManager.CHALLENGES.third)){
            //give player the option to submit score if he won a first second or third place challenge
            bTable.add(submitGhostButton).width(viewport.getScreenWidth()*.80f).height(viewport.getScreenHeight()/14);
            bTable.row();
        }

        bTable.add(continueButton).width(viewport.getScreenWidth()*.80f).height(viewport.getScreenHeight()/14);
        bTable.row();
       // bTable.add(tryAgainButton).width(viewport.getScreenWidth()*.80f).height(viewport.getScreenHeight()/14);
       // bTable.row();
       // bTable.add(challengeButtonTable);
       // bTable.row();
        bTable.add(lButton).width(viewport.getScreenWidth()*.80f).height(viewport.getScreenHeight()/14);

        storeTable.add(bTable).padTop(spacer);

        scrollPane = new ScrollPane(storeTable, skin, "default");

        bodyTable.add(scrollPane).width(width*.99f).height(height*.755f).padLeft(0).align(Align.top|Align.center);//set the scroll pane size

        table.add(bodyTable).fill().expandX();
        stage.addActor(table);
        stageLoaded = true;
        parent.parent.levelManager.didFail = false;//reset the didfail boolean
    }
}