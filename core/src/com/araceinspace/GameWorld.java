package com.araceinspace;

import com.araceinspace.InputSubSystem.InputManager;
import com.araceinspace.Managers.ConnectionManager;
import com.araceinspace.Managers.DialogManager;
import com.araceinspace.Managers.ResourceManager;
import com.araceinspace.Managers.ContactListenerManager;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.HttpManager;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.Managers.SoundManager;
import com.araceinspace.Screens.LOADINGScreen;
import com.araceinspace.Screens.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by ISaac Assegai on 7/11/17.
 * GameWorld holds all the game managers and is
 * the central controller of what happens in the game.
 */
public class GameWorld {
    /* Static Objects */
    public static float GHOST_TIMER_LIMIT = 60;

    /* Field Variables & Objects */
    public ApplicationAdapter parent;
    public ConnectionManager connectionManager;
    public GameStateManager gameStateManager;
    public ResourceManager resourceManager; //Must be constructed before renderManager
    public RenderManager renderManager;
    public DialogManager dialogManager;
    public LevelManager levelManager;
    public ContactListenerManager contactListenerManager;
    public InputManager inputManager;
    public SoundManager soundManager;
    public World world;
    public float elapsedTime;
    public boolean devMode = false;
    public boolean devMode2 = false;//used for drawing gui elements
    public Preferences prefs;
    private float ghostTimer;
    public boolean countGhostTimer = true;
    private int coins;
    public String playerName;
    public LOADINGScreen loadingScreen;

    /* Constructors */
    public GameWorld(ApplicationAdapter p){
        parent = p;

        //Get Preferences/Locally Saved Items
        prefs = Gdx.app.getPreferences("com.araceinspace.Saved_Items");
        ghostTimer = prefs.getFloat("com.araceinspace.ghostTimer", GHOST_TIMER_LIMIT);
        playerName = prefs.getString("com.araceinspace.playerName", null);
        coins = prefs.getInteger("com.araceinspace.coins");

        //Setup the loading screen, it has no parent
        loadingScreen = new LOADINGScreen(null);

        /**
         * When resourceManager is done loading assets, it will call the
         * initialzeManagers method, which will initialize all the other managers
         * for the game
         */
        resourceManager = new ResourceManager(this);
    }

    /* Private Methods */

    /**
     * Setup the physics systems for the world
     */
    public void setupPhysics(){
        world = new World(new Vector2(0,0), true); //create world
        world.setContactListener(contactListenerManager); //set collision manager
    }

    /* Public Methods */

    /**
     * Start up the managers that control all aspects of the game.
     * *NOTE* Sometimes the order of initialization matters, if so it should be noted
     */
    public void initializeManagers(){
        connectionManager = new ConnectionManager(this);
        contactListenerManager = new ContactListenerManager(this);//must be before setupphysics()
        setupPhysics();
        inputManager = new InputManager(this);
        levelManager = new LevelManager(this);
        renderManager = new RenderManager(this);
        dialogManager = new DialogManager(this);
        soundManager = new SoundManager(this);//must be before gamestateManager
        gameStateManager = new GameStateManager(this);//must come after rendermanager
        elapsedTime = 0; //The game is newly started.
    }

    /**
     * The Game Loop
     * Get Players Input - Calculate The World - Render Everything
     */
    public void update(){
        float delta = Gdx.graphics.getDeltaTime();
        elapsedTime += delta;

        if(resourceManager.loadingAssets){
            resourceManager.update();
            loadingScreen.progressBar.setValue(resourceManager.progress);
            loadingScreen.render(delta);
            return;
        }

        renderManager.render(elapsedTime);
        levelManager.update(elapsedTime);

        GameStateManager.GAME_STATE currentState = gameStateManager.getCurrentState();

        //we don't want physics to update when not ingame, or titlescreen
        if(currentState == GameStateManager.GAME_STATE.INGAME || currentState == GameStateManager.GAME_STATE.TITLE_SCREEN){
            elapsedTime += delta;
            world.step(1f / 60f, 6, 2); //updates the physics
        }

        //Update the ghost timer
        if(countGhostTimer){
            ghostTimer = ghostTimer - delta;
            if(ghostTimer <= 0){
                ghostTimer = 0;
                countGhostTimer = false;
            }
        }

        //Why are we saving the ghost timer again?
        if(countGhostTimer && renderManager.getFrameNum() % 1000 == 0){
            prefs.putFloat("com.araceinspace.ghostTimer", ghostTimer);
            prefs.flush();
        }
    }

    /**
     * Return the amount of seconds until ghost timer resets
     * @return
     */
    public float getGhostTimer(){
        return ghostTimer;
    }

    /**
     * Returns the number of coins the player has
     * @return
     */
    public int getCoins(){
        return coins;
    }

    /**
     * Sets the number of coins the player should have
     * @param c
     */
    public void setCoins(int c){
        coins = c;
        prefs.putInteger("com.araceinspace.coins", coins);
        prefs.flush();
        renderManager.getCurrentScreen().updateCoins();
    }
}
