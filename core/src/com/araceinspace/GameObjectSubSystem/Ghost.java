package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.GhostInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the avatar of the challenger in the game world. This also extends from
 * ThreeDGameObject, but might load a different Model than the player. It has a
 * GhostInputComponent which allows it to receive input from saved inputs taken from
 * a previous match. (See Input SubSystem).
 * The Ghost also uses the PlayerPhysicsComponent as we want it’s physics to interact
 * with the world in EXACTLY the same way the players does.
 */
public class Ghost extends TwoDGameObject{

    /**
     * Constructor
     * @param atlas
     * @param animations
     */
    public Ghost(TextureAtlas atlas, Animation animations) {
        input = new GhostInputComponent();
        physics = new PlayerPhysicsComponent();
        state = new PlayerStateComponent();
    }

    @Override
    void update() {
        //TODO create ghost update code
    }

    @Override
    void dispose() {
        //TODO create ghost dispose code
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}
