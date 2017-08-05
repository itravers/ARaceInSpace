package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/17/17.
 * Lets the user choose which level to play
 */
public class LEADERBOARDScreen extends Screen{
    /* Static variables */
    public static int levelClicked;

    int xCoords = 0;
    ArrayList<ImageButton> buyButtons;
    Skin skin;
    int spacer;
    ClickListener coinButtonListener;
    ClickListener backButtonListener;
    ClickListener rewardAdButtonListener;
    ClickListener menuButtonListener;
    ClickListener nextButtonListener;
    ClickListener continueButtonListener;
    ClickListener challengeListener;

    boolean stageLoaded;

    ImageButton starBronze;
    ImageButton starSilver;
    ImageButton starGold;
    ImageButton buyLevelsButton;

    JsonValue leaderBoardLevels;

    public LEADERBOARDScreen(RenderManager parent) {
        super(parent);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        skin.dispose();
    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return null;
    }

    @Override
    public void setup() {
        System.out.println("Settingup LeaderBoardScreen");
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
        stage = new Stage(viewport, batch);

        parent.setupInfoDialog(skin, stage, this);


        JsonReader json = new JsonReader();
        String jsonFromServer = parent.parent.httpManager.readLeaderBoardFromServer();
        JsonValue jsonValue;

        if(jsonFromServer == null){//the server is offline, read from generic leaderboards file
            jsonValue = json.parse(Gdx.files.internal("levels/LeaderBoard.json"));
            leaderBoardLevels = jsonValue.get("levels");

        }else{
            jsonValue = json.parse(jsonFromServer);
            leaderBoardLevels = jsonValue.get(0).get("levels");
        }


       // System.out.println(jsonValue);


        // updateOrientation(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                //System.out.println("Button Clicked: " + event);
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.STORE);
            }

        };

        backButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                parent.parent.gameStateManager.setCurrentState(parent.parent.gameStateManager.popState());
            }

        };

        rewardAdButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                //parent.parent.gameStateManager.setCurrentState(parent.parent.gameStateManager.popState());
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
        nextButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.devMode = ! parent.parent.devMode;
            }

        };
        continueButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
            }

        };
        challengeListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                String buttonName = ((ImageTextButton) event.getListenerActor()).getName();
                buttonName = buttonName.replace("Level ", "");
                int level = Integer.parseInt(buttonName);
                levelClicked = level;
                parent.coinsToSpend = 10;
                parent.placeClicked = RenderManager.PLACES.first;
                parent.purchaseDialog.getTitleLabel().setText("Are you sure you want to spend " + parent.coinsToSpend + " coins?");
                parent.purchaseDialog.show(stage);
            }

        };

        BitmapFont font = skin.getFont("default-font");
        font.getData().setScale(.13f, .66f);
        spacer = 25;
        setupPortraitGUI(viewport.getScreenWidth(), viewport.getScreenHeight());
        if(jsonFromServer == null){
            parent.infoDialog.getTitleLabel().setText("Can't Connect to LeaderBoard");
            parent.infoDialog.show(stage);//if can't connect to leaderboard, show dialog after everything else has been printed
        }

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
        float butWidth = width/2.6f;
        float butHeight = height/4.5f;


        System.out.println("setup portrait    stage w:h " + width + ":" + height);
        Table storeTable;

        //scene2d.ui items
        Table table;
        Table headerTable;
        Table bodyTable;

        ScrollPane scrollPane;

        Label storeTitleLabel1;
        Label storeTitleLabel2;
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
        backButton.addListener(backButtonListener);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(devMode);
        menuButton.addListener(menuButtonListener);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.setDebug(devMode);
        rewardButton.addListener(rewardAdButtonListener);

        System.out.println("density: portrait, " + Gdx.graphics.getDensity());
        storeTitleLabel1 = new Label("LEADER", skin, "coinLabel");
        storeTitleLabel1.setDebug(devMode);
        storeTitleLabel2 = new Label("BOARDS", skin, "coinLabel");
        storeTitleLabel2.setDebug(devMode);

        Table titleTable = new Table();
        titleTable.setDebug(devMode);
        titleTable.add(storeTitleLabel1).align(Align.bottom|Align.center).height(storeTitleLabel1.getHeight()*.65f);
        titleTable.row();
        titleTable.add(storeTitleLabel2).align(Align.top|Align.center).height(storeTitleLabel2.getHeight()*.65f);;

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
        headerTable.add(titleTable).expandX().align(Align.left).size(width/3.5f, height/12);


        headerTable.add(coinLabel).size(width/6, height/12).align(Align.right);
        headerTable.add(coinButton).size(width/8, height/10).padTop(0).padRight(spacer);

        headerTable.row();
        //headerTable.add(rewardButton).size(width/8, height/12).padLeft(spacer/1).padTop(spacer/4).align(Align.top).spaceLeft(0);

        Table extraTable = new Table();
        extraTable.setDebug(devMode);
        extraTable.align(Align.center|Align.top);
        extraTable.add(rewardButton).size(width/8, height/12).padLeft(spacer/1).padTop(spacer/4).align(Align.top).spaceLeft(0);

        Table extraTable2 = new Table();
        extraTable2.setDebug(devMode);
        extraTable2.align(Align.center|Align.top);
        Label taunt1 = new Label("Win A Challenge and become", skin, "extra_small");
        extraTable2.add(taunt1).height(height/30).align(Align.left);

        Label taunt2 = new Label("The New CHAMP!!!", skin, "coinLabel");
        extraTable2.row();
        extraTable2.add(taunt2).height(height/30).align(Align.top);

        extraTable.add(extraTable2).fill().expandX();



        table.add(headerTable).fill().expandX();
        table.row();
        table.add(extraTable).fill().expandX();
        table.row();

        bodyTable = new Table();
        bodyTable.setDebug(devMode);

        bodyTable.align(Align.center); //aligns reward video with back button

        buyButtons = new ArrayList<ImageButton>();

        storeTable = new Table();
        storeTable.setDebug(devMode);
        storeTable.align(Align.top|Align.left);



        Tree tree = new Tree(skin, "default");

        for(int i = 0; i < leaderBoardLevels.size; i++){
            JsonValue level = leaderBoardLevels.get(i);
            String levelName = level.get("name").asString();
            JsonValue data = level.get("data");

            Table parentTable = new Table();
            parentTable.setDebug(devMode);
            ImageTextButton challengeButton = new ImageTextButton("Challenge",skin);
            challengeButton.setName(levelName);
            challengeButton.addListener(challengeListener);
            Label l = new   Label(levelName, skin, "tree_header");
            ImageButton coin10 = new ImageButton(skin, "coinSmall10");
            parentTable.add(l).width(l.getWidth()*1.0f).padLeft(spacer).padRight(spacer);
           // parentTable.add(challengeButton).align(Align.right).width(challengeButton.getWidth()*1.0f);
            //parentTable.add(coin10);


            Tree.Node node = new Tree.Node(parentTable);


            Table tableChild = new Table();
            tableChild.align(Align.left);
            tableChild.setDebug(devMode);

            for(int j = 0; j < data.size; j++){
                String place = data.get(j).get("place").asString();
                String name = data.get(j).get("name").asString();
                String time = data.get(j).get("time").asString();
                String formattedTime = msToString(Integer.valueOf(time));
                String id = data.get(j).get("id").asString();

                //inner for loop here
                Table tablePlace = new Table();
                tablePlace.setDebug(devMode);
                Label labelPlace = new Label(place, skin, "extra_small");
                tablePlace.add(labelPlace).align(Align.left);
                tablePlace.padRight(spacer);

                Table tableName = new Table();
                tableName.setDebug(devMode);
                Label labelName = new Label(name + " - ", skin, "extra_small");
                tableName.add(labelName).align(Align.right);

                Table tableTime = new Table();
                tableTime.setDebug(devMode);
                Label labelTime = new Label(formattedTime, skin, "extra_small");
                tableTime.add(labelTime).align(Align.left);


                tableChild.add(tablePlace).align(Align.left);
                tableChild.add(tableName).align(Align.right);
                tableChild.add(tableTime).align(Align.left);
                tableChild.row();

            }




            //end inner for loop here

            Tree.Node nodeChild = new Tree.Node(tableChild);
            node.add(nodeChild);
            tree.add(node);


        }

        tree.expandAll();


        storeTable.add(tree).align(Align.top|Align.left).expandX();

       // storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        scrollPane = new ScrollPane(storeTable, skin, "default");

        ImageButton previousLevelButton = new ImageButton(skin, "previousLevelButton");
        previousLevelButton.setWidth(width*.10f);
        //nextLevelButton.rotateBy(180);

        buyLevelsButton = new ImageButton(skin, "nextLevelButton");
        buyLevelsButton.setWidth(width*.10f);
        buyLevelsButton.addListener(nextButtonListener);



        bodyTable.add(previousLevelButton).width(width*.10f).align(Align.left);
        bodyTable.add(scrollPane).width(width*.78f).height(height*.755f).padLeft(0).align(Align.top|Align.center);//set the scroll pane size
        bodyTable.add(buyLevelsButton).width(width*.10f).align(Align.right);


        ImageTextButton continueButton = new ImageTextButton("Continue", skin);
        continueButton.addListener(continueButtonListener);
        table.add(continueButton).width(viewport.getScreenWidth()/2);
        table.row();
        table.add(bodyTable).fill().expandX();
        stage.addActor(table);

        stageLoaded = true;
    }

    private String msToString(int playerMS){
        if(playerMS == 99999999)return "00:00:00";
        //Calculate player time min, sec and ms.
        int time = playerMS;
        int min = (time / 1000) / 60;
        time -= (min * 60 * 1000);
        int sec = time / 1000;
        time -= (sec * 1000);
        int ms = time;
        String playerMin = Integer.toString(min);
        if(playerMin.length() < 2)playerMin = "0"+playerMin;

        String playerSec = Integer.toString(sec);
        if(playerSec.length() < 2)playerSec = "0"+playerSec;

        String playerms = Integer.toString(ms);
        if(playerms.length() < 2)playerms = "0"+playerms;

        return playerMin + ":" +playerSec + ":" + playerms;
    }
}
