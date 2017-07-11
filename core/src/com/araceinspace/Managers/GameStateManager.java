package com.araceinspace.Managers;

import com.araceinspace.GameWorld;

/**
 * Created by Isaac Assegai on 7/11/17.
 * GameStateManager keeps track of which state the game is in.
 * Which level the game is on, and what menu it should be displaying, etc.
 */
public class GameStateManager {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;
    public enum GAME_STATE {INGAME}
    private GAME_STATE currentState;

    /* Constructors */
    public GameStateManager(GameWorld p){
        parent = p;
        setCurrentState(GAME_STATE.INGAME);

    }

    /* Private Methods */

    /* Public Methods */
    public GAME_STATE getCurrentState(){
        return currentState;
    }

    public void setCurrentState(GAME_STATE state){
        currentState = state;
    }
}
