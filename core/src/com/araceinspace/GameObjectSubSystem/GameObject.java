package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.GraphicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.InputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.StateComponent;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the base abstract object class.
 * All implemented game objects will be descendants of this class.
 * To be a GameObject an object will need input, physics, graphics and state components.
 * A game object will need update, create and dispose methods.

 */
public abstract class GameObject {
    /* Field Variables & Objects */
    InputComponent input;
    PhysicsComponent physics;
    GraphicsComponent graphics;
    StateComponent state;

    /* Abstract Methods */
    abstract void update();
    abstract void create();
    abstract void dispose();
}
