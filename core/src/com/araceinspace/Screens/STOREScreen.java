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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/17/17.
 * Lets the user buy items from the in-app purchase store.
 */
public class STOREScreen extends Screen{

    int xCoords = 0;
    ArrayList<ImageButton> buyButtons;
    Skin skin;
    int spacer;
    ClickListener coinButtonListener;
    ClickListener backButtonListener;
    ClickListener rewardAdButtonListener;
    ClickListener menuButtonListener;
    ClickListener buy15Listener;
    ClickListener buy30Listener;
    ClickListener buy100Listener;
    ClickListener buy5000Listener;
    ClickListener removeAdsListener;
    ClickListener unlockEverythingListener;

    Stage landscapeStage;
    boolean stageLoaded;

    public STOREScreen(RenderManager parent) {
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
        System.out.println("Settingup Store");
        stage = new Stage(viewport, batch);
        // updateOrientation(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                System.out.println("Button Clicked: " + event);
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

        buy15Listener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.buyItem("buy_15_coins");
            }

        };

        buy30Listener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.buyItem("buy_30_coins");
            }

        };

        buy100Listener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.buyItem("buy_100_coins");
            }

        };

        buy5000Listener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.buyItem("buy_5000_coins");
            }

        };

        removeAdsListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.buyItem("remove_ads");
            }

        };

        unlockEverythingListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.buyItem("unlock_everything");
            }

        };


        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
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



            // guiStage = new Stage(viewport, batch);
            // System.out.println("drawing portrait");
           // Gdx.input.setInputProcessor(stage);
        parent.parent.inputManager.addInputProcessor(stage);
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();


    }



    public void setupPortraitGUI(float width, float height){
        stageLoaded = false;
        float butWidth = width/2.4f;
        float butHeight = height/3.955f;


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
        table.setDebug(false);
        table.setWidth(width);
        table.align(Align.center|Align.top);
        table.setPosition(0, height);


        backButton = new ImageButton(skin, "backButton");
        backButton.setDebug(false);
        backButton.addListener(backButtonListener);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(false);
        menuButton.addListener(menuButtonListener);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.addListener(coinButtonListener);
        rewardButton.setDebug(false);
        rewardButton.addListener(rewardAdButtonListener);

        System.out.println("density: portrait, " + Gdx.graphics.getDensity());
        storeTitleLabel = new Label("STORE", skin, "Store_Title");
        storeTitleLabel.setDebug(false);
        String coins = Integer.toString(parent.parent.getCoins());
        coinLabel = new Label(coins, skin, "coinLabel");
        coinLabel.setAlignment(Align.right);

        coinButton = new ImageButton(skin, "coinButton");

        coinButton.addListener(coinButtonListener);

        headerTable = new Table();
        headerTable.setDebug(false);
        headerTable.align(Align.center|Align.top);

        headerTable.add(backButton).padLeft(spacer/4).padTop(0).size(width/8, height/10);
        headerTable.add(menuButton).padLeft(spacer/4).padTop(0).align(Align.left).size(width/8, height/10);
        headerTable.add(storeTitleLabel).expandX().align(Align.left).size(width/3.5f, height/12);

        float fontWidth = storeTitleLabel.getStyle().font.getSpaceWidth()*storeTitleLabel.getText().length();

        headerTable.add(coinLabel).size(width/6, height/12).align(Align.right);
        headerTable.add(coinButton).size(width/8, height/10).padTop(0).padRight(spacer);

        headerTable.row();
        //headerTable.add(rewardButton).size(width/8, height/12).padLeft(spacer/1).padTop(spacer/4).align(Align.top).spaceLeft(0);

        Table extraTable = new Table();
        extraTable.setDebug(false);
        extraTable.align(Align.center|Align.top);
        extraTable.add(rewardButton).size(width/8, height/12).padLeft(spacer/1).padTop(spacer/4).align(Align.top).spaceLeft(0);

        Table extraTable2 = new Table();
        extraTable2.setDebug(false);
        extraTable2.align(Align.center|Align.top);
        Label taunt1 = new Label("DO YOURSELF A FAVOR AND", skin, "extra_small");
        extraTable2.add(taunt1).height(height/30).align(Align.left);

        Label taunt2 = new Label("SPLURGE!!!", skin, "coinLabel");
        extraTable2.row();
        extraTable2.add(taunt2).height(height/30).align(Align.top);

        extraTable.add(extraTable2).fill().expandX();



        table.add(headerTable).fill().expandX();
        table.row();
        table.add(extraTable).fill().expandX();
        table.row();

        bodyTable = new Table();
        bodyTable.setDebug(false);

        bodyTable.align(Align.center); //aligns reward video with back button

        buyButtons = new ArrayList<ImageButton>();

        storeTable = new Table();
        storeTable.setDebug(false);
        storeTable.align(Align.top|Align.center);

        Stack buttonStack = makeButtonStack(butWidth, butHeight, "Buy 15", "$0.99", "Like Winning A Challenge", "You", "NO TALENT BUM!!!", true, buy15Listener);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        buttonStack = makeButtonStack(butWidth, butHeight, "Buy 30", "$1.50", "You Can Compete", "In 3", "Leaderboard Challenges", true, buy30Listener);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Buy 100", "$2.99", "More than 3 times the", "Coins! - Less Than", "Twice the Cost!!!", true, buy100Listener);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Buy 5000", "$9.99", "Maybe you can actually", "Win A Challenge", "HUH???", true, buy5000Listener);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Remove Ads", "$1.99", "Turn Those", "SUCKERS", "OFF!!!", false, removeAdsListener);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Unlock Everything", "$29.99", "Everything is YOURS", "No More Ads!!!", "Unlimited Coins!!!", false, unlockEverythingListener);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
