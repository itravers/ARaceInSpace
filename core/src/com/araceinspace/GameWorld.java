package com.araceinspace;

import com.araceinspace.Managers.AnimationManager;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.badlogic.gdx.ApplicationAdapter;

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


    /* Constructors */
    public GameWorld(ApplicationAdapter p){
        parent = p;
        gameStateManager = new GameStateManager(this);
        animationManager = new AnimationManager(this);
        renderManager = new RenderManager(this);
    }

    /* Private Methods */

    /* Public Methods */

}
