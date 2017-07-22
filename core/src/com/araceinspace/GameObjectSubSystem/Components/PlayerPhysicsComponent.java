package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
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
    public static float MAX_VELOCITY = 35f;
    public static float CRASH_VELOCITY;
    public static float MAX_ANGULAR_VELOCITY = 20f;
    public static float LINEAR_DAMPENING = .4f;
    public static float ANGULAR_DAMPENING = .5f;
    public static float GRAVITATIONAL_CONSTANT = .08f;

    public static float DENSITY = 0.5f;
    public static float FRICTION = .5f;

    /* Field Variables & Objects */
    public Player parent;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    Fixture fixture;
    PolygonShape shape;
    float torque = 0.0f;
    World world;
    Body body;

    private Vector2 gravityForce;
    private float lastFrameTime = 0; //Used by gravity to calculate time since last frame

    /**
     * Create a new PlayerPhysicsComponent
     */
    public PlayerPhysicsComponent(Player p, World world){
        parent = p;
        setupPhysics(world);
    }

    /* Private Methods */

    private void setupPhysics(World world){
        this.world = world;
        CRASH_VELOCITY = MAX_VELOCITY / 3.25f;
        gravityForce = new Vector2(0,0);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        float PIXELS_TO_METERS = parent.parent.parent.renderManager.PIXELS_TO_METERS;
        bodyDef.position.set((parent.getX() + parent.getWidth() / 2) / PIXELS_TO_METERS,
                             (parent.getY() + parent.getHeight() / 2) / PIXELS_TO_METERS);
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
        Vector2 force = new Vector2(0,0); // The total force of all the planets
        Vector2 preForce = new Vector2(0,0);// The force of a single planet
        float PIXELS_TO_METER = parent.parent.parent.renderManager.PIXELS_TO_METERS;

        for(int i = 0; i < planets.size(); i++){
            Planet p = planets.get(i);
            float g = GRAVITATIONAL_CONSTANT;
            float pMass = p.getMass();
            float sMass = this.getBody().getMass();
            float pRadius = p.getWidth();

           // System.out.println("smass: " + sMass);
            Vector2 pCenter = p.getBody().getPosition();
          // // pCenter = new Vector2(pCenter.x + pRadius/2, pCenter.y +pRadius/2);
           // pCenter = new Vector2(p.getX() + (pRadius/2), p.getY() +(pRadius/2));
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
        this.gravityForce = force.cpy();
        this.getBody().applyForce(force, body.getPosition(), true);

    }

    private void applyMovement(float elapsedTime){
        float deltaTime = Gdx.graphics.getDeltaTime();
        Vector2 impulse;
        Vector2 baseImpulse = new Vector2(-(float)Math.sin(body.getAngle()), (float)Math.cos(body.getAngle())).scl(6f);
        Vector2 pos = body.getPosition();

        //check if boost is pressed and change impulse accordingly
        if(parent.getInput().boostPressed){
            impulse = baseImpulse.scl(2f);
        }else{
            impulse = baseImpulse;
        }

        if(parent.getInput().upPressed){
            body.applyLinearImpulse(impulse, pos, true);
        }

        if(parent.getInput().downPressed){
            impulse = impulse.rotate(180);
            body.applyLinearImpulse(impulse, pos, true);
        }

        if(parent.getInput().leftPressed && onPlanet()){
            body.applyLinearImpulse(impulse.rotate(90).scl(2.5f), pos, true);//side force
            body.applyLinearImpulse(impulse.rotate(180).limit(impulse.len()/1.8f), pos, true);//downforce
        }else  if(parent.getInput().leftPressed){
            body.applyAngularImpulse(3f, true);
        }

        if(parent.getInput().rightPressed && onPlanet()){
            body.applyLinearImpulse(impulse.rotate(-90).scl(2.5f), pos, true);//sideforce
            body.applyLinearImpulse(impulse.rotate(180).limit(impulse.len()/1.8f), pos, true);//downforce
        }else if(parent.getInput().rightPressed){
            body.applyAngularImpulse(-3f, true);
        }




    }




    /* Public Methods */

    /**
     * Examines current state and decides if that means we are on a planet or we are off a planet
     * @return
     */
    public boolean onPlanet(){
        PlayerState state = parent.getState().getCurrentState();
        if((state == PlayerState.FLYING || state == PlayerState.FLOAT_SIDEWAYS) && getDistanceFromClosestPlanet() >= parent.getState().FLYING_DISTANCE / 2){
            return false;
        }else{
            return true;
        }
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
        Vector2 impulse = new Vector2(-(float)Math.sin(body.getAngle()), (float)Math.cos(body.getAngle())).scl(20f);
        //System.out.println("Applying Impulse: " + impulse);
        getBody().applyLinearImpulse(impulse, getBody().getPosition(), true);
    }

    /**
     * Returns the gravity force vector, useful for the gravity force indicator
     * @return
     */
    public Vector2 getGravityForce(){
        return gravityForce;
    }

}
