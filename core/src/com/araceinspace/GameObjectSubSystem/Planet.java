package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.PlanetPhysicsComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the actual in-game planets that the players will be landing on and taking off from.
 * Uses a PlanetPhysicsComponent as the physics of a planet will be different from the physics
 * of a Player or a Ghost.
 */
public class Planet extends TwoDGameObject{

    /**
     * constructor
     * @param atlas
     * @param animations
     */
    public Planet(TextureAtlas atlas, Animation animations) {
        input = null; //Planets don't take inputs
        physics = new PlanetPhysicsComponent();
        state = null; //Planets don't have different states;
    }

    @Override
    void update() {
        //TODO add Planet update code
    }

    @Override
    void dispose() {
        //TODO add planet dispose code
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}
