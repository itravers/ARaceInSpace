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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/17/17.
 * Lets the user choose which challenge to try on this particular level
 */
public class PREGAMEScreen extends Screen{

    int xCoords = 0;
    ArrayList<ImageButton> buyButtons;
    Skin skin;
    int spacer;
    ClickListener coinButtonListener;
    ClickListener backButtonListener;
    ClickListener rewardAdButtonListener;
    ClickListener menuButtonListener;
    ClickListener bronzeListener;
    ClickListener silverListener;
    ClickListener goldListener;
    ClickListener firstPlaceListener;
    ClickListener secondPlaceListener;
    ClickListener thirdPlaceListener;
    ClickListener playButtonListener;
    TextField textField;
    private PREGAMEScreen me;




    boolean stageLoaded;

    public PREGAMEScreen(RenderManager parent) {
        super(parent);
        me = this;
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return null;
    }

    @Override
    public void setup() {
        System.out.println("Settingup PregameScreen");
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
        stage = new Stage(viewport, batch);
        parent.setupDialogs(skin, stage, this);
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.STORE);
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
        bronzeListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.levelManager.setChallenge(LevelManager.CHALLENGES.bronze);
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
            }

        };
        silverListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.levelManager.setChallenge(LevelManager.CHALLENGES.silver);
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
            }

        };
        goldListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.levelManager.setChallenge(LevelManager.CHALLENGES.gold);
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
            }

        };
        firstPlaceListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.placeClicked = RenderManager.PLACES.first;
                parent.coinsToSpend = 10;
                parent.purchaseDialog.getTitleLabel().setText("Are you sure you want to spend " + parent.coinsToSpend + " coins?");
                parent.purchaseDialog.show(stage);
            }
        };
        secondPlaceListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.placeClicked = RenderManager.PLACES.second;
                parent.coinsToSpend = 9;
                parent.purchaseDialog.getTitleLabel().setText("Are you sure you want to spend " + parent.coinsToSpend + " coins?");
                parent.purchaseDialog.show(stage);
            }

        };
        thirdPlaceListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.placeClicked = RenderManager.PLACES.third;
                parent.coinsToSpend = 8;
                parent.purchaseDialog.getTitleLabel().setText("Are you sure you want to spend " + parent.coinsToSpend + " coins?");
                parent.purchaseDialog.show(stage);
            }

        };

        playButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                int level = parent.parent.levelManager.getCurrentLevel();
                String ghostID = textField.getText();

                String ghostJSON = parent.parent.httpManager.readCustomGhostFromServer(ghostID, level);
                System.out.println("loading ghost: " + ghostID + " for level: " + level);

                if(ghostJSON == null || ghostJSON.startsWith("error:")){
                    if(ghostJSON == null)ghostJSON = "Error: Problem Connecting to Server";
                    parent.setupInfoDialog(skin, stage, me);
                    parent.infoDialog.getTitleLabel().setText(ghostJSON);
                    parent.infoDialog.show(stage);
                }else{
                    //System.out.println(ghostJSON);
                    JsonReader jsonReader = new JsonReader();

                    JsonValue jsonValue  =  jsonReader.parse(ghostJSON);


                    int ghostLevel = jsonValue.get(0).get("level").asInt();
                    JsonValue ghost = jsonValue.get(0).get("ghost");
                    String jsonOfGhost = ghost.toJson(JsonWriter.OutputType.json);

                    /**
                     * If the level of the ghost doesn't equal the current level we want to load
                     * level of the ghost instead of the current level.
                     * If the current level doesn't match, load a dialog letting the player
                     * know a different level is going to be loaded. When player ok's the dialog
                     * the level will be loaded.
                     */
                    if(ghostLevel != level){
                        parent.setupLoadingLevelDialog(skin, stage, me, ghostLevel, jsonOfGhost);
                        parent.levelLoadingDialog.show(stage); //set level, setup ghost, and change game state will be done in dialog callback
                    }else{
                        parent.parent.levelManager.setupGhostFromJson(jsonOfGhost);
                        parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);//start level
                    }


                }
            }

        };



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




    private Stack makeButtonStack(float butWidth, float butHeight, String title, ClickListener listener, ImageButton star, String s_taunt1, String s_taunt2){
        butHeight = butHeight - spacer/4;

        boolean devMode = parent.parent.devMode;
        ImageButton levelButton = new ImageButton(skin, "storeButton");
        levelButton.setName(title);
        levelButton.addListener(listener);

        Table buttonTable = new Table();
        buttonTable.setDebug(devMode);
        buttonTable.add(levelButton).size(butWidth, butHeight);
        levelButton.getImageCell().expand().fill();

        //create stuff to put in table button
        Label titleLabel = new Label(title, skin, "button_title");
        titleLabel.setTouchable(Touchable.disabled);
        titleLabel.setFontScale(.9f); //WORKS for rizing font, but we also change table container

        ImageButton button_image;

        button_image = new ImageButton(skin, "coinButton_small");
        button_image.setTouchable(Touchable.disabled);



        Table starTable = new Table();
        starTable.setDebug(devMode);
        starTable.align(Align.center);

        starTable.add(star);


        Label taunt1 = new Label(s_taunt1, skin, "taunt_small");
        taunt1.setAlignment(Align.top);
        taunt1.setTouchable(Touchable.disabled);
        taunt1.setFontScale(.7f);

        Label yourTime = new Label("Your Time", skin, "taunt_small");
        yourTime.setAlignment(Align.bottom);
        yourTime.setTouchable(Touchable.disabled);
        yourTime.setFontScale(.8f);

        Label taunt2 = new Label(s_taunt2, skin, "taunt_small");
        taunt2.setTouchable(Touchable.disabled);
        taunt2.setAlignment(Align.top);
        taunt2.setFontScale(.6f);

        //create table in buttons
        Table purchaseTable = new Table();
        purchaseTable.setDebug(devMode);
        purchaseTable.align(Align.center);

        //Header for purchase table
        Table purchaseHeaderTable = new Table();
        purchaseHeaderTable.setDebug(devMode);
        purchaseHeaderTable.align(Align.top|Align.center);
        purchaseHeaderTable.add(titleLabel).padRight(spacer/4).align(Align.left);//.size(width/8, stage.getHeight()/20);
        purchaseTable.add(purchaseHeaderTable).align(Align.top);
        purchaseTable.row();

        Table purchaseBodyTable = new Table();
        purchaseBodyTable.setDebug(devMode);
        purchaseBodyTable.align(Align.top|Align.center);

        purchaseBodyTable.add(taunt1).expandX().align(Align.top);
        purchaseBodyTable.row();

        purchaseBodyTable.add(starTable).expandX().align(Align.top);
        purchaseBodyTable.row();

        purchaseBodyTable.add(yourTime).expandX();
        purchaseBodyTable.row();

        purchaseBodyTable.add(taunt2).expandX().align(Align.top).padBottom(30);
        purchaseBodyTable.row();

        purchaseTable.add(purchaseBodyTable).align(Align.top);

        Stack buttonStack;
        buttonStack = new Stack();
        buttonStack.add(buttonTable);
        buttonStack.add(purchaseTable);
        return buttonStack;
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
        backButton.addListener(backButtonListener);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(devMode);
        menuButton.addListener(menuButtonListener);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.setDebug(devMode);
        rewardButton.addListener(rewardAdButtonListener);

        System.out.println("density: portrait, " + Gdx.graphics.getDensity());
        storeTitleLabel = new Label("Choose", skin, "Store_Title");
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
        headerTable.add(storeTitleLabel).expandX().align(Align.left).size(width/3.5f, height/12);

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
        Label taunt1 = new Label(" Your    ", skin, "extra_small");
        extraTable2.add(taunt1).height(height/30).align(Align.left);

        Label taunt2 = new Label(" Challenge!!!", skin, "coinLabel");
        extraTable2.row();
        extraTable2.add(taunt2).height(height/30).align(Align.top);

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


        ImageButton starBronze = new ImageButton(skin, "starBronze");
        starBronze.setTouchable(Touchable.disabled);
        Stack buttonStack1 = makeButtonStack(butWidth, butHeight, "Bronze", bronzeListener, starBronze, "taunt1", "taunt2");

        ImageButton starSilver = new ImageButton(skin, "starSilver");
        starSilver.setTouchable(Touchable.disabled);
        Stack buttonStack2 = makeButtonStack(butWidth, butHeight, "Silver", silverListener, starSilver, "52:23", "52:21");

        ImageButton starGold = new ImageButton(skin, "starGold");
        starGold.setTouchable(Touchable.disabled);
        Stack buttonStack3 = makeButtonStack(butWidth, butHeight, "Gold",  goldListener, starGold, "43:17", "52:17");

        buttonTable.add(buttonStack1).pad(0).size(butWidth, butHeight).align(Align.top);
        buttonTable.add(buttonStack2).pad(0).size(butWidth, butHeight).align(Align.top);
        buttonTable.add(buttonStack3).pad(0).size(butWidth, butHeight).align(Align.top);

        storeTable.add(buttonTable).align(Align.center);
        storeTable.row();


        Table t1 = new Table();
        t1.setDebug(devMode);
        Label firstPlaceLabel = new Label("First Place", skin, "taunt_small");
        firstPlaceLabel.setTouchable(Touchable.disabled);
        firstPlaceLabel.setFontScale(.7f);

        ImageButton coin1 = new ImageButton(skin, "coinSmall10");
        coin1.setTouchable(Touchable.disabled);

        Table t1Header = new Table();
        t1Header.setDebug(devMode);
        t1Header.align(Align.top|Align.center);

        t1Header.add(firstPlaceLabel);
        t1Header.add(coin1);
        t1.add(t1Header);
        t1.row();

        ImageButton gold = new ImageButton(skin, "star1");
        gold.addListener(firstPlaceListener);
        t1.add(gold).align(Align.center);



        Table t2 = new Table();
        t2.setDebug(devMode);
        Label secondPlaceLabel = new Label("Second Place", skin, "taunt_small");
        secondPlaceLabel.setTouchable(Touchable.disabled);
        secondPlaceLabel.setFontScale(.7f);

        ImageButton coin2 = new ImageButton(skin, "coinSmall9");
        coin2.setTouchable(Touchable.disabled);

        Table t2Header = new Table();
        t2Header.setDebug(devMode);
        t2Header.align(Align.top|Align.center);

        t2Header.add(secondPlaceLabel);
        t2Header.add(coin2);
        t2.add(t2Header);
        t2.row();

        ImageButton silver = new ImageButton(skin, "star2");
        silver.addListener(secondPlaceListener);
        t2.add(silver).align(Align.center);



        Table t3 = new Table();
        t3.setDebug(devMode);
        Label thirdPlaceLabel = new Label("Third Place", skin, "taunt_small");
        thirdPlaceLabel.setTouchable(Touchable.disabled);
        thirdPlaceLabel.setFontScale(.7f);

        ImageButton coin3 = new ImageButton(skin, "coinSmall8");
        coin3.setTouchable(Touchable.disabled);

        Table t3Header = new Table();
        t3Header.setDebug(devMode);
        t3Header.align(Align.top|Align.center);

        t3Header.add(thirdPlaceLabel);
        t3Header.add(coin3);
        t3.add(t3Header);
        t3.row();


        ImageButton bronze = new ImageButton(skin, "star3");
        bronze.addListener(thirdPlaceListener);
        t3.add(bronze).align(Align.center);

        Table placeTable = new Table();
        placeTable.setDebug(devMode);
        placeTable.align(Align.top|Align.center);
        placeTable.add(t1).width(butWidth);
        placeTable.add(t2).width(butWidth);
        placeTable.add(t3).width(butWidth);


        Table customPlayTable = new Table();
        customPlayTable.align(Align.top|Align.center);
        customPlayTable.setDebug(devMode);

        Label customTitle = new Label("Custom", skin);
        ImageButton coin = new ImageButton(skin, "coinMedium15");

        Table customHeader = new Table();
        customHeader.setDebug(devMode);
        customHeader.align(Align.top|Align.center);

        customHeader.add(customTitle);
        customHeader.add(coin);

        Label inputLabel = new Label("Input Game ID", skin, "button_title");
        textField = new TextField("", skin);
        textField.setAlignment(Align.center);
        ImageTextButton playButton = new ImageTextButton("Play", skin);
        playButton.addListener(playButtonListener);

        customPlayTable.add(customHeader);
        customPlayTable.row();
        customPlayTable.add(inputLabel);
        customPlayTable.row();
        customPlayTable.add(textField).width(viewport.getScreenWidth()/2);
        customPlayTable.row();
        customPlayTable.add(playButton).width(viewport.getScreenWidth()/2);

        storeTable.add(placeTable);
        storeTable.row();
        storeTable.add(customPlayTable);

        scrollPane = new ScrollPane(storeTable, skin, "default");

        bodyTable.add(scrollPane).width(width*.99f).height(height*.755f).padLeft(0).align(Align.top|Align.center);//set the scroll pane size

        table.add(bodyTable).fill().expandX();
        stage.addActor(table);
        stageLoaded = true;
    }
}