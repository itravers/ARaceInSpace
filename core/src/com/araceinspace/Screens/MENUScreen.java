package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
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
public class MENUScreen extends Screen {
    /* Static Variables */

    /* Field Variables & Objects */
    private Skin skin;
    private Table mainTable;
    private Label titleLabel;
    private ClickListener backButtonListener;

    private TextButton backButton;

    /* Constructors */

    public MENUScreen(RenderManager parent) {
        super(parent);
    }

    /* Private Methods */

    private void setupSkin(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin_default.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin_default.json"), atlas);

    }

    private void setupButtons(){


        backButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("backButtonListener");
                parent.parent.gameStateManager.setCurrentState(parent.parent.gameStateManager.popState());
            }
        };



        backButton = new TextButton("Back", skin);
        backButton.addListener(backButtonListener);

    }

    private void setupLabels(){
        titleLabel = new Label("Menu", skin);
    }

    private void setupTables(){
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(parent.parent.devMode);

        mainTable.add(titleLabel);
        mainTable.row();


        mainTable.add(backButton);
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
}
