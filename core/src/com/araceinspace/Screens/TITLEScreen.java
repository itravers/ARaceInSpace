package com.araceinspace.Screens;

import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

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
    private SpriteBatch backgroundBatch;
    private OrthCamera backgroundCamera;
    private OrthCamera menuCamera;
    private SpriteBatch menuBatch;


    /* Constructors */

    public TITLEScreen(RenderManager p) {
        super(p);
        parent.parent.levelManager.setLevel(1);
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();;
        batch.dispose();
        backgroundBatch.dispose();
        menuBatch.dispose();
    }

    /* Private Methods */

    private void setupButtons(){
        startButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("StartButtonClicked");
               // parent.parent.levelManager.setLevel(1);
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
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
        backgroundCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch = new SpriteBatch();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);

        menuCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuBatch = new SpriteBatch();
        menuBatch.setProjectionMatrix(menuCamera.combined);

        Viewport menuViewport = new ScreenViewport(menuCamera);

        stage = new Stage(menuViewport, menuBatch);

        setupSkin();
        setupButtons();
        setupLabels();
        setupTables();

        stage.addActor(mainTable);
        parent.parent.inputManager.addInputProcessor(stage);
        menuBatch.enableBlending();
    }

    @Override
    public void render(float elapsedTime) {
        if(monetizationController.isBannerAdShowing()) monetizationController.hideBannerAd();
        mainTable.setDebug(parent.parent.devMode);
        Player p = parent.parent.levelManager.getPlayer();
        ArrayList<Planet> planets = parent.parent.levelManager.getPlanets();
        camera.zoom = parent.getCameraZoom();
        backgroundCamera.zoom = parent.getCameraZoom();
        camera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0);
        camera.setToAngle(p.getPhysics().getBody().getAngle());
        camera.update();

        mainTable.setPosition((p.getX()+p.getWidth()/2)-Gdx.graphics.getWidth()/2, (p.getY()+p.getHeight()/2)-Gdx.graphics.getHeight()/3); //reposition the table

        backgroundCamera.position.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2, 0);
        backgroundCamera.setToAngle(p.getPhysics().getBody().getAngle());
        backgroundCamera.update();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        batch.setProjectionMatrix(camera.combined);

        menuCamera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0);
       // menuCamera.setToAngle(p.getPhysics().getBody().getAngle());
        menuCamera.update();

        //clear screen
       Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);



        parent.renderBackground(backgroundCamera, elapsedTime, backgroundBatch);



        stage.act(Gdx.graphics.getDeltaTime());

        /**
         * These blending lines are needed before stage.draw() to allow the font to blend in
         */
        menuBatch.enableBlending();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.begin();

        parent.renderPlanets(planets, elapsedTime, batch);
        parent.renderPlayer(p, elapsedTime, batch);
        batch.end();
        stage.draw();

        /**
         * These Blending lines are needed after stage.draw() to allow the font to blend.
         */
        Gdx.gl.glDisable(GL20.GL_BLEND);
        menuBatch.disableBlending();
    }



    public void setCameraZoom(float zoom){
        backgroundCamera.zoom = zoom;
        camera.zoom = zoom;
    }
}