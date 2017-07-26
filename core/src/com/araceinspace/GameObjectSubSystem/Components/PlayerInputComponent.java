package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventReceiver;
import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Player;
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
    GameInput lastWalkInput =  null;
    Player parent;
    GameInput currentInput;

    /* Constructors */
    public PlayerInputComponent(Player parent){
        this.parent = parent;
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
        System.out.println("inputEvent Received: " + e.getData());
        String id = e.getId();
        Event.TYPE type = e.getType();
        currentInput = (GameInput)e.getData();
        handleCurrentInput();
    }

    public void handleCurrentInput(){
        //handle undefined input error
        if(currentInput == null)return;

        switch(currentInput){
            case BOOST_PRESSED:
                boostPressed = true;
                break;
            case RIGHT_PRESSED:
                rightPressed = true;
                lastWalkInput = currentInput;
                break;
            case LEFT_PRESSED:
                leftPressed = true;
                lastWalkInput = currentInput;
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
            case JUMP_PRESSED:
                jumpPressed = true;
                break;
            case JUMP_RELEASED:
                jumpPressed = false;
                break;
            case TOUCH_NONE:
                setTouched("touchNone");
                upPressed = false;
                jumpPressed = false;
                leftPressed = false;
                downPressed = false;
                rightPressed = false;
                break;
            case TOUCH_RIGHT:
                setTouched("touchRight");
                lastWalkInput = currentInput;
                upPressed = false;
                jumpPressed = false;
                leftPressed = false;
                downPressed = false;
                rightPressed = true;
                break;
            case TOUCH_UP_RIGHT:
                setTouched("touchUpRight");
                lastWalkInput = currentInput;
                if(parent.getPhysics().onPlanet()){
                    upPressed = false;
                    rightPressed = false;
                    jumpPressed = true;
                }else{
                    jumpPressed = false;
                    upPressed = true;
                    rightPressed = true;
                }
                leftPressed = false;
                downPressed = false;
                break;
            case TOUCH_UP:
                setTouched("touchUp");
                if(parent.getPhysics().onPlanet()){
                    upPressed = false;
                    jumpPressed = true;
                }else{
                    jumpPressed = false;
                    upPressed = true;
                }
                leftPressed = false;
                downPressed = false;
                rightPressed = false;
                break;
            case TOUCH_UP_LEFT:
                setTouched("touchUpLeft");
                lastWalkInput = currentInput;
                if(parent.getPhysics().onPlanet()){
                    upPressed = false;
                    leftPressed = false;
                    jumpPressed = true;
                }else{
                    jumpPressed = false;
                    upPressed = true;
                    leftPressed = true;
                }
                rightPressed = false;
                downPressed = false;
                break;
            case TOUCH_LEFT:
                setTouched("touchLeft");
                lastWalkInput = currentInput;
                upPressed = false;
                jumpPressed = false;
                rightPressed = false;
                downPressed = false;
                leftPressed = true;
                break;
            case TOUCH_DOWN_LEFT:
                setTouched("touchDownLeft");
                lastWalkInput = currentInput;
                if(parent.getPhysics().onPlanet()){
                    downPressed = false;
                }else{
                    downPressed = true;
                }
                leftPressed = true;
                rightPressed = false;
                jumpPressed = false;
                upPressed = false;
                break;
            case TOUCH_DOWN:
                setTouched("touchDown");
                if(parent.getPhysics().onPlanet()){
                    downPressed = false; //if on planet we do nothing
                }else{
                    downPressed = true;
                }
                leftPressed = false;
                rightPressed = false;
                upPressed = false;
                jumpPressed = false;
                break;
            case TOUCH_DOWN_RIGHT:
                setTouched("touchDownRight");
                lastWalkInput = currentInput;
                if(parent.getPhysics().onPlanet()){ //if player is on planet don't press down, but only righ
                    downPressed = false;
                }else{
                    downPressed = true;
                }
                rightPressed = true;
                leftPressed = false;
                jumpPressed = false;
                upPressed = false;
                break;
            default:
                break;
        }
    }

    /**
     * Sets the given touched boolean to true
     * sets all other touched booleans to false
     * Used to keep touchpad events from spawning duplicates
     * @param touched
     */
    private void setTouched(String touched){
        touchRight = false;
        touchUpRight = false;
        touchUp = false;
        touchUpLeft = false;
        touchLeft = false;
        touchDownLeft = false;
        touchDown = false;
        touchDownRight = false;
        touchNone = false;

        if(touched.equals("touchRight")){
            touchRight = true;
        }else if(touched.equals("touchUpRight")){
            touchUpRight = true;
        }else if(touched.equals("touchUp")){
            touchUp = true;
        }else if(touched.equals("touchUpLeft")){
            touchUpLeft = true;
        }else if(touched.equals("touchLeft")){
            touchLeft = true;
        }else if(touched.equals("touchDownLeft")){
            touchDownLeft = true;
        }else if(touched.equals("touchDown")){
            touchDown = true;
        }else if(touched.equals("touchDownRight")){
            touchDownRight = true;
        }else if(touched.equals("touchNone")){
            touchNone = true;
        }
    }

    /**
     * Registers this Event Receiver with the Event Dispatcher.
     */
    @Override
    public void registerReceiver() {
        EventDispatcher.getSingletonDispatcher().registerReceiver(Event.TYPE.INPUT, this);
    }

    /**
     * Returns true if the jump button is pressed.
     * @return
     */
    public boolean jumpPressed(){
        return jumpPressed;
    }

    /**
     * Returns true if one of the thrust buttons are pressed.
     * @return
     */
    public boolean thrustPressed(){
        if(upPressed || downPressed){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Returns true if no player inputs are currently pressed.
     * @return
     */
    public boolean noInputs(){
        if(!boostPressed && !upPressed && !downPressed && !rightPressed && !leftPressed && !jumpPressed){
            return true;
        }else{
            return false;
        }
    }

    /**
     * returns true if a walk input is pressed, false if not
     * @return
     */
    public boolean walkInput(){
        if(rightPressed || leftPressed){
            return true;
        }else{
            return false;
        }
    }

    /**
     * returns true if players leftINput is pressed
     * @return
     */
    public boolean flip(){
        if(lastWalkInput == GameInput.LEFT_PRESSED || lastWalkInput == GameInput.TOUCH_LEFT || lastWalkInput == GameInput.TOUCH_DOWN_LEFT || lastWalkInput == GameInput.TOUCH_UP_LEFT){
            return true;
        }else{
            return false;
        }
    }
}
