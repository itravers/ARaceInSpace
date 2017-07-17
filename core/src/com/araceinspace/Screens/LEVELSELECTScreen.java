package com.araceinspace.Screens;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
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

    private TextButton menuButton;
    private TextButton backButton;
    private TextButton storeButton;
    private TextButton levelButton;

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
                parent.parent.levelManager.setLevel(3);

                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);

            }
        };

        menuButton = new TextButton("Menu", skin);
        menuButton.addListener(menuButtonListener);

        backButton = new TextButton("Back", skin);
        backButton.addListener(backButtonListener);

        storeButton = new TextButton("Store", skin);
        storeButton.addListener(storeButtonListener);

        levelButton = new TextButton("Level", skin);
        levelButton.addListener(levelButtonListener);
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
       // Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
       // Gdx.gl.glClearColor(1, 1, 1, 1);
       // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());

        //batch.enableBlending();
       // Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
       // Gdx.gl.glEnable(GL20.GL_BLEND);

        stage.draw();

        //Gdx.gl.glDisable(GL20.GL_BLEND);
       // batch.disableBlending();

        monetizationController.updateVisibility();//used for banner ads to know whether to show
    }

    @Override
    public void dispose() {

    }
}
