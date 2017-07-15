package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Encapsulates all the data and methods needed to make a Planet work correctly.
 * In this game the planet doesn’t do much, but it does have a location,
 * size and a density which it’s gravity is calculated through.
 */
public class PlanetPhysicsComponent extends  PhysicsComponent{
    /* Static Variables */
    public static float DENSITY = 10f;
    public static float FRICTION = .5f;

    /* Field Variables & Objects */
    private World world;
    private Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private CircleShape shape;
    private float mass;
    private float radius;
    public float gravityRadius;
    public Planet parent;

    /**
     * Create a new PlanetPhysicsComponent
     */
    public PlanetPhysicsComponent(Planet p, World w, Vector2 loc, float s, float m, float gravityRadius){
        parent = p;
        world = w;
        radius = s;
        mass = m;
        this.gravityRadius = gravityRadius;
        setupPhysics(world, loc);

    }

    /* Methods */

    private void setupPhysics(World w, Vector2 loc){
        float PIXELS_TO_METERS = parent.parent.parent.renderManager.PIXELS_TO_METERS;
        this.world = w;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; //Planets do not move, so we use static type
        bodyDef.position.set((loc.x + radius / 2)/PIXELS_TO_METERS,
                             (loc.y + radius / 2) / PIXELS_TO_METERS);
        body = world.createBody(bodyDef);
        shape = new CircleShape();
        shape.setRadius((radius / 2) / PIXELS_TO_METERS);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.groupIndex = parent.parent.CATEGORY_PLANET;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixture = body.createFixture(fixtureDef);
        body.setUserData(this);
        shape.dispose();

        //set graphical properties based on physical properties
        parent.getGraphics().setSize(radius, radius);
        parent.getGraphics().setPosition(loc.x, loc.y);
    }


    /**
     * Updates the Planets Physics.
     */
    @Override
    public void update(float timeElapsed) {
        float PIXELS_TO_METERS = parent.parent.parent.renderManager.PIXELS_TO_METERS;
        parent.getGraphics().setPosition(body.getPosition().x * PIXELS_TO_METERS - parent.getGraphics().getWidth() / 2,
                                         body.getPosition().y * PIXELS_TO_METERS - parent.getGraphics().getHeight() / 2);
    }

    public float getMass(){
        return mass;
    }

    public Body getBody(){
        return body;
    }

    public float getGravityRadius(){
        return gravityRadius;
    }
}
