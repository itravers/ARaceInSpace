package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.FreetypeFontLoader;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/17/17.
 * Lets the user choose which level to play
 */
public class LEVELSELECTScreen extends Screen{

    int xCoords = 0;
    ArrayList<ImageButton> buyButtons;
    Skin skin;
    int spacer;
    ClickListener coinButtonListener;
    ClickListener backButtonListener;
    ClickListener rewardAdButtonListener;
    ClickListener menuButtonListener;

    boolean stageLoaded;

    ImageButton starBronze;
    ImageButton starSilver;
    ImageButton starGold;
    ImageButton buyLevelsButton;


    public LEVELSELECTScreen(RenderManager parent) {
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
        System.out.println("Settingup LevelSelectScreen");

        stage = new Stage(viewport, batch);
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
       // TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        //skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
        skin = parent.parent.resourceManager.getSkin();

        //BitmapFont font = skin.getFont("default-font");
        //font.getData().setScale(.13f, .66f);
        spacer = 25;
        setupPortraitGUI(viewport.getScreenWidth(), viewport.getScreenHeight());
        parent.parent.dialogManager.setupNameDialog(skin, stage, viewport);
        if(parent.parent.playerName == null)parent.parent.dialogManager.nameDialog.show(stage);
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



    private Stack makeButtonStack(float width, float height, String title, String s_taunt1, String s_taunt2){
        height = height - spacer/4;
        boolean devMode = parent.parent.devMode;
        //create buttons to buy iap
        // final Button storeTestButton = new Button(skin, "white");
        //storeTestButton.setColor(Color.WHITE);
        ImageButton levelButton = new ImageButton(skin, "storeButton");
        levelButton.setName(title);
        //levelButton.setWidth(viewport.getScreenWidth()/2);
        levelButton.addListener(parent.parent.inputManager);

        Table buttonTable = new Table();
        buttonTable.setDebug(devMode);
        buttonTable.add(levelButton).size(width, height);
        levelButton.getImageCell().expand().fill();

        //create stuff to put in table button
        Label titleLabel = new Label(title, skin, "optional");
        Label.LabelStyle style = titleLabel.getStyle();
        style.font = parent.parent.resourceManager.Font24;
        titleLabel.setStyle(style);
        titleLabel.setTouchable(Touchable.disabled);
        //titleLabel.setFontScale(.7f); //WORKS for rizing font, but we also change table container

        ImageButton button_image;

        button_image = new ImageButton(skin, "coinButton_small");
        button_image.setTouchable(Touchable.disabled);

        ArrayList<Boolean>stars = getLevelStars(title);

        if(stars.get(0)){
            starBronze = new ImageButton(skin, "starBronze");
        }else{
            starBronze = new ImageButton(skin, "starEmpty");
        }
        starBronze.setTouchable(Touchable.disabled);

        if(stars.get(1)){
            starSilver = new ImageButton(skin, "starSilver");
        }else{
            starSilver = new ImageButton(skin, "starEmpty");
        }
        starSilver.setTouchable(Touchable.disabled);

        if(stars.get(2)){
            starGold = new ImageButton(skin, "starGold");
        }else{
            starGold = new ImageButton(skin, "starEmpty");
        }
        starGold.setTouchable(Touchable.disabled);

        /*starBronze.setSize(width, width);
        starSilver.setSize(width/5, width/5);
        starGold.setSize(width, width);*/

        starBronze.getImage().getDrawable().setMinWidth(width/5);
        starBronze.getImage().getDrawable().setMinHeight(width/5);

        starSilver.getImage().getDrawable().setMinWidth(width/5);
        starSilver.getImage().getDrawable().setMinHeight(width/5);

        starGold.getImage().getDrawable().setMinWidth(width/5);
        starGold.getImage().getDrawable().setMinHeight(width/5);

        Table starTable = new Table();
        starTable.setDebug(devMode);
        starTable.align(Align.center);

        starTable.add(starBronze).padRight(spacer).size(width/5);
        starTable.add(starSilver).size(width/5);;
        starTable.add(starGold).padLeft(spacer).size(width/5);


        Label taunt1 = new Label(s_taunt1, skin, "optional");
        style = taunt1.getStyle();
        style.font = parent.parent.resourceManager.Font20;
        taunt1.setStyle(style);
        taunt1.setTouchable(Touchable.disabled);
        taunt1.setFontScale(.7f);
        //taunt1.setFontScale(Gdx.graphics.getDensity());

        Label taunt2 = new Label(s_taunt2, skin, "error");
        style = taunt2.getStyle();
        style.font = parent.parent.resourceManager.Font24;
        taunt2.setStyle(style);
        taunt2.setTouchable(Touchable.disabled);
        taunt2.setAlignment(Align.top);

        //create table in buttons
        Table purchaseTable = new Table();
        purchaseTable.setDebug(devMode);
        purchaseTable.align(Align.center);

        //Header for purchase table
        Table purchaseHeaderTable = new Table();
        purchaseHeaderTable.setDebug(devMode);
        purchaseHeaderTable.align(Align.top|Align.center);
        purchaseHeaderTable.add(titleLabel).padTop(spacer/4).padRight(spacer/4).align(Align.left);//.size(width/8, stage.getHeight()/20);
        purchaseTable.add(purchaseHeaderTable).align(Align.top);
        purchaseTable.row();

        Table purchaseBodyTable = new Table();
        purchaseBodyTable.setDebug(devMode);
        purchaseBodyTable.align(Align.top|Align.center);
        purchaseBodyTable.add(starTable).expandX().padBottom(spacer/8).align(Align.top);
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt1).expandX().padTop(10).padRight(spacer/2);
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt2).expandX().align(Align.top).padBottom(30);
        purchaseBodyTable.row();

