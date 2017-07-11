package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by slack on 7/10/17.
 */
public class RenderManager {
    /* Static Variables */
    static public int frameNum;

    /* Field Variables & Objects */
    GameWorld parent;
    SpriteBatch batch;
    Texture img;
    private float elapsedTime;

    /* Constructor */
    public RenderManager(GameWorld p){
        parent = p;
        frameNum = 0;
        elapsedTime = 0;
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    /* Private Methods */

    /**
     * Finds what level we are on and renders it
     * @param timeElapsed
     */
    private void renderInGame(float timeElapsed){

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
        batch.dispose();
        img.dispose();
    }
}
