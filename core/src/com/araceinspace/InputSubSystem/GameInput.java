package com.araceinspace.InputSubSystem;

/**
 * Created by Isaac Assegai on 7/10/17.
 * A GameInput is just an enum we use to define what input has been pressed.
 * While we are not doing it with this game, we may want to remap input controls in a
 * future game. Using this enum will make it easier to change the input map in the future.
 */
public enum GameInput {
    RIGHT_PRESSED,
    LEFT_PRESSED,
    UP_PRESSED,
    DOWN_PRESSED,
    BOOST_PRESSED,
    RIGHT_RELEASED,
    LEFT_RELEASED,
    UP_RELEASED,
    DOWN_RELEASED,
    BOOST_RELEASED,
    DEV_MODE_TOGGLE,
    JUMP_PRESSED,
    JUMP_RELEASED
}
