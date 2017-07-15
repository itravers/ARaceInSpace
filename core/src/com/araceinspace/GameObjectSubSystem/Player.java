package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.PlayerGraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.araceinspace.Managers.LevelManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the actual player avatar in the game world. This extends ThreeDGameObject.
 * It has PlayerInputComponent as its input component, which allows it to be controlled by
 * the player and to save all inputs to replay a game. It uses a PlayerPhysicsComponent.
 */
public class Player extends TwoDGameObject{
    /* Static Variables */

    /* Field Variables & Objects */
    public LevelManager parent;
    private float health;

    /**
     * Constructor
     * @param atlas
     * @param animations
     */
    public Player(LevelManager p, Vector2 loc, World world, TextureAtlas atlas, Animation animations) {
        parent = p;
        health = 100;
        graphics = new PlayerGraphicsComponent(loc, atlas, animations);//Graphics Component must be constructed before physics component
        input = new PlayerInputComponent();
        physics = new PlayerPhysicsComponent(this, world);
        state = new PlayerStateComponent(this);


    }

    @Override
    public void update(float elapsedTime) {
        state.update(elapsedTime);
        physics.update(elapsedTime);
    }

    @Override
    void dispose() {
        //TODO create player dispose code
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }

    public PlayerGraphicsComponent getGraphics(){
        return (PlayerGraphicsComponent)graphics;
    }

    public PlayerPhysicsComponent getPhysics(){
        return (PlayerPhysicsComponent)physics;
    }

    public PlayerInputComponent getInput(){
        return (PlayerInputComponent)input;
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

    public void setRotation(float rotation){
        getGraphics().setRotation(rotation);
    }

    public boolean isAlive(){
        if(health > 0){
            return true;
        }else{
            return false;
        }
    }


}