/*
        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Buy 15", "$0.99", "Like Winning A Challenge", "You", "NO TALENT BUM!!!", true);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        buttonStack = makeButtonStack(butWidth, butHeight, "Buy 30", "$1.50", "You Can Compete", "In 3", "Leaderboard Challenges", true);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Buy 100", "$2.99", "More than 3 times the", "Coins! - Less Than", "Twice the Cost!!!", true);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Buy 5000", "$9.99", "Maybe you can actually", "Win A Challenge", "HUH???", true);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Remove Ads", "$1.99", "Turn Those", "SUCKERS", "OFF!!!", false);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Unlock Everything", "$29.99", "Everything is YOURS", "No More Ads!!!", "Unlimited Coins!!!", false);
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
*/
        scrollPane = new ScrollPane(storeTable, skin, "default");

        bodyTable.add(scrollPane).width(width*.95f).height(height*.755f).padLeft(0).align(Align.top|Align.center);//set the scroll pane size


        table.add(bodyTable).fill().expandX();
       // table.setPosition(camera.position.x, camera.position.y);
        stage.addActor(table);

        stageLoaded = true;
    }



    private Stack makeButtonStack(float width, float height, String title, String price, String s_taunt1, String s_taunt2, String s_taunt3, boolean showImage, ClickListener listener){
        height = height - spacer/4;
       // width = width + spacer/4;
        boolean devMode = parent.parent.devMode;

        //create buttons to buy iap
        // final Button storeTestButton = new Button(skin, "white");
        //storeTestButton.setColor(Color.WHITE);
        ImageButton storeButton = new ImageButton(skin, "storeButton");
        storeButton.addListener(listener);

        Table buttonTable = new Table();
        buttonTable.setDebug(devMode);
        buttonTable.add(storeButton).size(width, height);
        storeButton.getImageCell().expand().fill();

        //create stuff to put in table button
        Label titleLabel = new Label(title, skin, "button_title");
        titleLabel.setTouchable(Touchable.disabled);
        titleLabel.setFontScale(.7f); //WORKS for rizing font, but we also change table container

        ImageButton button_image;

        button_image = new ImageButton(skin, "coinButton_small");
        button_image.setTouchable(Touchable.disabled);



        Label priceLabel = new Label(price, skin, "button_title");
        priceLabel.setTouchable(Touchable.disabled);
        priceLabel.setFontScale(.8f);
        //priceLabel.setFontScale(Gdx.graphics.getDensity()); //WORKS for rizing font

        Label taunt1 = new Label(s_taunt1, skin, "taunt_small");
        taunt1.setTouchable(Touchable.disabled);
        taunt1.setFontScale(.7f);
        //taunt1.setFontScale(Gdx.graphics.getDensity());

        Label taunt2 = new Label(s_taunt2, skin, "taunt_small");
        taunt2.setTouchable(Touchable.disabled);
        taunt2.setFontScale(.7f);
        //taunt2.setFontScale(Gdx.graphics.getDensity());

        Label taunt3 = new Label(s_taunt3, skin, "taunt_small");
        taunt3.setTouchable(Touchable.disabled);
        taunt3.setFontScale(.7f);

        //create table in buttons
        Table purchaseTable = new Table();
        purchaseTable.setDebug(false);
        purchaseTable.align(Align.center);

        //Header for purchase table
        Table purchaseHeaderTable = new Table();
        purchaseHeaderTable.setDebug(false);
        purchaseHeaderTable.align(Align.top|Align.center);
        purchaseHeaderTable.add(titleLabel).padTop(spacer/4).padRight(spacer/4).align(Align.left);//.size(width/8, stage.getHeight()/20);
        if(showImage)purchaseHeaderTable.add(button_image).padTop(0).size(width/4, width/4).align(Align.top);
        purchaseTable.add(purchaseHeaderTable).align(Align.top);
        purchaseTable.row();

        Table purchaseBodyTable = new Table();
        purchaseBodyTable.setDebug(false);
        purchaseBodyTable.align(Align.top|Align.center);
        purchaseBodyTable.add(priceLabel).expandX().padBottom(spacer/8).align(Align.top);
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt1).expandX();
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt2).expandX();
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt3).expandX().padBottom(spacer/2);

        purchaseTable.add(purchaseBodyTable);


        Stack buttonStack;
        buttonStack = new Stack();
        buttonStack.add(buttonTable);
        buttonStack.add(purchaseTable);
        return buttonStack;
    }


}
