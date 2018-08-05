package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.InputSubSystem.GameInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Encapsulates all the data and methods needed to drive Players and Ghosts.
 * Both these types of GameObjects should physically operate EXACTLY the same.
 * This holds the Box2D body.

 */
public class PlayerPhysicsComponent extends PhysicsComponent{
    /* Static Variables */
    public static float MAX_VELOCITY = 40f;
    public static float CRASH_VELOCITY;
    public static float MAX_ANGULAR_VELOCITY = 20f;
    public static float LINEAR_DAMPENING = .4f;
    public static float ANGULAR_DAMPENING = .5f;
    public static float GRAVITATIONAL_CONSTANT = .08f;

    public static float DENSITY = .75f;
    public static float FRICTION = .4f;

    /* Field Variables & Objects */
    public PlayerPrototype parent;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    Fixture fixture;
    PolygonShape shape;
    float torque = 0.0f;
    World world;
    Body body;

    private Vector2 gravityForce;
    private float lastFrameTime = 0; //Used by gravity to calculate time since last frame

    Vector2 force;
    Vector2 preForce;
    Vector2 baseImpulse;
    Vector2 jumpImpulse;

    boolean lastOnPlanet; //set to what the last on planet status was, used to see if onPlanet changes.

    /**
     * Scales the jumpImpulse, gets bigger the faster the player is moving
     */
    float jumpScale = 1; //scales the jump impulse, get



    /**
     * Create a new PlayerPhysicsComponent
     */
    public PlayerPhysicsComponent(PlayerPrototype p, World world){
        parent = p;
        setupPhysics(world);
    }

    /* Private Methods */

    private void setupVectors(){
        force = new Vector2(0,0);
        preForce = new Vector2(0,0);
        gravityForce = new Vector2(0,0);
        baseImpulse = new Vector2(0,0);
        jumpImpulse = new Vector2(0,0);
    }

    private void setupPhysics(World world){
        this.world = world;
        CRASH_VELOCITY = MAX_VELOCITY / 3.25f;
        setupVectors();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        float PIXELS_TO_METERS = parent.parent.parent.renderManager.PIXELS_TO_METERS;
        bodyDef.position.set((parent.getX() + parent.getWidth() / 2) / PIXELS_TO_METERS,
                ((parent.getY() + parent.getHeight() / 2) / PIXELS_TO_METERS)-(0));
        body = world.createBody(bodyDef);
        body.setLinearDamping(LINEAR_DAMPENING);
        body.setAngularDamping(ANGULAR_DAMPENING);
        shape = new PolygonShape();
        shape.setAsBox((parent.getHeight() / 2) / PIXELS_TO_METERS,
                       (parent.getHeight() / 2) / PIXELS_TO_METERS);
        fixtureDef = new FixtureDef();
        fixtureDef.filter.groupIndex = parent.parent.CATEGORY_PLAYER;
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixture = body.createFixture(fixtureDef);
        body.setUserData(this);
        shape.dispose();

    }

    /**
     * Apply Orbital gravity.
     * Loop through each planet and calculate it's gravitimetric force on the player
     * Add all these forces together and apply to player
     * @param elapsedTime
     */
    private void applyGravity(float elapsedTime){
        ArrayList<Planet> planets = parent.parent.parent.levelManager.getPlanets();
        //force = new Vector2(0,0); // The total force of all the planets
        force.set(0,0);
        preForce.set(0,0);
        float PIXELS_TO_METER = parent.parent.parent.renderManager.PIXELS_TO_METERS;

        for(int i = 0; i < planets.size(); i++){
            Planet p = planets.get(i);
            float g = GRAVITATIONAL_CONSTANT;
            float pMass = p.getMass();
            float sMass = this.getBody().getMass();
            float pRadius = p.getWidth();

           // System.out.println("smass: " + sMass);
            Vector2 pCenter = p.getBody().getPosition();
            Vector2 sCenter = this.getBody().getPosition();
            float distanceSQ = sCenter.dst2(pCenter);
            float distance = sCenter.dst(pCenter);


            //calculate single planet force
            preForce.set(0,0);
            preForce = pCenter.sub(sCenter);
            preForce.scl(g * pMass * sMass);
            preForce.set(preForce.x / distanceSQ, preForce.y/distanceSQ);

            //Add the force if the distance between player and planet is less than the gravity radius of the planet
            if(distance * PIXELS_TO_METER * 2 <= p.getGravityRadius()){
                force = force.add(preForce);
            }
        }
        float elapsedTimeInLastFrame = elapsedTime - lastFrameTime;
        lastFrameTime = elapsedTime;

        force = force.scl(elapsedTimeInLastFrame);
        this.gravityForce.set(force.x, force.y);
        //this.gravityForce = force.cpy();
        this.getBody().applyForce(force, body.getPosition(), true);

    }

