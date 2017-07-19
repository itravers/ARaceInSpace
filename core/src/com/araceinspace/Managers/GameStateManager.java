package com.araceinspace.Managers;

import com.araceinspace.GameWorld;

import java.util.Stack;

/**
 * Created by Isaac Assegai on 7/11/17.
 * GameStateManager keeps track of which state the game is in.
 * Which level the game is on, and what menu it should be displaying, etc.
 */
public class GameStateManager {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;
    public enum GAME_STATE {TITLE_SCREEN, LEVEL_SELECT, STORE, MENU, LEADERBOARDS, PREGAME, SCOREBOARD, INGAME}
    private Stack<GAME_STATE> stateStack; //Used to keep track of what state we are in, and what order we have been in

    /* Constructors */
    public GameStateManager(GameWorld p){
        parent = p;
        stateStack = new Stack<GAME_STATE>();
        setCurrentState(GAME_STATE.MENU);

    }

    /* Private Methods */

    /* Public Methods */
    public GAME_STATE getCurrentState(){
        return stateStack.peek();
    }

    public GAME_STATE popState(){
        stateStack.pop();
        return stateStack.pop();
    }

    public void setCurrentState(GAME_STATE state){
        parent.renderManager.disposeScreen();
        parent.renderManager.loadScreen(state);
        stateStack.push(state);
    }
}
