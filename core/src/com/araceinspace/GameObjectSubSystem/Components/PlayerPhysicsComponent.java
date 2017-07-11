package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Encapsulates all the data and methods needed to drive Players and Ghosts.
 * Both these types of GameObjects should physically operate EXACTLY the same.
 * This holds the Box2D body.

 */
public class PlayerPhysicsComponent extends PhysicsComponent{

    /* Field Variables & Objects */
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    Fixture fixture;
    PolygonShape shape;
    float torque = 0.0f;
    World world;
    Body body;

    /**
     * Create a new PlayerPhysicsComponent
     */
    public PlayerPhysicsComponent(){
        //TODO Add constructor code.
    }

    /* Methods */

    /**
     * Updates the Players Physics.
     * @param o // The Player Object
     */
    @Override
    public void update(GameObject o) {
        //TODO Add update code for player physics
    }
}
