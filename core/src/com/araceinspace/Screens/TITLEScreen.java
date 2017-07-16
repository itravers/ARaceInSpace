package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by The screen to be rendered when the GAME_STATE is TITLE on 7/16/17.
 */
public class TITLEScreen extends Screen{

    /* Static Variables */

    /* Field Variables & Objects */
    private Skin skin;
    private Table mainTable;
    private Label titleLabel;
    private ClickListener startButtonListener;
    private ClickListener leaderboardButtonListener;
    private TextButton startButton;
    private TextButton leaderboardButton;


    /* Constructors */

    public TITLEScreen(RenderManager p) {
        super(p);
    }



    /* Private Methods */

    private void setupButtons(){
        startButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("StartButtonClicked");
                parent.parent.levelManager.setLevel(1);
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
            }
        };
        leaderboardButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("LeaderBoardButtonClicked");
            }
        };
        startButton = new TextButton("Start Game", skin);
        startButton.addListener(startButtonListener);

        leaderboardButton = new TextButton("LeaderBoards", skin);
        leaderboardButton.addListener(leaderboardButtonListener);
    }

    private void setupTables(){
        mainTable = new Table();
        mainTable.setFillParent(true);

        mainTable.setDebug(parent.parent.devMode);

        mainTable.add(titleLabel);
        mainTable.row();
        mainTable.row();
        mainTable.add(startButton);
        mainTable.add(leaderboardButton);
    }

    private void setupSkin(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin_default.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin_default.json"), atlas);
    }

    private void setupLabels(){
        titleLabel = new Label("A Race In Space", skin);
    }

    /* Public Methods */

    @Override
    public void setup() {
        setupSkin();
        setupButtons();
        setupLabels();
        setupTables();

        stage.addActor(mainTable);
        //Gdx.input.setInputProcessor(stage);
        parent.parent.inputManager.addInputProcessor(stage);
        batch.enableBlending();
    }

    @Override
    public void render(float elapsedTime) {
        mainTable.setDebug(parent.parent.devMode);
        //clear screen
       Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);



        parent.renderBackground(elapsedTime, batch);

        stage.act(Gdx.graphics.getDeltaTime());

        /**
         * These blending lines are needed before stage.draw() to allow the font to blend in
         */
        batch.enableBlending();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        stage.draw();

        /**
         * These Blending lines are needed after stage.draw() to allow the font to blend.
         */
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.disableBlending();
    }

    @Override
    public void dispose() {

    }
}