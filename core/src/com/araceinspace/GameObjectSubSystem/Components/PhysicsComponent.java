package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;

/**
 * Created by Isaac Assegai on 7/10/17.
 * A physics component encapsulates all the data and methods needed for a
 * GameObject to update its physical properties.
 * This is where the player locomotion and forces are calculated.
 * Everything excluding collision physics should be located here.
 */
public abstract class PhysicsComponent implements Component{
    public abstract void update(GameObject o);
}
