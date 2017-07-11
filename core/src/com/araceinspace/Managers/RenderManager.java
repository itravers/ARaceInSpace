package com.araceinspace.Managers;

import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Manages the game rendering
 */
public class RenderManager {
    /* Static Variables */
    static public int frameNum;

    /* Field Variables & Objects */
    GameWorld parent;
    private float elapsedTime;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    /* Constructor */
    public RenderManager(GameWorld p){
        System.out.println("RenderManager Constructor");
        parent = p;
        frameNum = 0;
        elapsedTime = 0;
        setupRendering();

    }

    /* Private Methods */

    /**
     * Sets up the rendering engine with everything it needs to render with.
     */
    private void setupRendering(){
        parent.animationManager.setupAnimations();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    /**
     * Finds what level we are on and renders it
     * @param timeElapsed
     */
    private void renderInGame(float timeElapsed){
        Player p = parent.levelManager.getPlayer();
        batch.setProjectionMatrix(camera.combined);
        camera.position.set(p.getX()+p.getWidth(), p.getY()+p.getHeight(), 0);
        camera.update();

        batch.begin();
        renderPlayer(timeElapsed, batch);
        batch.end();
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
     * Also calculated elapsedTime
     */
    public void render(){
        //First Calculate Elapsed Time
        elapsedTime += Gdx.graphics.getDeltaTime();

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