        purchaseTable.add(purchaseBodyTable);


        Stack buttonStack;

        buttonStack = new Stack();
        buttonStack.add(buttonTable);
        buttonStack.add(purchaseTable);
        return buttonStack;
    }



    public void setupPortraitGUI(float width, float height){
        boolean devMode = parent.parent.devMode;
        stageLoaded = false;
        float butWidth = width/2.6f;
        float butHeight = height/4.5f;


        //System.out.println("setup portrait    stage w:h " + width + ":" + height);
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

        //System.out.println("density: portrait, " + Gdx.graphics.getDensity());
        storeTitleLabel = new Label("Choose", skin, "optional");
        Label.LabelStyle style = storeTitleLabel.getStyle();
        style.font = parent.parent.resourceManager.Font48;
        storeTitleLabel.setStyle(style);
        //storeTitleLabel.getStyle().font = bigFont;
        storeTitleLabel.setDebug(devMode);

        String coins = Integer.toString(parent.parent.getCoins());
        coinLabel = new Label(coins, skin, "optional");
        style = coinLabel.getStyle();
        style.font = parent.parent.resourceManager.Font36;
        coinLabel.setStyle(style);
        coinLabel.setAlignment(Align.right);

        coinButton = new ImageButton(skin, "coinButton");
        coinButton.addListener(coinButtonListener);

        headerTable = new Table();
        headerTable.setDebug(devMode);
        headerTable.align(Align.center|Align.top);

        headerTable.add(backButton).padLeft(spacer/4).padTop(0).size(width/8, height/10);
        headerTable.add(menuButton).padLeft(spacer/4).padTop(0).align(Align.left).size(width/8, height/10);
        headerTable.add(storeTitleLabel).expandX().align(Align.left).size(width/3.5f, height/12);


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
        Label taunt1 = new Label(" Your    ", skin, "optional");
        style = taunt1.getStyle();
        style.font = parent.parent.resourceManager.Font24;
        taunt1.setStyle(style);
        extraTable2.add(taunt1).height(height/30).align(Align.left);

        Label taunt2 = new Label(" LEVEL!!!", skin, "optional");
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
        storeTable.align(Align.top|Align.center);
        ArrayList<String>leaderBoardChamps = parent.parent.httpManager.getLevelLeaders();

        Stack buttonStack = makeButtonStack(butWidth, butHeight, "Level 1", "Leaderboard Champ", leaderBoardChamps.get(0));

        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 2",  "Leaderboard Champ", leaderBoardChamps.get(1));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 3",  "Leaderboard Champ", leaderBoardChamps.get(2));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 4",  "Leaderboard Champ", leaderBoardChamps.get(3));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 5",  "Leaderboard Champ", leaderBoardChamps.get(4));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 6",  "Leaderboard Champ", leaderBoardChamps.get(5));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 7",  "Leaderboard Champ", leaderBoardChamps.get(6));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 8",  "Leaderboard Champ", leaderBoardChamps.get(7));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 9",  "Leaderboard Champ", leaderBoardChamps.get(8));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 10",  "Leaderboard Champ", leaderBoardChamps.get(9));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 11", "Leaderboard Champ", leaderBoardChamps.get(10));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        buttonStack = makeButtonStack(butWidth, butHeight, "Level 12", "Leaderboard Champ", leaderBoardChamps.get(11));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);

        scrollPane = new ScrollPane(storeTable, skin, "default");

        ImageButton previousLevelButton = new ImageButton(skin, "previousLevelButton");
        previousLevelButton.setWidth(width*.10f);
        //nextLevelButton.rotateBy(180);

        buyLevelsButton = new ImageButton(skin, "buyLevelsButton");
        buyLevelsButton.setWidth(width*.10f);


        previousLevelButton.setVisible(false);
        buyLevelsButton.setVisible(false);

        bodyTable.add(previousLevelButton).width(width*.10f).align(Align.left);
        bodyTable.add(scrollPane).width(width*.78f).height(height*.755f).padLeft(0).align(Align.top|Align.center);//set the scroll pane size
        bodyTable.add(buyLevelsButton).width(width*.10f).align(Align.right);

        table.add(bodyTable).fill().expandX();
        stage.addActor(table);

        stageLoaded = true;
    }

    /**
     * Returns a 3 member array list of booleans
     * 0 - true means we have completed bronze challenge
     * 1 - true means we have completed silver challenge
     * 2 - true means we have completed gold challenge
     * This will be used to decide if we show the corresponding star, or an empty star in the level
     * select button.
     * @param level
     * @return
     */
    private ArrayList<Boolean>getLevelStars(String level){
        ArrayList<Boolean>returnVal = new ArrayList<Boolean>();
        returnVal.add(parent.parent.prefs.getBoolean("com.araceinspace.Saved_Items."+level+"bronze", false));
        returnVal.add(parent.parent.prefs.getBoolean("com.araceinspace.Saved_Items."+level+"silver", false));
        returnVal.add(parent.parent.prefs.getBoolean("com.araceinspace.Saved_Items."+level+"gold", false));
        return returnVal;
    }
}