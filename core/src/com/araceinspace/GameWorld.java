package com.araceinspace;

import com.araceinspace.Managers.AnimationManager;
import com.araceinspace.Managers.ContactListenerManager;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.Managers.RenderManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

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


    /* Constructors */
    public GameWorld(ApplicationAdapter p){
        parent = p;
        contactListenerManager = new ContactListenerManager(this);
        gameStateManager = new GameStateManager(this);
        animationManager = new AnimationManager(this);
        renderManager = new RenderManager(this);
        levelManager = new LevelManager(this);
    }

    /* Private Methods */

    /* Public Methods */

}
