package com.araceinspace.Managers;

import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameWorld;
import com.araceinspace.Screens.INGAMEScreen;
import com.araceinspace.Screens.TITLEScreen;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    public GameWorld parent;
    private float baseZoom;
    private float cameraZoom;

    private TITLEScreen titleScreen;
    private INGAMEScreen ingameScreen;

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
        titleScreen = new TITLEScreen(this);
        ingameScreen = new INGAMEScreen(this);
        setupScreenSizeDependantItems();//must come after screens are constructed
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
        ingameScreen.render(timeElapsed);
    }



    public void renderBackground(OrthCamera backgroundCamera, float timeElapsed, SpriteBatch b){
        if(parent.levelManager.mainBackground == null)parent.levelManager.setupBackground();
        parent.levelManager.getBackground().render(backgroundCamera, timeElapsed, b);
    }

    /**
     * Renders the player to the screen
     */
    public void renderPlayer(Player p, float timeElapsed, SpriteBatch batch){
        parent.levelManager.getPlayer().getGraphics().render(timeElapsed, batch);
    }

    public void renderPlanets(ArrayList<Planet> planets, float timeElapsed, SpriteBatch batch){
        for(int i = 0; i < planets.size(); i++){
            planets.get(i).getGraphics().render(timeElapsed, batch);
        }
    }

    private void setFrameNum(int num){
        frameNum = num;
    }

    private void renderTitleScreen(float elapsedTime){
        titleScreen.render(elapsedTime);
    }

    /* Public Methods */

    /**
     * Finds what state the game is currently in and calls the pertinent render method.
     */
    public void render(float elapsedTime){
        //Call appropriate render method based on game state
        if(parent.gameStateManager.getCurrentState() == GameStateManager.GAME_STATE.INGAME){
            renderInGame(elapsedTime);
        }else if(parent.gameStateManager.getCurrentState() == GameStateManager.GAME_STATE.TITLE_SCREEN){
            renderTitleScreen(elapsedTime);
        }

        //Increase the amound of frameNum's we have used (used for ghost recordings)
        frameNum ++;
    }

    public void resetFrameNum(){
        setFrameNum(0);
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
        if(parent.gameStateManager.getCurrentState() == GameStateManager.GAME_STATE.INGAME){
            ingameScreen.setCameraZoom(cameraZoom);
        }else if(parent.gameStateManager.getCurrentState() == GameStateManager.GAME_STATE.TITLE_SCREEN){
            titleScreen.setCameraZoom(cameraZoom);
        }
    }
}
