package com.araceinspace.InputSubSystem;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 7/10/17.
 * In reality completely duplicating a play behaviour exactly is actually quite hard.
 * The physics simulation needs to be completely deterministic, no matter what is going on,
 * what time it is, what machine you are playing on. Everything needs to be repeated EXACTLY.
 * This doesn’t work. So instead of relying on complete determinism, we are going to try to keep
 * things close, but if they get out of whack, we will be able to reset them using a KeyAction.
 * A KeyAction will only be generated once every X amount of Actions.

 */
public class KeyAction extends Action{

    /* Field Variables */
    private Vector2 position, velocity;
    private float angle, angularVelocity;

    /* Constructors */
    public KeyAction(int timeStamp, GameInput input, Vector2 position, Vector2 velocity, float angle, float angularVelocity){
        super(timeStamp, input);
        this.position = position;
        this.velocity = velocity;
        this.angle = angle;
        this.angularVelocity = angularVelocity;
    }

    public KeyAction(int timeStamp, GameInput input) {
        super(timeStamp, input);
    }

    /* Methods */
    public Vector2 getPosition(){
        return position;
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public float getAngle(){
        return angle;
    }

    public float getAngularVelocity(){
        return angularVelocity;
    }
}