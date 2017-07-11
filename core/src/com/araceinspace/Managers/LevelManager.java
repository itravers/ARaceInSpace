package com.araceinspace.Managers;

import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameWorld;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Loads and keeps track of the current level
 */
public class LevelManager {

    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;
    private int currentLevel;
    private Player player;

    /* Constructors */
    public LevelManager(GameWorld p){
        System.out.println("LevelManager Constructor");
        parent = p;
    }

    /* Private Methods */
    private void resetPhysics(){
        parent.renderManager.resetFrameNum();

    }

    /* Public Methods */
    public void setCurrentLevel(int level){
        currentLevel = level;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

    /**
     * Sets the level to the designated level.
     * Unload all resources used for the previous level.
     * Load all resources used for the set level
     * @param level The Level we are setting to
     */
    public void setLevel(int level){
        currentLevel = level;
      //  player = new Player(atlas);
    }

    /**
     * Returns the current player object
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Sets the current player object
     * @param p The player to set
     */
    public void setPlayer(Player p){
        player = p;
    }
}