    private void applyMovement(float elapsedTime){
        PlayerState state = parent.getState().getCurrentState();
        float deltaTime = Gdx.graphics.getDeltaTime();
        float speed = body.getLinearVelocity().len();
        jumpScale = 1;
        Vector2 impulse;
        baseImpulse.set(-(float)Math.sin(body.getAngle()), (float)Math.cos(body.getAngle())).scl(6f);
        //baseImpulse = new Vector2(-(float)Math.sin(body.getAngle()), (float)Math.cos(body.getAngle())).scl(6f);
        Vector2 pos = body.getPosition();

        //check if boost is pressed and change impulse accordingly
        if(parent.getInput().boostPressed && parent.getBoost() > 0){
            impulse = baseImpulse.scl(2f);
            parent.setBoost(parent.getBoost() - deltaTime);
           // System.out.println("boost: " + parent.getBoost());
        }else{
            impulse = baseImpulse;
        }

        /**
         * Only apply up or down impulses if we are in a flying state
         */

        if(state == PlayerState.FLYING){
            if(parent.getInput().upPressed){
                body.applyLinearImpulse(impulse, pos, true);
            }

            if(parent.getInput().downPressed){
                impulse = impulse.rotate(180);
                body.applyLinearImpulse(impulse, pos, true);
            }
        }



        if(parent.getInput().leftPressed && onPlanet()){
            body.applyLinearImpulse(impulse.rotate(90).scl(3.5f), pos, true);//side force
            body.applyLinearImpulse(impulse.rotate(160).limit( speed*.90f), pos, true);//downforce
            jumpScale = speed;
        }else  if(parent.getInput().leftPressed){
            body.applyAngularImpulse(3.5f, true);
        }

        if(parent.getInput().rightPressed && onPlanet()){
            body.applyLinearImpulse(impulse.rotate(-90).scl(3.5f), pos, true);//sideforce
            body.applyLinearImpulse(impulse.rotate(200).limit(speed*.90f), pos, true);//downforce
            jumpScale = speed;
        }else if(parent.getInput().rightPressed){
            body.applyAngularImpulse(-3.5f, true);
        }
    }




    /* Public Methods */

    /**
     * Examines current state and decides if that means we are on a planet or we are off a planet
     * @return
     */
    public boolean onPlanet(){
        boolean returnVal = false;
        GameInput currentInput = parent.getInput().currentInput;
        PlayerState state = parent.getState().getCurrentState();
        if((state == PlayerState.FLYING || state == PlayerState.FLOAT_SIDEWAYS || state == PlayerState.JUMP_FORWARD) && getDistanceFromClosestPlanet() >= parent.getState().FLYING_DISTANCE / 2){
       // if(getDistanceFromClosestPlanet() >= parent.getState().FLYING_DISTANCE / 2){
            returnVal = false;
        }else{
            returnVal = true;
        }

        lastOnPlanet = returnVal;
        return returnVal;
    }

