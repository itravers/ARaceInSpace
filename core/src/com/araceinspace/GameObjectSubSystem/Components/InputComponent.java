package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.InputSubSystem.GameInput;

/**
 * Created by Isaac Assegai on 7/10/17.
 * A GameObject uses an input Component to process its input.
 * The PlayerInputComponent and GhostInputComponent both receive input events from the
 * EventDispatcher.
 * In this game Planets do not receive any inputs so we don’t define any for the planets.
 * I believe we can just use a null field.
 * Both of their input components own their own input recorder.
 * Which is defined in the Input SubSystem section.
 */
public abstract class InputComponent implements Component{
    public boolean boostPressed = false;
    public boolean upPressed = false;
    public boolean leftPressed = false;
    public boolean downPressed = false;
    public boolean rightPressed = false;
    public boolean jumpPressed = false;
    abstract public void update(float timeElapsed);


}
