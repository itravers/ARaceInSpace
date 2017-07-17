package com.araceinspace.Screens;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

/**
 * Created by The screen to be rendered when the GAME_STATE is INGAME on 7/16/17.
 */
public class INGAMEScreen extends Screen{

    /* Static Variables */
    int spacer = 25;

    /* Field Variables & Objects */
    private OrthCamera backgroundCamera;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private SpriteBatch backgroundBatch;
    private BitmapFont font;
    private ClickListener menuButtonListener;
    private ClickListener rewardButtonListener;
    private Skin skin;
    private OrthCamera menuCamera;

    /* Constructors */

    public INGAMEScreen(RenderManager p) {
        super(p);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundBatch.dispose();
        font.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void setup() {
       // stage = new Stage(menuCamera)
        font = new BitmapFont();
        backgroundCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch = new SpriteBatch();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        debugRenderer = new Box2DDebugRenderer();
        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);
        setupStage();
    }



    /* Private Methods */

    private void setupStage(){
        viewport = new ScreenViewport(menuCamera);
        stage = new Stage(viewport, batch);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

        Table mainTable;
        Table headerTable;
        Table bodyTable;
        ImageButton menuButton;
        ImageButton rewardButton;

        mainTable = new Table();
        mainTable.setDebug(parent.parent.devMode);
        mainTable.setWidth(viewport.getScreenWidth());
        mainTable.align(Align.center | Align.top);
        mainTable.setPosition(0, viewport.getScreenHeight());

        rewardButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("Reward Clicked");
                monetizationController.loadRewardAd();
                monetizationController.showRewardAd();
            }
        };

        menuButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("Menu Clicked");
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.MENU);
            }
        };


        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.setDebug(parent.parent.devMode);
        rewardButton.addListener(rewardButtonListener);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(parent.parent.devMode);
        menuButton.addListener(menuButtonListener);

        headerTable = new Table();
        headerTable.setDebug(parent.parent.devMode);
        headerTable.align(Align.center | Align.top);
        headerTable.add(rewardButton).padLeft(spacer).padTop(0).size(viewport.getScreenWidth()/8, viewport.getScreenHeight()/10);
        headerTable.add(menuButton).padLeft(spacer).padTop(0).align(Align.left).size(viewport.getScreenWidth()/8, viewport.getScreenHeight()/10);
        mainTable.add(headerTable).fill().expandX();
        stage.addActor(mainTable);
        parent.parent.inputManager.addInputProcessor(stage);



    }

    private void renderVersion(SpriteBatch batch){
        font.setColor(Color.WHITE);
        font.draw(batch, "Version: " +((ARaceInSpace)parent.parent.parent).version, parent.parent.levelManager.getPlayer().getX(),parent.parent.levelManager.getPlayer().getY());
    }

    /* Public Methods */

    @Override
    public void render(float timeElapsed) {
        Player p = parent.parent.levelManager.getPlayer();
        ArrayList<Planet> planets = parent.parent.levelManager.getPlanets();
        camera.zoom = parent.getCameraZoom();
        backgroundCamera.zoom = parent.getCameraZoom();
        camera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0);
        camera.setToAngle(p.getPhysics().getBody().getAngle());
        camera.update();

        backgroundCamera.position.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2, 0);
        backgroundCamera.setToAngle(p.getPhysics().getBody().getAngle());
        backgroundCamera.update();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        batch.setProjectionMatrix(camera.combined);

        menuCamera.update();

        //clear screen
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        parent.renderBackground(backgroundCamera, timeElapsed, backgroundBatch);

        batch.begin();
        parent.renderPlayer(p, timeElapsed, batch);
        parent.renderPlanets(planets, timeElapsed, batch);
        if(parent.parent.devMode){
            renderVersion(batch);
        }
        batch.end();
        //stage.act(timeElapsed);
        // stage.draw();

        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);
        if(parent.parent.devMode){
            debugRenderer.render(parent.parent.levelManager.getWorld(), debugMatrix);

        }
        stage.setDebugAll(parent.parent.devMode);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }



    public void setCameraZoom(float zoom){
        super.setCameraZoom(zoom);
        backgroundCamera.zoom = zoom;
    }
}
