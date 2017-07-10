package com.araceinspace.EventSubSystem;

/**
 * Created by Isaac Assegai on 9/16/16.
 * This is an interface to be implemented by
 * any object that want to receive a type of event.
 * The EventReceiver registers itself with the
 * EventDispatcher using a specific Event.TYPE.
 * After doing so it will receive any dispatches
 * that match it’s designated Event.TYPE.
 * An EventReceiver can register multiple times
 * and receive multiple Event.TYPE’s
 * from the EventDispatcher.
 */
public interface EventReceiver {
    public void receiveEvent(Event e);
    public void registerReceiver();
}
