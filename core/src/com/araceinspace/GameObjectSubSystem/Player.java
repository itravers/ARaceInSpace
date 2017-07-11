package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.PlayerGraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the actual player avatar in the game world. This extends ThreeDGameObject.
 * It has PlayerInputComponent as its input component, which allows it to be controlled by
 * the player and to save all inputs to replay a game. It uses a PlayerPhysicsComponent.
 */
public class Player extends TwoDGameObject{

    /**
     * Constructor
     * @param atlas
     * @param animations
     */
    public Player(TextureAtlas atlas, Animation animations) {
        input = new PlayerInputComponent();
        physics = new PlayerPhysicsComponent();
        state = new PlayerStateComponent();
        graphics = new PlayerGraphicsComponent(atlas, animations);
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


}
