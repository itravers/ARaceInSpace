package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Ghost;
import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.InputSubSystem.Action;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.InputSubSystem.InputRecorder;
import com.araceinspace.Managers.RenderManager;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/10/17.
 * When a GhostInputComponent receives an input from the EventDispatcher
 * it will process it normally and then it will NOT record it to it’s InputRecorder.
 * This is because with a GhostInputComponent the Input Recorder will have originated
 * the input event.
 * It will use the EventDispatcher to get the event to the GhostInputComponent.
 * Note, this is different than how a PlayerInputComponent processes Input Events
 */
public class GhostInputComponent extends PlayerInputComponent implements EventSender{

    /* Field Variables & Objects */
    InputRecorder inputRecorder;
    PlayerPrototype parent;

    /* Constructors */
    public GhostInputComponent(PlayerPrototype p, ArrayList<Action>actions){
        super(p);
        parent = p;
        inputRecorder = new InputRecorder(actions);
    }

    /* Methods */

    /**
     * Updates the input component
     * @param o
     */
    public void update(float timeElapsed) {
        Action nextAction = inputRecorder.getNextAction(RenderManager.frameNum);
        if(nextAction != null){
            GameInput input = nextAction.getInput();
            sendEvent(new Event(Event.TYPE.GHOST_INPUT, "PlayerInput", input));
        }
    }

    @Override
    public void receiveEvent(Event e) {
        System.out.println("ghost inputEvent Received: " + e.getData());
        currentInput = (GameInput)e.getData();
        handleCurrentInput();
    }

    @Override
    public void registerReceiver() {
        EventDispatcher.getSingletonDispatcher().registerReceiver(Event.TYPE.GHOST_INPUT, this);
    }

    public void sendEvent(Event e) {
        EventDispatcher.getSingletonDispatcher().dispatch(e);
    }
}
