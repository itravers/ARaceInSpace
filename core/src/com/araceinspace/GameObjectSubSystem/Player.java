package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.InputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerGraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerState;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.misc.Animation;
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
public class Player extends PlayerPrototype{



    /**
     * Constructor
     * @param region
     * @param animations
     */
    public Player(LevelManager p, PlayerState firstState, Vector2 loc, World world, TextureAtlas.AtlasRegion region, Animation animations) {
       super(p);
        this.startTime = 0;
        labelName = parent.parent.playerName;
        graphics = new PlayerGraphicsComponent(this, loc, region, animations);//Graphics Component must be constructed before physics component
        input = new PlayerInputComponent(this);
        physics = new PlayerPhysicsComponent(this, world, loc);
        state = new PlayerStateComponent(this, firstState);
        boolean onPlanet = ((PlayerPhysicsComponent)physics).onPlanet();
        ((PlayerStateComponent)state).isLanded = onPlanet;
    }

    public PlayerGraphicsComponent getGraphics(){
        return (PlayerGraphicsComponent)graphics;
    }

    public void setRotation(float rotation){
        getGraphics().setRotation(rotation);
    }

    public float getPlayTime(){
       float playTime =  (float)Math.round((endTime-startTime) * 1000f) / 1000f; //round to 3 decimal places
        return playTime;
    }

    public PlayerInputComponent getInput(){
        return ((PlayerInputComponent) input);
    }


}
