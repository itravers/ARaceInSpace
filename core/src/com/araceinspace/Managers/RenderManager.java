package com.araceinspace.Managers;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameWorld;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.Screens.INGAMEScreen;
import com.araceinspace.Screens.LEADERBOARDScreen;
import com.araceinspace.Screens.LEVELSELECTScreen;
import com.araceinspace.Screens.MENUScreen;
import com.araceinspace.Screens.PREGAMEScreen;
import com.araceinspace.Screens.SCOREScreen;
import com.araceinspace.Screens.STOREScreen;
import com.araceinspace.Screens.Screen;
import com.araceinspace.Screens.TITLEScreen;
import com.araceinspace.misc.Background;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
    public GameWorld p;
    private float baseZoom;
    private float cameraZoom;

    private Screen currentScreen;
    private INGAMEScreen ingameScreen;
    public MonetizationController monetizationController;
    FPSLogger fpsLogger;

   public Dialog purchaseDialog;
    public Dialog notEnoughCoinsDialog;
    public boolean dialogQuestion = false;
   public int coinsToSpend;
   public static enum PLACES {first, second, third};
    public PLACES placeClicked;

    /* Constructor */
    public RenderManager(GameWorld p){
        System.out.println("RenderManager Constructor");
        parent = p;
        this.p = p;
        monetizationController =( (ARaceInSpace)parent.parent).monetizationController;
        frameNum = 0;
        fpsLogger = new FPSLogger();
        setupRendering();

    }

    /* Private Methods */

    /**
     * Sets up the rendering engine with everything it needs to render with.
     */
    private void setupRendering(){
        resetFrameNum();
        monetizationController.loadBannerAd();
        parent.animationManager.setupAnimations();
        //setupBackground();
        currentScreen = new TITLEScreen(this);//default screen when game is loaded
        setupScreenSizeDependantItems();//must come after screens are constructed
    }

    /**
     * Called from game state manager, when a state is set.
     * @param state
     */
    public void loadScreen(GameStateManager.GAME_STATE state){
        switch(state){
            case TITLE_SCREEN:
                currentScreen = new TITLEScreen(this);
                break;
            case LEVEL_SELECT:
                currentScreen = new LEVELSELECTScreen(this);
                break;
            case STORE:
                currentScreen = new STOREScreen(this);
                break;
            case INGAME:
                currentScreen = new INGAMEScreen(this);
                break;
            case MENU:
                currentScreen = new MENUScreen(this);
                break;
            case PREGAME:
                currentScreen = new PREGAMEScreen(this);
                break;
            case SCOREBOARD:
                currentScreen = new SCOREScreen(this);
                break;
            case LEADERBOARDS:
                currentScreen = new LEADERBOARDScreen(this);
                break;

        }
    }

    public void disposeScreen(){
        currentScreen.dispose();
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
        //if(parent.levelManager.mainBackground == null)parent.levelManager.setupBackground();
       // parent.levelManager.getBackground().render(backgroundCamera, timeElapsed, b);
    }

    public void setupBackground() {
        System.out.println("SetupBackground");
        //Texture background = new Texture(Gdx.files.internal("data/tiledBackground.png"));
        Texture starscape1 = new Texture(Gdx.files.internal("data/stars1.png"));
        //Texture starscape2 = new Texture(Gdx.files.internal("data/stars1.png"));
        //Texture starscape3 = new Texture(Gdx.files.internal("data/stars1.png"));
        //background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        //starscape1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
       // starscape2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
       // starscape3.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion[] backgroundLayers = new TextureRegion[4];
        //Texture[] backGroundTextures = new Texture[4];
        //backGroundTextures[0] = background;
        //backGroundTextures[1] = starscape1;
        //backGroundTextures[2] = starscape1;
       // backGroundTextures[3] = starscape1;

        //backgroundLayers[0] = new TextureRegion(background, 0, 0, Gdx.graphics.getHeight()*2, Gdx.graphics.getHeight()*2);
        backgroundLayers[0] = new TextureRegion(starscape1, 0, 0, starscape1.getWidth(), starscape1.getHeight());
       // backgroundLayers[1] = new TextureRegion(starscape1, 0, 0, starscape1.getWidth(), starscape1.getHeight());
       // backgroundLayers[2] = new TextureRegion(starscape1, 0, 0,  starscape1.getWidth(), starscape1.getHeight());
        parent.levelManager.mainBackground = new Background(this, backgroundLayers);
        //mainBackground = new Background(this, backGroundTextures);
    }

    /**
     * Renders the player to the screen
     */
    public void renderPlayer(Player p, float timeElapsed, SpriteBatch batch){

        if(parent.levelManager.getGhost() != null)parent.levelManager.getGhost().getGraphics().render(timeElapsed, batch);
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

    public  int getFrameNum(){
        return frameNum;
    }


    /* Public Methods */

    /**
     * Finds what state the game is currently in and calls the pertinent render method.
     */
    public void render(float elapsedTime){
        currentScreen.render(elapsedTime);
        monetizationController.updateVisibility();//used for banner ads to know whether to show /**  This was causing a new banner ad to show every frame, causing huge garabage collection*/

        //Increase the amound of frameNum's we have used (used for ghost recordings)
        frameNum ++;
       // fpsLogger.log();

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
        currentScreen.setCameraZoom(cameraZoom);
    }

   public float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public Screen getCurrentScreen(){
        return currentScreen;
    }

    public void setupDialogs(Skin skin, final Stage stage, final Screen screen) {
        purchaseDialog = new Dialog("Are you sure you want to spend " + coinsToSpend + " coins?", skin) {
            protected void result(Object object) {
                if (object.toString().equals("true")) {
                    dialogQuestion = true;
                } else {
                    dialogQuestion = false;
                }
                if (dialogQuestion) {
                    if (parent.getCoins() >= coinsToSpend) {
                        if(screen instanceof SCOREScreen){
                            parent.levelManager.setLevel(parent.levelManager.getCurrentLevel());
                        }
                        if (placeClicked == PLACES.first) {
                            parent.levelManager.setChallenge(LevelManager.CHALLENGES.first);
                        } else if (placeClicked == PLACES.second) {
                            parent.levelManager.setChallenge(LevelManager.CHALLENGES.second);
                        } else if (placeClicked == PLACES.third) {
                            parent.levelManager.setChallenge(LevelManager.CHALLENGES.third);
                        } else{
                            parent.levelManager.setChallenge(parent.levelManager.getCurrentChallenge());
                        }
                        parent.setCoins(parent.getCoins() - coinsToSpend);

                        parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.INGAME);
                    } else {
                        notEnoughCoinsDialog.show(stage);
                    }

                    dialogQuestion = false;//reset the boolean
                }
            }
        };
        ImageTextButton yes = new ImageTextButton("YES", skin);
        ImageTextButton no = new ImageTextButton("NO", skin);
        purchaseDialog.button(yes, "true");
        purchaseDialog.button(no, "false");

        notEnoughCoinsDialog = new Dialog("Not Enough Coins", skin);
        ImageTextButton oh = new ImageTextButton("Oh...", skin);
        notEnoughCoinsDialog.button(oh);
    }

}
