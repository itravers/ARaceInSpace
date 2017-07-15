package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.Player;
import com.badlogic.gdx.Gdx;
import  com.araceinspace.misc.Animation;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Controls the state of the player based on inputs and former states
 * Animations are based on current state.
 */
public class PlayerStateComponent extends StateComponent{
    /* Static Variables */
    private int WAVE_TIME = 20; //The number of times the state still animation plays before a wave
    private int STANDING_STILL_SIDEWAYS_TIME = 4; //Number of times Stand_Still_Sideways animation plays, before state change
    private float NO_MOVEMENT_SPEED = .001f;
    private float FLYING_DISTANCE = 3.5f;
    private float LANDING_DISTANCE = FLYING_DISTANCE / 2;

    /* Field Variables & Objects */
    PlayerState currentState;
    Player parent;
    float stateTime = 0; //The amount of time we have been in current State
    boolean isLanded = onPlanet();




    /* Constructors */
    public PlayerStateComponent(Player p){
        parent = p;
        setState(PlayerState.STAND_STILL_FORWARD); //The Game Starts, the Avatar is in this state.
    }

    /* Private Methods */

    /**
     * Set the current state to the given state,
     * reset stateTime
     * @param s
     */
    private void setState(PlayerState s){
        currentState = s;
        stateTime = 0;
    }

    /* Public Methods */

    /**
     * Updates the objects current state.
     * @param timeElapsed The time that has elapsed since last update
     */
    @Override
    public void update(float timeElapsed) {
        stateTime += Gdx.graphics.getDeltaTime(); //Update the amount of time we have been in current state
        Animation currentAnimation = parent.getGraphics().currentAnimation; //Used by some transitions

        /* Go through all possible state transitions*/

        //Transition from StandingStillSideways to StandingStillForwards
        if(currentState == PlayerState.STAND_STILL_SIDEWAYS && onPlanet() &&
           parent.getPhysics().getBody().getLinearVelocity().len2() < NO_MOVEMENT_SPEED  &&
           currentAnimation.getLoops(stateTime) >= STANDING_STILL_SIDEWAYS_TIME){
            setState(PlayerState.STAND_STILL_FORWARD);
        }else if(currentState == PlayerState.LAND_FORWARD && parent.isAlive() && isLanded()){
            //Transistion from Land_Forward to Stand_Stil_Forward
            setState(PlayerState.STAND_STILL_FORWARD);
        }else if(currentState == PlayerState.WAVE && currentAnimation.getLoops(stateTime) >= 1){
            //Transition from WAVE to STAND_STILL_FORWARD
            setState(PlayerState.STAND_STILL_FORWARD);
        }else if(currentState == PlayerState.STAND_STILL_FORWARD && currentAnimation.getLoops(stateTime) >= WAVE_TIME){
            //Transition from STAND_STILL_FORWARDS to WAVE
            setState(PlayerState.WAVE);
        }else if(currentState == PlayerState.STAND_STILL_FORWARD && parent.getInput().jumpPressed()){
            //Transistion from STAND_STILL_FORWARDS TO JUMP_FORWARD
            setState(PlayerState.JUMP_FORWARD);
        }else if(currentState == PlayerState.JUMP_FORWARD && parent.getPhysics().getDistanceFromClosestPlanet() >= FLYING_DISTANCE){
            //Transition from JUMP_FORWARD to FLYING
            setState(PlayerState.FLYING);
        }else if(currentState == PlayerState.FLOAT_SIDEWAYS && parent.getPhysics().getDistanceFromClosestPlanet() >= FLYING_DISTANCE &&
                 parent.getInput().thrustPressed()){
            //Transition from FLOAT_SIDEWAYS to FLYING
            setState(PlayerState.FLYING);
        }else if(currentState == PlayerState.FLYING && parent.getPhysics().getDistanceFromClosestPlanet() <= LANDING_DISTANCE &&
                 parent.getPhysics().movingTowardsClosestPlanet()){
            //Transition from flying to landing forward
            setState(PlayerState.LAND_FORWARD);
        }

    }


    /**
     * Examines the current state we are in and decides if that state means we are on
     * a planet, or are we off a planet.
     * @return True if on a planet. False if off a Planet.
     */
    public boolean onPlanet(){
        boolean returnVal = false;
        if(currentState == PlayerState.FLYING && parent.getPhysics().getDistanceFromClosestPlanet() > FLYING_DISTANCE / 2){
            returnVal = false;
        }else{
            returnVal = true;
        }
        return returnVal;
    }

    public boolean isLanded(){
        return isLanded;
    }
}
