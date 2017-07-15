package com.araceinspace;

import com.araceinspace.InputSubSystem.InputManager;
import com.araceinspace.Managers.AnimationManager;
import com.araceinspace.Managers.ContactListenerManager;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.Managers.RenderManager;
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
    public World world;
    float elapsedTime;
    public boolean devMode = false;


    /* Constructors */
    public GameWorld(ApplicationAdapter p){
        parent = p;
        contactListenerManager = new ContactListenerManager(this);//must be before setupphysics
        setupPhysics();
        inputManager = new InputManager(this);

        gameStateManager = new GameStateManager(this);
        animationManager = new AnimationManager(this);

        renderManager = new RenderManager(this);
        levelManager = new LevelManager(this);
        elapsedTime = 0;
    }

    /* Private Methods */

    private void setupPhysics(){
        world = new World(new Vector2(0,0), true); //create world
        world.setContactListener(contactListenerManager); //set collision manager
    }

    /* Public Methods */

    private void resetPhysics(){
        setupPhysics();
    }

    public void update(){
        elapsedTime += Gdx.graphics.getDeltaTime();
        renderManager.render(elapsedTime);
        levelManager.update(elapsedTime);
        world.step(1f / 60f, 6, 2);
    }

}
