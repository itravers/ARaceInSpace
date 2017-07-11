package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Each GameObject works as a state machine.
 * The StateComponent IS the state machine part of a GameObject.
 * The StateComponent completely encapsulates the data and the transitions
 * associated with every state. Other Components may query the GameObject for it’s state.
 * The GameObject will return it’s state from the StateComponent.

 */
public abstract class StateComponent implements Component{
    abstract public void update(GameObject o);
}
