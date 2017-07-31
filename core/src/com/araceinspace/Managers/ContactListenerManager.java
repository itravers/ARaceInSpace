package com.araceinspace.Managers;

import com.araceinspace.GameObjectSubSystem.Components.PlanetPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerState;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.GameWorld;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Deals with all the collisions.
 */
public class ContactListenerManager implements ContactListener {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;

    /* Constructors */
    public ContactListenerManager(GameWorld p){
        System.out.println("ContactListenerManager Constructor");
        parent = p;
    }

    /* Private Methods */

    /* Public Methods */

    @Override
    public void beginContact(Contact contact) {
       // System.out.println("Begin Contact: " + contact);
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        /*
        Collision happened between planet and player
         */
        if((a instanceof PlanetPhysicsComponent && b instanceof PlayerPhysicsComponent) || b instanceof PlanetPhysicsComponent && a instanceof PlayerPhysicsComponent){
            PlayerPrototype player;
            Planet planet;
            if(a instanceof PlayerPhysicsComponent){
                player = ((PlayerPhysicsComponent)a).parent;
                planet = ((PlanetPhysicsComponent)b).parent;
            }else{
                player = ((PlayerPhysicsComponent)b).parent;
                planet = ((PlanetPhysicsComponent)a).parent;
            }

            //Decide what to do based on players state

            /**
             * We were flying, now we've hit the planet, so we are either landing or crashing.
             * In order to land the player must be faced away from the planet, and must be under the crashing speed.
             */
       // System.out.println(player.getState().getCurrentState());
            if(player.getState().getCurrentState() == PlayerState.FLYING){
                if(didPlayerCrash(player, planet)){
                    player.getState().setState(PlayerState.EXPLODING);
                    parent.levelManager.didFail = true; //set levelManager did fail to true, score screen will check this to display a fail
                }else{
                player.getState().isLanded = true;
                    player.getState().setState(PlayerState.LAND_FORWARD);
                }
            }else if(player.getState().getCurrentState() == PlayerState.FLOAT_SIDEWAYS){
                player.getState().setState(PlayerState.LAND_SIDEWAYS);
            }

        }

    }



    /**
     * Checks to see if the given player - s crashed into the given
     * planet - p during the collision that called this function.
     * If s is facing the opposite direction as p and s' velocity
     * is slow enough
     * @param s The Player that we are checking
     * @param p The Planet that we are checking
     * @return True if the player crashed, false if not.
     */
    private boolean didPlayerCrash(PlayerPrototype s, Planet p){
        boolean returnVal = false;
        Vector2 playerPos = new Vector2(s.getPhysics().getBody().getPosition());
        Vector2 planetPos = new Vector2(p.getBody().getPosition());
        Vector2 planetToPlayer = playerPos.cpy().sub(planetPos);
        Vector2 playerDir = new Vector2(MathUtils.cos(s.getPhysics().getBody().getAngle()), MathUtils.sin(s.getPhysics().getBody().getAngle()));
        playerDir = playerDir.rotate(90);
        float angleDif = planetToPlayer.angle(playerDir);
        angleDif = Math.abs(angleDif);
        if(angleDif >= 45f || 360 - angleDif <= 45){
            //The player didn't land on his feet, so he crashes.
           /* float newHealth = s.getHealth();
            newHealth = newHealth - s.getPhysics().getBody().getLinearVelocity().len()*15;
            s.setHealth(newHealth);
            System.out.println("newhealth: " + newHealth);
            if(s.getHealth() < 0){
                returnVal = true;
            }else{
                returnVal = false;
            }
            */
            return true;

        }else{
            /*the player hit the planet while facing the opposite direction.
              We now need to check if the player was going slow enough */
            float speed = s.getPhysics().getBody().getLinearVelocity().len();
            //if(speed > s.CRASH_VELOCITY){
            float newHealth = s.getHealth();
            newHealth = newHealth - s.getPhysics().getBody().getLinearVelocity().len()*15;
            s.setHealth(newHealth);
            System.out.println("newhealth: " + newHealth);
            if(s.getHealth() < 0){
                returnVal = true;
            }else{
                returnVal = false;
            }
        }
        return returnVal;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
