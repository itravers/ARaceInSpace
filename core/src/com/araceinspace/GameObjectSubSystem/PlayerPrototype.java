package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.InputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerGraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.araceinspace.GameObjectSubSystem.Components.StateComponent;
import com.araceinspace.Managers.LevelManager;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

/**
 * Created by Isaac Assegai on 7/30/17.
 * this is going to be the shared super class for both player and ghost
 */
public abstract class PlayerPrototype extends TwoDGameObject{
    /* Static Variables */
    public static float BOOST_TOTAL = 1000;
    public static float HEALTH_TOTAL = 100;

    /* Field Variables & Objects */
    public LevelManager parent;
    private float health;
    private float boost;
    private boolean updateable = true;
    public float startTime;
    public float endTime;

    public PlayerPrototype(LevelManager p){
        parent = p;
        health = HEALTH_TOTAL;
        boost = BOOST_TOTAL;
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void update(float elapsedTime) {
        if(updateable){
            state.update(elapsedTime);
            graphics.update(elapsedTime);
            physics.update(elapsedTime);
            input.update(elapsedTime);
        }

    }

    @Override
    void dispose() {
        //TODO create player dispose code
    }

    public float getHealth(){
        return health;
    }

    public void setHealth(float health){
        this.health = health;
    }

    public void setBoost(float boost){
        if(boost <= 0)boost = 0;
        this.boost = boost;
    }

    public float getBoost(){
        return boost;
    }

    public boolean isAlive(){
        if(health > 0){
            return true;
        }else{
            return false;
        }
    }

    public float getX(){
        return ((PlayerGraphicsComponent)graphics).getX();
    }

    public float getY(){
        return ((PlayerGraphicsComponent)graphics).getY();
    }

    public float getWidth(){
        return ((PlayerGraphicsComponent)graphics).getWidth();
    }

    public float getHeight(){
        return ((PlayerGraphicsComponent)graphics).getHeight();
    }

    public abstract void setRotation(float rotation);

    public abstract PlayerGraphicsComponent getGraphics();

    public PlayerPhysicsComponent getPhysics(){
        return (PlayerPhysicsComponent)physics;
    }

    public InputComponent getInput(){
        return (InputComponent)input;
    }

    public PlayerStateComponent getState(){
        return (PlayerStateComponent)state;
    }

    public void setUpdateable(boolean u){
        updateable = u;
    }


}
