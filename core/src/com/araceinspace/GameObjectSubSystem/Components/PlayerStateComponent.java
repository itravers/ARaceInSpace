package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.Managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import  com.araceinspace.misc.Animation;
import com.badlogic.gdx.math.Vector2;

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
    public float FLYING_DISTANCE = 5.5f;
    private float LANDING_DISTANCE = FLYING_DISTANCE / 2;
    private float WALK_SLOW_THRESHOLD = 60f;
    private float RUN_SLOW_THRESHOLD = 120f;
    private float RUN_FAST_THRESHOLD = 200f;

    /* Field Variables & Objects */
    PlayerState currentState;
    PlayerPrototype parent;
    float stateTime = 0; //The amount of time we have been in current State
    public  boolean isLanded = onPlanet();




    /* Constructors */
    public PlayerStateComponent(PlayerPrototype p, PlayerState state){
        parent = p;
        setState(state); //The Game Starts, the Avatar is in this state.
    }

    /* Private Methods */

    /**
     * Set the current state to the given state,
     * reset stateTime
     * @param s
     */
    public void setState(PlayerState s){
        currentState = s;
        stateTime = 0;
        //System.out.println("SetState("+s+")");
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
        float speed = parent.getPhysics().getBody().getLinearVelocity().len2();

        /* Go through all possible state transitions*/

        //Transition from StandingStillSideways to StandingStillForwards
        if(currentState == PlayerState.STAND_STILL_SIDEWAYS && onPlanet() &&
          speed < NO_MOVEMENT_SPEED  &&
           currentAnimation.getLoops(stateTime) >= STANDING_STILL_SIDEWAYS_TIME){
            setState(PlayerState.STAND_STILL_FORWARD);
        }else if(currentState == PlayerState.LAND_FORWARD && parent.isAlive() && isLanded() && currentAnimation.getLoops(stateTime) >= 1){
            //Transistion from Land_Forward to Stand_Stil_Forward
            setState(PlayerState.STAND_STILL_FORWARD);
            parent.parent.checkGoal(parent.getPhysics().getClosestPlanet() ,parent);
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
            parent.getInput().handleCurrentInput();//we want the touchpad to continue having us fly when we change state
            isLanded = false;
        }else if(currentState == PlayerState.FLOAT_SIDEWAYS && parent.getPhysics().getDistanceFromClosestPlanet() >= FLYING_DISTANCE &&
                 parent.getInput().thrustPressed()){
            //Transition from FLOAT_SIDEWAYS to FLYING
            setState(PlayerState.FLYING);
            parent.getInput().handleCurrentInput();//we want the touchpad to continue having us fly when we change state
            isLanded = false;
        }else if(currentState == PlayerState.FLYING && parent.getPhysics().getDistanceFromClosestPlanet() <= LANDING_DISTANCE &&
                 parent.getPhysics().movingTowardsClosestPlanet()){
            //Transition from flying to landing forward
            setState(PlayerState.LAND_FORWARD);
            isLanded = true;
        }else if(currentState == PlayerState.JUMP_SIDEWAYS &&
                 parent.getPhysics().getDistanceFromClosestPlanet() >= FLYING_DISTANCE){
            //Transition from JUMP_SIDEWAYS to FLOAT_SIDEWAYS
            setState(PlayerState.FLOAT_SIDEWAYS);
        }else if(currentState == PlayerState.FLOAT_SIDEWAYS && parent.getPhysics().getDistanceFromClosestPlanet() <= LANDING_DISTANCE &&
                 parent.getPhysics().movingTowardsClosestPlanet()){
            //Transition from FLOAT_SIDEWAYS to LAND_SIDEWAYS
            setState(PlayerState.LAND_SIDEWAYS);
            isLanded = true;
        }else if(currentState == PlayerState.JUMP_SIDEWAYS && currentAnimation.getLoops(stateTime) >= 1 &&
                 parent.getPhysics().getDistanceFromClosestPlanet() <= LANDING_DISTANCE &&
                 parent.getPhysics().movingTowardsClosestPlanet()){
            //Transition from JUMP_SIDEWAYS to LAND_SIDEWAYS
            setState(PlayerState.LAND_SIDEWAYS);
            isLanded = true;
        }else if(currentState == PlayerState.LAND_SIDEWAYS && currentAnimation.getLoops(stateTime) >= 1 && isLanded()){
            //Transition from LAND_SIDEWAYS to STAND_STILL_SIDEWAYS
            setState(PlayerState.STAND_STILL_SIDEWAYS);
            parent.parent.checkGoal(parent.getPhysics().getClosestPlanet() ,parent);
        }else if(currentState == PlayerState.WALK_SLOW && speed < NO_MOVEMENT_SPEED){
            //Transition from WALK_SLOW to STAND_STILL_SIDEWAYS
            setState(PlayerState.STAND_STILL_SIDEWAYS);
        }else if(currentState == PlayerState.STAND_STILL_FORWARD && parent.getInput().walkInput()){
            //Transition from STAND_STILL_SIDEWAYS to WALK_SLOW
            setState(PlayerState.WALK_SLOW);
        }else if(currentState == PlayerState.STAND_STILL_SIDEWAYS && parent.getInput().walkInput()){
            //Transition from STAND_STILL_SIDEWAYS to WALK_SLOW
            setState(PlayerState.WALK_SLOW);
        }else if(currentState == PlayerState.WALK_FAST && speed <= WALK_SLOW_THRESHOLD){
            //Transition from WALK_FAST to WALK_SLOW
            setState(PlayerState.WALK_SLOW);
        }else if(currentState == PlayerState.WALK_SLOW && speed > WALK_SLOW_THRESHOLD){
            //Transition from WALK_SLOW to WALK_FAST
            setState(PlayerState.WALK_FAST);
        }else if(currentState == PlayerState.RUN_SLOW && speed <= RUN_SLOW_THRESHOLD){
            //Transition from RUN_SLOW to WALK_FAST
            setState(PlayerState.WALK_FAST);
        }else if(currentState == PlayerState.WALK_FAST && speed >= RUN_SLOW_THRESHOLD){
            //Transition from WALk_FAST to RUN_SLOW
            setState(PlayerState.RUN_SLOW);
        }else if(currentState == PlayerState.RUN_FAST && speed < RUN_FAST_THRESHOLD){
            //Transition from RUN_FAST to RUN_SLOW
            setState(PlayerState.RUN_SLOW);
        }else if(currentState == PlayerState.RUN_SLOW && speed >= RUN_FAST_THRESHOLD){
            //Transition from RUN_SLOW to RUN_FAST
            setState(PlayerState.RUN_FAST);
        }else if((currentState == PlayerState.WALK_SLOW ||
                  currentState == PlayerState.STAND_STILL_SIDEWAYS ||
                  currentState == PlayerState.WALK_FAST ||
                  currentState == PlayerState.RUN_SLOW ||
                  currentState == PlayerState.RUN_FAST) &&
                 parent.getInput().jumpPressed()){
            //Transition from WALk_SLOW to JUMP_SIDEWAYS
            setState(PlayerState.JUMP_SIDEWAYS);
        }else if((currentState == PlayerState.JUMP_SIDEWAYS || currentState == PlayerState.JUMP_FORWARD) && currentAnimation.getLoops(stateTime) >= 1){
            parent.getPhysics().jumpImpulse();
        }else if(currentState == PlayerState.EXPLODING && currentAnimation.getLoops(stateTime) >=.9f){
            parent.parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.SCOREBOARD);
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

    public PlayerState getCurrentState(){
        return currentState;
    }
}
