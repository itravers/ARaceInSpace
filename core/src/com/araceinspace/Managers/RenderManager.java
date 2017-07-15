package com.araceinspace.Managers;

import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameWorld;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Manages the game rendering
 */
public class RenderManager {
    /* Static Variables */
    static public int frameNum;
    public static final float PIXELS_TO_METERS = 10f;


    /* Field Variables & Objects */
    GameWorld parent;

    private OrthCamera camera;
    private OrthCamera backgroundCamera;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private SpriteBatch batch;
    private SpriteBatch backgroundBatch;
    private float baseZoom;
    private float cameraZoom;

    private Viewport viewport;
    public Stage stage;

    /* Constructor */
    public RenderManager(GameWorld p){
        System.out.println("RenderManager Constructor");
        parent = p;
        frameNum = 0;
        setupRendering();
    }

    /* Private Methods */

    /**
     * Sets up the rendering engine with everything it needs to render with.
     */
    private void setupRendering(){
        resetFrameNum();
        parent.animationManager.setupAnimations();
        camera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        backgroundBatch = new SpriteBatch();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);

        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, batch);


        debugRenderer = new Box2DDebugRenderer();
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

        setupScreenSizeDependantItems();
    }

    //Several things in the game are going to be dependant on the users screen size
    //zoom
    //gui size
    private void setupScreenSizeDependantItems(){
        System.out.println(Gdx.graphics.getWidth());
        if(Gdx.graphics.getWidth() <= 480){
            baseZoom = 1f;
        }else if(Gdx.graphics.getWidth() >= 900){
            baseZoom = .5f;
        }else{
            baseZoom = .75f;
        }
        setCameraZoom(baseZoom);
    }



    /**
     * Finds what level we are on and renders it
     * @param timeElapsed
     */
    private void renderInGame(float timeElapsed){
        Player p = parent.levelManager.getPlayer();
        camera.zoom = cameraZoom;
        backgroundCamera.zoom = cameraZoom;
        camera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0); //this causes a 2nd sprite to be printed???
        camera.setToAngle(p.getPhysics().getBody().getAngle());
        camera.update();

        backgroundCamera.position.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2, 0);
        backgroundCamera.setToAngle(p.getPhysics().getBody().getAngle());
        backgroundCamera.update();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        batch.setProjectionMatrix(camera.combined);

        //clear screen
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderBackground(timeElapsed, backgroundBatch);



        batch.begin();
        renderPlayer(timeElapsed, batch);
        renderPlanets(timeElapsed, batch);
        batch.end();
        //stage.act(timeElapsed);
       // stage.draw();

        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
       if(parent.devMode) debugRenderer.render(parent.levelManager.getWorld(), debugMatrix);
    }

    private void renderBackground(float timeElapsed, SpriteBatch b){
      //  System.out.println("RenderBackground");
        parent.levelManager.getBackground().render(timeElapsed, b);
    }

    /**
     * Renders the player to the screen
     */
    private void renderPlayer(float timeElapsed, SpriteBatch batch){
        parent.levelManager.getPlayer().getGraphics().render(timeElapsed, batch);
    }

    private void renderPlanets(float timeElapsed, SpriteBatch batch){
        ArrayList<Planet> planets = parent.levelManager.getPlanets();
        for(int i = 0; i < planets.size(); i++){
            planets.get(i).getGraphics().render(timeElapsed, batch);
        }
    }

    private void setFrameNum(int num){
        frameNum = num;
    }

    /* Public Methods */

    /**
     * Finds what state the game is currently in and calls the pertinent render method.
     */
    public void render(float elapsedTime){
        //Call appropriate render method based on game state
        if(parent.gameStateManager.getCurrentState() == GameStateManager.GAME_STATE.INGAME){
            renderInGame(elapsedTime);
        }

        //Increase the amound of frameNum's we have used (used for ghost recordings)
        frameNum ++;
    }

    public void resetFrameNum(){
        setFrameNum(0);
    }

    public OrthCamera getBackgroundCamera(){
        return backgroundCamera;
    }

    public float getCameraZoom() {
        return cameraZoom;
    }

    public float getBaseZoom(){
        return baseZoom;
    }

    public void dispose(){

    }

    public void setCameraZoom(float cameraZoom) {
        this.cameraZoom = cameraZoom;
        camera.zoom = cameraZoom;
        backgroundCamera.zoom = cameraZoom;
        // shapeCamera.zoom = cameraZoom;
    }
}
