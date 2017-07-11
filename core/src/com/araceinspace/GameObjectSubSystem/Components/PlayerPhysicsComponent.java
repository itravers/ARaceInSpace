package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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
    /* Static Variables */
    public static float MAX_VELOCITY = 35f;
    public static float CRASH_VELOCITY;
    public static float MAX_ANGULAR_VELOCITY = 20f;
    public static float LINEAR_DAMPENING = .4f;
    public static float ANGULAR_DAMPENING = .5f;

    public static float DENSITY = 1.25f;
    public static float FRICTION = .5f;

    /* Field Variables & Objects */
    Player parent;
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    Fixture fixture;
    PolygonShape shape;
    float torque = 0.0f;
    World world;
    Body body;
    private Vector2 gravityForce;

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
        shape.setAsBox((parent.getWidth() / 2) / PIXELS_TO_METERS,
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

    private void applyGravity(float elapsedTime){
        //TODO write apply gravity code
    }

    private void applyMovement(float elapsedTime){
        float deltaTime = Gdx.graphics.getDeltaTime();
        Vector2 impulse;
        Vector2 baseImpulse = new Vector2(-(float)Math.sin(body.getAngle()), (float)Math.cos(body.getAngle())).scl(4f);
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

        if(parent.getInput().leftPressed){
            body.applyAngularImpulse(-1f, true);
        }

        if(parent.getInput().rightPressed){
            body.applyAngularImpulse(1f, true);
        }




    }


    /* Public Methods */

    /**
     * Updates the Players Physics.
     * @param elapsedTime // The time elapsed
     */
    @Override
    public void update(float elapsedTime) {
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
}
