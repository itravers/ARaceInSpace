package com.araceinspace.Screens;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Isaac Assegai on 7/16/17.
 * This screen will contain everything needed to implement the Level Select Functionality
 */
public class LEVELSELECTScreen extends Screen {
    /* Static Variables */

    /* Field Variables & Objects */
    private Skin skin;
    private Table mainTable;
    private Label titleLabel;
    private ClickListener menuButtonListener;
    private ClickListener storeButtonListener;
    private ClickListener backButtonListener;
    private ClickListener levelButtonListener;
    private ClickListener levelButtonListener2;
    private ClickListener levelButtonListener3;
    private ClickListener levelButtonListener4;
    private ClickListener levelButtonListener5;
    private ClickListener levelButtonListener6;



    private TextButton menuButton;
    private TextButton backButton;
    private TextButton storeButton;
    private TextButton levelButton;
    private TextButton levelButton2;
    private TextButton levelButton3;
    private TextButton levelButton4;
    private TextButton levelButton5;
    private TextButton levelButton6;

    /* Constructors */

    public LEVELSELECTScreen(RenderManager parent) {
        super(parent);
    }

    /* Private Methods */

    private void setupSkin(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin_default.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin_default.json"), atlas);

    }

    private void setupButtons(){
        menuButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("menuButtonListener");
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.MENU);
            }
        };

        storeButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("storeButtonListener");
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.STORE);
            }
        };

        backButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("backButtonListener");
                parent.parent.gameStateManager.setCurrentState(parent.parent.gameStateManager.popState());
            }
        };

        levelButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("levelButtonListener");
                monetizationController.hideBannerAd();
                parent.parent.levelManager.setLevel(1);

                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.PREGAME);


            }
        };

        levelButtonListener2 = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("levelButtonListener");
                monetizationController.hideBannerAd();
                parent.parent.levelManager.setLevel(2);

                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.PREGAME);


            }
        };

        levelButtonListener3 = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("levelButtonListener");
                monetizationController.hideBannerAd();
                parent.parent.levelManager.setLevel(3);

                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.PREGAME);


            }
        };

        levelButtonListener4 = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("levelButtonListener");
                monetizationController.hideBannerAd();
                parent.parent.levelManager.setLevel(4);

                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.PREGAME);


            }
        };

        levelButtonListener5 = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("levelButtonListener");
                monetizationController.hideBannerAd();
                parent.parent.levelManager.setLevel(5);

                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.PREGAME);


            }
        };

        levelButtonListener6 = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("levelButtonListener");
                monetizationController.hideBannerAd();
                parent.parent.levelManager.setLevel(6);

                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.PREGAME);


            }
        };

        menuButton = new TextButton("Menu", skin);
        menuButton.addListener(menuButtonListener);

        backButton = new TextButton("Back", skin);
        backButton.setName("back");
        backButton.addListener(backButtonListener);


        storeButton = new TextButton("Store", skin);
        storeButton.addListener(storeButtonListener);

        levelButton = new TextButton("Level 1", skin);
        levelButton.addListener(levelButtonListener);

        levelButton2 = new TextButton("Level 2", skin);
        levelButton2.addListener(levelButtonListener2);

        levelButton3 = new TextButton("Level 3", skin);
        levelButton3.addListener(levelButtonListener3);

        levelButton4 = new TextButton("Level 4", skin);
        levelButton4.addListener(levelButtonListener4);

        levelButton5 = new TextButton("Level 5", skin);
        levelButton5.addListener(levelButtonListener5);

        levelButton6 = new TextButton("Level 6", skin);
        levelButton6.addListener(levelButtonListener6);
    }

    private void setupLabels(){
        titleLabel = new Label("Choose Your Level", skin);
    }

    private void setupTables(){
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(parent.parent.devMode);

        mainTable.add(titleLabel);
        mainTable.row();

        mainTable.add(menuButton);
        mainTable.row();

        mainTable.add(backButton);
        mainTable.row();

        mainTable.add(storeButton);
        mainTable.row();

        mainTable.add(levelButton);
        mainTable.row();

        mainTable.add(levelButton2);
        mainTable.row();

        mainTable.add(levelButton3);
        mainTable.row();

        mainTable.add(levelButton4);
        mainTable.row();

        mainTable.add(levelButton5);
        mainTable.row();

        mainTable.add(levelButton6);
        mainTable.row();
    }

    /* Public Methods */

    /**
     * Automatically gets called from extended class.
     */
    @Override
    public void setup() {
        stage = new Stage(viewport, batch);
        setupSkin();
        setupButtons();
        setupLabels();
        setupTables();

        stage.addActor(mainTable);
        parent.parent.inputManager.addInputProcessor(stage);
    }

    /**
     * Gets called from render manager
     * @param elapsedTime
     */
    @Override
    public void render(float elapsedTime) {
        if(monetizationController.isBannerAdLoaded())monetizationController.showBannerAd();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();

    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return null;
    }
}
