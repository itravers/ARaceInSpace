package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.GhostInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.araceinspace.Managers.LevelManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the avatar of the challenger in the game world. This also extends from
 * ThreeDGameObject, but might load a different Model than the player. It has a
 * GhostInputComponent which allows it to receive input from saved inputs taken from
 * a previous match. (See Input SubSystem).
 * The Ghost also uses the PlayerPhysicsComponent as we want it’s physics to interact
 * with the world in EXACTLY the same way the players does.
 */
public class Ghost extends Player{

    /**
     * Constructor
     * @param atlas
     * @param animations
     */
    public Ghost(LevelManager levelManager, World world, TextureAtlas atlas, Animation animations) {
        super(levelManager, world, atlas, animations);
        input = new GhostInputComponent();
        physics = new PlayerPhysicsComponent((Player)this, world);
        state = new PlayerStateComponent();
    }

    @Override
    public void update(float elapsedTime) {
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
