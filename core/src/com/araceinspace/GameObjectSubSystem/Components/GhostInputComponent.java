package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventReceiver;
import com.araceinspace.GameObjectSubSystem.GameObject;

/**
 * Created by Isaac Assegai on 7/10/17.
 * When a GhostInputComponent receives an input from the EventDispatcher
 * it will process it normally and then it will NOT record it to it’s InputRecorder.
 * This is because with a GhostInputComponent the Input Recorder will have originated
 * the input event.
 * It will use the EventDispatcher to get the event to the GhostInputComponent.
 * Note, this is different than how a PlayerInputComponent processes Input Events
 */
public class GhostInputComponent extends InputComponent implements EventReceiver{

    /* Field Variables & Objects */
    InputRecorder inputRecorder;

    /* Constructors */
    public GhostInputComponent(){
        inputRecorder = new InputRecorder();
    }

    /* Methods */

    /**
     * Updates the input component
     * @param o
     */
    void update(GameObject o) {
        //TODO write update code
    }

    /**
     * Receives an event from the InputRecorder
     * Proccesses this event with the gameobject, but does
     * not record the event again.
     * @param e
     */
    @Override
    public void receiveEvent(Event e) {
        //TODO write receive Event code
    }
}