    /**
     * Updates the Players Physics.
     * @param elapsedTime // The time elapsed
     */
    @Override
    public void update(float elapsedTime) {
       // System.out.println("update: " + elapsedTime);
        applyMovement(elapsedTime);
        applyGravity(elapsedTime);
        body.applyTorque(torque, true);
        parent.setRotation((float) Math.toDegrees(body.getAngle()));

        /* We want the position to be set reguardless of the STATE. */
        float PIXELS_TO_METERS = parent.parent.parent.renderManager.PIXELS_TO_METERS;
        parent.getGraphics().setPosition(body.getPosition().x * PIXELS_TO_METERS - parent.getWidth() / 2,
                body.getPosition().y * PIXELS_TO_METERS - parent.getHeight() / 2);
    }


    public Body getBody(){
        return body;
    }

    /**
     * Returns the distance from the closest planet.
     * @return The distance
     */
    public float getDistanceFromClosestPlanet(){
        float distanceToClosestPlanet = 1010101f;
        ArrayList<Planet>planets = parent.parent.parent.levelManager.getPlanets();
        for(int i = 0; i < planets.size(); i++){
            Planet p = planets.get(i);
            float radiusPlanet = p.getBody().getFixtureList().first().getShape().getRadius();
            float dist = p.getBody().getPosition().dst(body.getPosition());
            dist -= radiusPlanet;
            if(dist < distanceToClosestPlanet){
                distanceToClosestPlanet = dist;
            }
        }
        return distanceToClosestPlanet;
    }

    /**
     * Returns the distance from the player to the given planet
     * @param p
     * @return
     */
    public float getDistanceFromPlanet(Planet p){
        float distanceToPlanet = p.getBody().getPosition().dst(body.getPosition());

        return distanceToPlanet;
    }

    /**
     * Returns the closest Planet to the Player
     * @return
     */
    public Planet getClosestPlanet(){
        float distanceToClosestPlanet = 1010101f;
        ArrayList<Planet>planets = parent.parent.parent.levelManager.getPlanets();
        Planet closest = planets.get(0);
        for(int i = 0; i < planets.size(); i++){
            Planet p = planets.get(i);
            float radiusPlanet = p.getBody().getFixtureList().first().getShape().getRadius();
            float dist = p.getBody().getPosition().dst(body.getPosition());
            dist -= radiusPlanet;
            if(dist < distanceToClosestPlanet){
                distanceToClosestPlanet = dist;
                closest = planets.get(i);
            }
        }
        return closest;
    }

    /**
     * Return true if the player is moving towards the closest planet
     * @return
     */
    public boolean movingTowardsClosestPlanet(){
        Planet p = getClosestPlanet();
        if(isMovingTowards(p.getBody().getPosition(), this.getBody().getPosition(), this.getBody().getLinearVelocity())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Returns true if object position and velocity is moving towards testPoint, false if not.
     * @param testPoint
     * @param objectPosition
     * @param objectVelocity
     * @return
     */
    public boolean isMovingTowards(Vector2 testPoint, Vector2 objectPosition, Vector2 objectVelocity){
        Vector2 toPoint = testPoint.sub(objectPosition);
        return toPoint.dot(objectVelocity) > 0;
    }

    /**
     * Makes the player have a jump impulse, should only be called after the jump animation is done.
     */
    public void jumpImpulse(){

        jumpScale = map(jumpScale, 1, MAX_VELOCITY, 1, 2.5f);

        jumpImpulse.set(-(float)Math.sin(body.getAngle()), (float)Math.cos(body.getAngle())).scl(20*jumpScale);
       // System.out.print("JumpImpulse: " + jumpImpulse.len());
       // System.out.println("   JumpScale: " + jumpScale);
       // jumpImpulse = new Vector2(-(float)Math.sin(body.getAngle()), (float)Math.cos(body.getAngle())).scl(20f);
        //System.out.println("Applying Impulse: " + impulse);
        getBody().applyLinearImpulse(jumpImpulse, getBody().getPosition(), true);
    }

    /**
     * Returns the gravity force vector, useful for the gravity force indicator
     * @return
     */
    public Vector2 getGravityForce(){
        return gravityForce;
    }

    public float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

}
