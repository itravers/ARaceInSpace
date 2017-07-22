package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.PlanetGraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlanetPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerGraphicsComponent;
import com.araceinspace.Managers.LevelManager;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the actual in-game planets that the players will be landing on and taking off from.
 * Uses a PlanetPhysicsComponent as the physics of a planet will be different from the physics
 * of a Player or a Ghost.
 */
public class Planet extends TwoDGameObject{
    /* Static Variables */

    /* Field Variables & Objects */
    public LevelManager parent;


    /**
     * constructor*/
    public Planet(Vector2 loc, TextureAtlas.AtlasRegion region, Animation animations, World world, float size, float gravityRadius, float mass, LevelManager p) {
        parent = p;
        input = null; //Planets don't take inputs

        graphics = new PlanetGraphicsComponent(this, loc, region, animations); //graphics must be constructed before physics
        physics = new PlanetPhysicsComponent(this, world, loc, size, mass, gravityRadius);
        state = null; //Planets don't have different states;

    }


    /* Private Methods */


    /* Public Methods */


    @Override
    public void update(float elapsedTime) {
       physics.update(elapsedTime);
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

    public PlanetGraphicsComponent getGraphics(){
        return (PlanetGraphicsComponent)graphics;
    }

    public PlanetPhysicsComponent getPhysics(){
        return (PlanetPhysicsComponent)physics;
    }

    public float getX(){
        return getGraphics().getX();
    }

    public float getY(){
        return getGraphics().getY();
    }

    public float getMass(){
        return getPhysics().getMass();
    }

    public Body getBody(){
        return getPhysics().getBody();
    }

    public float getGravityRadius(){
        return getPhysics().getGravityRadius();
    }

    public float getWidth(){
        return ((PlanetGraphicsComponent)graphics).getWidth();
    }

    public float getHeight(){
        return ((PlanetGraphicsComponent)graphics).getHeight();
    }
}
