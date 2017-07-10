package com.araceinspace.InputSubSystem;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
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

    @Override
    public Event initiateEvent() {
        return null;
    }

    @Override
    public void sendEvent(Event e) {

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

    @Override
    public boolean keyDown(int keycode) {
        sendEvent(new Event(Event.TYPE.INPUT, "KeyDown", keycode));
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        sendEvent(new Event(Event.TYPE.INPUT, "KeyUp", keycode));
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
        return false;
    }
}
