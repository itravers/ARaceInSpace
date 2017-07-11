package com.araceinspace.Managers;

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
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private SpriteBatch batch;
    

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

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        debugRenderer = new Box2DDebugRenderer();
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
    }

    /**
     * Finds what level we are on and renders it
     * @param timeElapsed
     */
    private void renderInGame(float timeElapsed){
        Player p = parent.levelManager.getPlayer();
        camera.zoom = .5f;
       // camera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0); //this causes a 2nd sprite to be printed???
        //camera.setToAngle(p.getPhysics().getBody().getAngle());
        camera.update();

        batch.setProjectionMatrix(camera.combined);



        batch.begin();
        renderPlayer(timeElapsed, batch);
        batch.end();

        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
        debugRenderer.render(parent.levelManager.getWorld(), debugMatrix);
    }

    /**
     * Renders the player to the screen
     */
    private void renderPlayer(float timeElapsed, SpriteBatch batch){
        parent.levelManager.getPlayer().getGraphics().render(timeElapsed, batch);
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

    public void dispose(){

    }
}
