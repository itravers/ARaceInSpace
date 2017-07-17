package com.araceinspace.InputSubSystem;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
import com.araceinspace.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 7/10/17.
 * The InputManager is responsible for receiving any input from the actual player.
 * It accomplishes this by using LibGdX’s InputProcessor interface and it’s GestureListener
 * interface. This allows it to listen for keyboard keys and mouse clicks on a computer, or
 * touches flings and zooms on a touch screen. The InputManager does NOT decide what to do
 * with the inputs it gets. Instead it creates an GameInput, then requests an Event of type.
 * INPUT from the EventDispatcher. It adds the created GameInput to the event then asks the
 * EventDispatcher to dispatch it. A GameObjects InputComponent receives this and consumes it.
 */
public class InputManager implements EventSender, InputProcessor, GestureDetector.GestureListener {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;
    InputMultiplexer multiplexer;

    /* Constructors */

    public InputManager(GameWorld p){
        parent = p;
        Gdx.input.setCatchBackKey(true);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);

    }

    /* Private Methods */

    /* Public Methods */

    public void addInputProcessor(InputProcessor p){
        multiplexer.addProcessor(p);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void sendEvent(Event e) {
        EventDispatcher.getSingletonDispatcher().dispatch(e);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    /**
     * When the keyboard has a key pressed down. We figure out what key it is
     * map that key to a GameInput, and sent that GameInput through the Event Subsystem
     * to the player.
     * @param keycode
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
      //  System.out.println("KeyDown: " + keycode);
        GameInput input = null;
        if(keycode == Input.Keys.W){
            input = GameInput.UP_PRESSED;
        }else if(keycode == Input.Keys.A){
            input = GameInput.LEFT_PRESSED;
        }else if(keycode == Input.Keys.S){
            input = GameInput.DOWN_PRESSED;
        }else if(keycode == Input.Keys.D){
            input = GameInput.RIGHT_PRESSED;
        }else if(keycode == Input.Keys.SHIFT_LEFT){
            input = GameInput.BOOST_PRESSED;
        }else if(keycode == Input.Keys.F1){
            parent.devMode = !parent.devMode;
            return true; //don't send event, just toggle devMode
        }else if(keycode == Input.Keys.SPACE){
            input = GameInput.JUMP_PRESSED;
        }else if(keycode == Input.Keys.BACK) {
            System.out.println("KeyDown: " + keycode);
            parent.gameStateManager.setCurrentState(parent.gameStateManager.popState());

        }
        sendEvent(new Event(Event.TYPE.INPUT, "PlayerInput", input));
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        GameInput input = null;
        if(keycode == Input.Keys.W){
            input = GameInput.UP_RELEASED;
        }else if(keycode == Input.Keys.A){
            input = GameInput.LEFT_RELEASED;
        }else if(keycode == Input.Keys.S){
            input = GameInput.DOWN_RELEASED;
        }else if(keycode == Input.Keys.D){
            input = GameInput.RIGHT_RELEASED;
        }else if(keycode == Input.Keys.SHIFT_LEFT){
            input = GameInput.BOOST_RELEASED;
        }else if(keycode == Input.Keys.F1){
            return true;//don't do anything
        }else if(keycode == Input.Keys.SPACE){
            input = GameInput.JUMP_RELEASED;
        }
        sendEvent(new Event(Event.TYPE.INPUT, "PlayerInput", input));
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        float zoom = parent.renderManager.getCameraZoom();
        float baseZoom = parent.renderManager.getBaseZoom();
        if(amount <= 0){
            float zoomChange = zoom/10;
            zoom -= zoomChange;
            if(zoom <= baseZoom*.4f && !parent.devMode) zoom = baseZoom*.4f;
            System.out.println("Zoom In " + zoom);
        }else{
            float zoomChange = zoom/10;
            zoom += zoomChange;

            if(zoom > baseZoom*2.5 && !parent.devMode){
                zoom = baseZoom*2.5f;
                System.out.println("Zoom Out " + zoom);
            }

        }
        //parent.getRenderManager().viewport.getC
        parent.renderManager.setCameraZoom(zoom);
        return false;
    }
}
