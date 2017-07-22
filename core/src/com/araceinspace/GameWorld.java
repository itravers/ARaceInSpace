package com.araceinspace;

import com.araceinspace.InputSubSystem.InputManager;
import com.araceinspace.Managers.AnimationManager;
import com.araceinspace.Managers.ContactListenerManager;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.Managers.SoundManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by ISaac Assegai on 7/11/17.
 * GameWorld holds all the game managers and is
 * the central controller of what happens in the game.
 */
public class GameWorld {
    /* Field Variables & Objects */
    public ApplicationAdapter parent;
    public GameStateManager gameStateManager;
    public AnimationManager animationManager; //Must be constructed before renderManager
    public RenderManager renderManager;
    public LevelManager levelManager;
    public ContactListenerManager contactListenerManager;
    public InputManager inputManager;
    public SoundManager soundManager;
    public World world;
    public float elapsedTime;
    public boolean devMode = false;


    /* Constructors */
    public GameWorld(ApplicationAdapter p){
        parent = p;

        contactListenerManager = new ContactListenerManager(this);//must be before setupphysics
        setupPhysics();
        inputManager = new InputManager(this);


        animationManager = new AnimationManager(this);//must before level manager & before rendermanager
        levelManager = new LevelManager(this);
        renderManager = new RenderManager(this);
        gameStateManager = new GameStateManager(this);//must come after rendermanager

        soundManager = new SoundManager(this);

        elapsedTime = 0;
    }

    /* Private Methods */

    public void setupPhysics(){
        world = new World(new Vector2(0,0), true); //create world
        world.setContactListener(contactListenerManager); //set collision manager
    }

    /* Public Methods */

    private void resetPhysics(){
        setupPhysics();
    }

    public void update(){
        float delta = Gdx.graphics.getDeltaTime();
        //System.out.println("deltaTime: " + delta);

        renderManager.render(elapsedTime);
        levelManager.update(elapsedTime);

        GameStateManager.GAME_STATE currentState = gameStateManager.getCurrentState();

        //we don't want physics to update when not ingame, or titlescreen
        if(currentState == GameStateManager.GAME_STATE.INGAME || currentState == GameStateManager.GAME_STATE.TITLE_SCREEN){
            elapsedTime += delta;
            world.step(1f / 60f, 6, 2);
        }

    }

}
