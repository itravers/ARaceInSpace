package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/17/17.
 * This is the screen when the player chooses his level
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
    ClickListener nextLevelListener;
    ClickListener previousLevelListener;
    ClickListener buyLevelsListener;

    boolean stageLoaded;

    ImageButton starBronze;
    ImageButton starSilver;
    ImageButton starGold;
    ImageButton buyLevelsButton;
    ImageButton nextLevelsButton;

    public Label taunt2;


    //Reference to Level Manager
    LevelManager lm;


    /**
     * Constructor
     * @param parent The RenderManager that will contain this screen
     */
    public LEVELSELECTScreen(RenderManager parent) {
        super(parent);
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
        lm = parent.parent.levelManager;
        //decide what the current level pack is by what level the player last played
        lm.nextLevelPackUnlocked = isLevelPackUnlocked(lm.currentLevelPack+1);
        System.out.println("Settingup LevelSelectScreen");

        stage = new Stage(viewport, batch);
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.STORE);
            }
        };

        //back button shouldn't do anything on level select screen
        backButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
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

        //Check if the next Level Button has been pressed
        nextLevelListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                lm.currentLevelPack++; //this doesn't work because we set currentLevelPack based on current level at top of file
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
            }
        };

        previousLevelListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                lm.currentLevelPack--; //this doesn't work because we set currentLevelPack based on current level at top of file
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
            }
        };

        //called when the buy levels button is pressed
        buyLevelsListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("Buying level pack: " + (lm.currentLevelPack+1));
                parent.parent.dialogManager.coinsToSpend = 15;
                parent.parent.dialogManager.levelPackToBuy = lm.currentLevelPack+1;
                parent.parent.dialogManager.setupPurchaseDialog(skin, stage, parent.getCurrentScreen());
                parent.parent.dialogManager.purchaseDialog.addSubtitle("Are you sure you want to spend " + parent.parent.dialogManager.coinsToSpend + " coins?");
                parent.parent.dialogManager.purchaseDialog.addSubtitle("To Unlock A New Level Pack?");
                parent.parent.dialogManager.purchaseDialog.show(stage);
            }
        };

        skin = parent.parent.resourceManager.getSkin();

        spacer = 25;
        setupPortraitGUI(viewport.getScreenWidth(), viewport.getScreenHeight());
        parent.parent.dialogManager.setupNameDialog(skin, stage, viewport);
        if(parent.parent.playerName == null){
            parent.parent.dialogManager.nameDialog.show(stage);

        }
        monetizationController.showBannerAd();
    }

    /**
     * Renders the screen
     * @param elapsedTime The time passed since last render
     */
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


    /**
     * Creates a level select button
     * @param width
     * @param height
     * @param title
     * @param s_taunt1
     * @param s_taunt2
     * @return
     */
    private Stack makeButtonStack(float width, float height, String title, String s_taunt1, String s_taunt2){
        height = height - spacer/4;
        boolean devMode = parent.parent.devMode;
        ImageButton levelButton = new ImageButton(skin, "storeButton");
        levelButton.setName(title);
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

        ImageButton button_image;

        button_image = new ImageButton(skin, "coinButton_small");
        button_image.setTouchable(Touchable.disabled);

        ArrayList<Boolean>stars = parent.parent.levelManager.getLevelStars(title);

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

        Label taunt2 = new Label(s_taunt2, skin, "error");
        if(s_taunt2.equals(parent.parent.playerName)) taunt2.setColor(new Color(.275f, .65f, .12f, 1f));
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

    /**
     * Sets up the level select screen in a portrait orientation
     * @param width
     * @param height
     */
    public void setupPortraitGUI(float width, float height){
        boolean devMode = parent.parent.devMode;
        stageLoaded = false;
        float butWidth = width/2.6f;
        float butHeight = height/4.5f;

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

        storeTitleLabel = new Label("Choose", skin, "optional");
        Label.LabelStyle style = storeTitleLabel.getStyle();
        style.font = parent.parent.resourceManager.Font48;
        storeTitleLabel.setStyle(style);
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
        extraTable2.align(Align.left|Align.top);
        Label taunt1 = new Label(" Your LEVEL", skin, "optional");
        style = taunt1.getStyle();
        style.font = parent.parent.resourceManager.Font24;
        taunt1.setStyle(style);
        extraTable2.add(taunt1).height(height/30).padLeft(spacer*3);
        String name = parent.p.playerName;
        taunt2 = new Label(" "+name+"!!!", skin, "optional");
        style = taunt2.getStyle();
        style.font = parent.parent.resourceManager.Font36;
        taunt2.setStyle(style);
        extraTable2.row();
        extraTable2.add(taunt2).height(height/30).align(Align.top).padLeft(spacer*3);

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
        ArrayList<String>leaderBoardChamps = parent.parent.httpManager.getLevelLeaders(lm.currentLevelPack);

        String levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+0);

        boolean levelBeaten = false;
        if(lm.currentLevelPack == 0 || parent.parent.levelManager.getLevelStars(levelString).get(0)){
            levelBeaten = true;
        }
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+1);
        Stack buttonStack = makeButtonStack(butWidth, butHeight, levelString, "Leaderboard Champ", leaderBoardChamps.get(0));
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+2);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(1));
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size

        storeTable.row();

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+3);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(2));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+4);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(3));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        storeTable.row();

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+5);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(4));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+6);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(5));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        storeTable.row();

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+7);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(6));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+8);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(7));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);//sets the button size
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        storeTable.row();

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+9);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(8));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+10);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString,  "Leaderboard Champ", leaderBoardChamps.get(9));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        storeTable.row();

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+11);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString, "Leaderboard Champ", leaderBoardChamps.get(10));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        //check if previous level's bronze star has been beaten, if not, gray this one out
        levelBeaten = parent.parent.levelManager.getLevelStars(levelString).get(0);
        levelString = "Level "+((lm.currentLevelPack*lm.levelPerPack)+12);
        buttonStack = makeButtonStack(butWidth, butHeight, levelString, "Leaderboard Champ", leaderBoardChamps.get(11));
        storeTable.add(buttonStack).pad(0).size(butWidth, butHeight);
        if(!levelBeaten){
            buttonStack.setTouchable(Touchable.disabled);
            SnapshotArray<Actor> actors = buttonStack.getChildren();
            for(int i = 0; i < actors.size; i++){
                actors.get(i).getColor().a = .2f;
            }
        }

        scrollPane = new ScrollPane(storeTable, skin, "default");

        ImageButton previousLevelButton = new ImageButton(skin, "previousLevelButton");
        previousLevelButton.setWidth(width*.10f);
        previousLevelButton.addListener(previousLevelListener);

        buyLevelsButton = new ImageButton(skin, "buyLevelsButton");
        buyLevelsButton.setWidth(width*.10f);
        buyLevelsButton.addListener(buyLevelsListener);

        nextLevelsButton = new ImageButton(skin, "nextLevelButton");
        nextLevelsButton.setWidth(width*.10f);

        nextLevelsButton.addListener(nextLevelListener);

        //Previous level button should not be visible for first level pack
        if(lm.currentLevelPack == 0){
            previousLevelButton.setVisible(false);
        }else{
            previousLevelButton.setVisible(true);
        }


        bodyTable.add(previousLevelButton).width(width*.10f).align(Align.left);
        bodyTable.add(scrollPane).width(width*.78f).height(height*.755f).padLeft(0).align(Align.top|Align.center);//set the scroll pane size

        //If the next level is unlocked we want to display the next button, otherwise display buyLevelsButton
        lm.nextLevelPackUnlocked = isLevelPackUnlocked(lm.currentLevelPack+1);
        if(lm.nextLevelPackUnlocked){
            buyLevelsButton.setVisible(false);
            nextLevelsButton.setVisible(true);
            bodyTable.add(nextLevelsButton).width(width*.10f).align(Align.right);
        }else{
            //check if the next level pack even exists, if it doesn't, we don't want to display buyLevelsButton
            if(parent.parent.httpManager.isLevelPackAvailable(parent.parent.levelManager.currentLevelPack + 1)){
                buyLevelsButton.setVisible(true);
            }else{
                buyLevelsButton.setVisible(false);
            }
            nextLevelsButton.setVisible(false);
            bodyTable.add(buyLevelsButton).width(width*.10f).align(Align.right);
        }




        table.add(bodyTable).fill().expandX();
        stage.addActor(table);

        stageLoaded = true;
    }

    /**
     * Checks the player preferences to see if the given level pack is unlocked
     * @param levelPack
     * @return
     */
    private boolean isLevelPackUnlocked(int levelPack){
        boolean returnVal = true;
        //The first level pack is always unlocked
        if(levelPack == 0){
            returnVal = true;
        }else{
            //check prefs to see if this levelPack is unlocked
            returnVal = parent.parent.prefs.getBoolean("com.araceinspace.levelPackUnlocked."+levelPack);
        }
        return returnVal;
    }
}