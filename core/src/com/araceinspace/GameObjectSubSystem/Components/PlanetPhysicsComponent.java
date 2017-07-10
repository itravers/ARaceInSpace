package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Encapsulates all the data and methods needed to make a Planet work correctly.
 * In this game the planet doesn’t do much, but it does have a location,
 * size and a density which it’s gravity is calculated through.
 */
public class PlanetPhysicsComponent extends  PhysicsComponent{

    /* Field Variables & Objects */
    World world;
    Body body;

    /**
     * Create a new PlanetPhysicsComponent
     */
    public PlanetPhysicsComponent(){
        //TODO Add constructor code.
    }

    /* Methods */

    /**
     * Updates the Planets Physics.
     * @param o // The Planet Object
     */
    @Override
    void update(GameObject o) {
        //TODO Add update code for player physics
    }
}
