package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.GhostInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.GhostPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerGraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerState;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.araceinspace.InputSubSystem.Action;
import com.araceinspace.InputSubSystem.KeyAction;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the avatar of the challenger in the game world. This also extends from
 * ThreeDGameObject, but might load a different Model than the player. It has a
 * GhostInputComponent which allows it to receive input from saved inputs taken from
 * a previous match. (See Input SubSystem).
 * The Ghost also uses the PlayerPhysicsComponent as we want it’s physics to interact
 * with the world in EXACTLY the same way the players does.
 */
public class Ghost extends PlayerPrototype{
    public int playtime;

    /**
     * Constructor
     * @param animations
     */
    public Ghost(LevelManager levelManager, PlayerState firstState, Vector2 loc, World world, TextureAtlas.AtlasRegion region, Animation animations, ArrayList<Action> actions) {
        super(levelManager);
        input = new GhostInputComponent(this, actions);
        labelName = input.getInputRecorder().getName();
        graphics = new PlayerGraphicsComponent(this, loc, region, animations);//Graphics Component must be constructed before physics component

        physics = new GhostPhysicsComponent(this, world);
        state = new PlayerStateComponent(this, firstState);
        boolean onPlanet = ((PlayerPhysicsComponent)physics).onPlanet();
        ((PlayerStateComponent)state).isLanded = onPlanet;
        playtime = ((GhostInputComponent)input).getPlayTime();
    }

    public PlayerGraphicsComponent getGraphics(){
        return (PlayerGraphicsComponent)graphics;
    }

    public GhostPhysicsComponent getPhysics(){
        return (GhostPhysicsComponent)physics;
    }

    public GhostInputComponent getInput(){
        return (GhostInputComponent)input;
    }

    public PlayerStateComponent getState(){
        return (PlayerStateComponent)state;
    }

    public void setRotation(float rotation){
        getGraphics().setRotation(rotation);
    }

}
