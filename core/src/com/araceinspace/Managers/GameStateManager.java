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
    public enum GAME_STATE {TITLE_SCREEN, LEVEL_SELECT, STORE, MENU, LEADERBOARDS, PREGAME, SCOREBOARD, INGAME, CREDITS}
    private Stack<GAME_STATE> stateStack; //Used to keep track of what state we are in, and what order we have been in

    /* Constructors */
    public GameStateManager(GameWorld p){
        parent = p;
        stateStack = new Stack<GAME_STATE>();
        parent.levelManager.setLevel(1);
        //setCurrentState(GAME_STATE.TITLE_SCREEN);
        setCurrentState(GAME_STATE.PREGAME);

    }

    /* Private Methods */

    /* Public Methods */
    public GAME_STATE getCurrentState(){
        if(stateStack.size() == 0)return null; //incase getCurrentState is called before a state is set.
        return stateStack.peek();
    }

    public GAME_STATE popState(){
        if(stateStack.size() ==1)return null;
        GAME_STATE stateBefore = stateStack.pop();
        GAME_STATE stateNow;

        stateNow = stateStack.pop();

        return stateNow;
    }

    public void setCurrentState(GAME_STATE state){
        if(getCurrentState() != null){
          //  System.out.println(" currentState: " + getCurrentState().name());
           // System.out.println("    nextState: " + state.name());
        }

        if(getCurrentState() == GAME_STATE.TITLE_SCREEN || getCurrentState() == GAME_STATE.SCOREBOARD){
            parent.elapsedTime = 0;
            stateStack.clear();
        }

        parent.renderManager.disposeScreen();
        parent.renderManager.loadScreen(state);
        stateStack.push(state);
    }

    /**
     * Returns true if we are currently on the first state.
     * We use this so we can tell if we should ignore back button input or not
     * @return
     */
    public boolean firstState(){
        boolean returnVal = false;
        if(stateStack.size() == 1){
            returnVal = true;
        }
        return returnVal;
    }


}
