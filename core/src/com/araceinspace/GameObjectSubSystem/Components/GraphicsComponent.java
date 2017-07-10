package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;

/**
 * Created by Isaac Assegai on 7/10/17.
 * A GameObject uses a GraphicsComponent to actually output itself to the screen.
 * In this game we want to use both 2d and 3d graphics.
 * The planets and indicators and backgrounds and clouds will all be 2d graphics,
 * but the Players and the Ghosts will be 3D graphics(possibly).
 */
public abstract class GraphicsComponent {
    abstract void update(GameObject o);
}
