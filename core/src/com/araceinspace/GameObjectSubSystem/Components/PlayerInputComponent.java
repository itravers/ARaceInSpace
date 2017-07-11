package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventReceiver;
import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.InputSubSystem.InputRecorder;

/**
 * Created by Isaac Assegai on 7/10/17.
 * When a PlayerINputComponent receives an input from the EventDispatcher
 * it will process it normally and then it WILL record it to it’s InputRecorder.
 * This is because with a PlayerInputComponent the input will
 * have originated with the main games InputManager.
 * This means the Player's input is coming from the actual player.
 */
public class PlayerInputComponent extends InputComponent implements EventReceiver{

    /* Field Variables & Objects */
    InputRecorder inputRecorder;

    /* Constructors */
    public PlayerInputComponent(){
        registerReceiver();
        inputRecorder = new InputRecorder();
    }

    /* Methods */

    /**
     * Updates the input component
     * @param timeElapsed
     */
    public void update(float timeElapsed) {
        //TODO write update code
    }

    /**
     * Receives keyboard event from the InputSubSystem
     * This event should be processed by the Player Object, as well
     * as be stored in the InputRecorder
     * @param e
     */
    @Override
    public void receiveEvent(Event e) {
        String id = e.getId();
        Event.TYPE type = e.getType();
        GameInput currentInput = (GameInput)e.getData();

        switch(currentInput){
            case BOOST_PRESSED:
                    boostPressed = true;
                break;
            case RIGHT_PRESSED:
                rightPressed = true;
                break;
            case LEFT_PRESSED:
                leftPressed = true;
                break;
            case UP_PRESSED:
                upPressed = true;
                break;
            case DOWN_PRESSED:
                downPressed = true;
                break;
            case RIGHT_RELEASED:
                rightPressed = false;
                break;
            case LEFT_RELEASED:
                leftPressed = false;
                break;
            case UP_RELEASED:
                upPressed = false;
                break;
            case DOWN_RELEASED:
                downPressed = false;
                break;
            case BOOST_RELEASED:
                boostPressed = false;
                break;
        }

    }

    /**
     * Registers this Event Receiver with the Event Dispatcher.
     */
    @Override
    public void registerReceiver() {
        EventDispatcher.getSingletonDispatcher().registerReceiver(Event.TYPE.INPUT, this);
    }
}
