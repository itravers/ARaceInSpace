package com.araceinspace.EventSubSystem;

/**
 * Created by Isaac Assegai on 9/16/16.
 * This is an interface to be implemented
 * by any objects that want to send an
 * event to another group of object.
 * This class will use the EventDispatcher
 * to create its Event. It’ll then change
 * the data as it needs to. Finally it
 * will use the EventDispatcher to send
 * it’s new event.
 */
public interface EventSender {
    public Event initiateEvent();
    public void sendEvent(Event e);
}
