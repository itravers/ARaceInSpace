package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
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
        parent = p;
    }

    /* Private Methods */

    /* Public Methods */

    @Override
    public void beginContact(Contact contact) {

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
