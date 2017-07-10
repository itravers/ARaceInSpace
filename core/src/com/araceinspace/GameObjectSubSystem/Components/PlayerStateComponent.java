package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;

/**
 * Created by Isaac Assegai on 7/10/17.
 */
public class PlayerStateComponent extends StateComponent{

    /* Field Variables & Objects */
    PlayerState currentState;


    /* Constructors */
    public PlayerStateComponent(){
        currentState = PlayerState.STAND_STILL_FORWARD;
    }

    /**
     * Updates the objects current state.
     * @param o
     */
    @Override
    void update(GameObject o) {
        //TODO make update code
    }
}
