package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.PlayerGraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.araceinspace.Managers.LevelManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.physics.box2d.World;

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

    /**
     * Constructor
     * @param atlas
     * @param animations
     */
    public Player(LevelManager p, World world, TextureAtlas atlas, Animation animations) {
        parent = p;
        graphics = new PlayerGraphicsComponent(atlas, animations);//Graphics Component must be constructed before physics component
        input = new PlayerInputComponent();
        physics = new PlayerPhysicsComponent(this, world);
        state = new PlayerStateComponent();

    }

    @Override
    void update() {
        //TODO create player update code
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


}
