package com.araceinspace.GameObjectSubSystem.Components;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is an enum owned by the PlayerStateComponent.
 * It’s used to define all the different possible player states.
 */
public enum PlayerState {
    STAND_STILL_FORWARD,
    STAND_STILL_SIDEWAYS,
    WAVE,
    LAND_FORWARD,
    JUMP_FORWARD,
    JUMP_SIDEWAYS,
    FLYING,
    WALK_SLOW,
    WALK_FAST,
    RUN_SLOW,
    RUN_FAST,
    FLOAT_SIDEWAYS,
    LAND_SIDEWAYS
}
