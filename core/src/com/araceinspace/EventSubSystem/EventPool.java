package com.araceinspace.EventSubSystem;

/**
 * Created by Isaac Assegai on 9/15/16.
 * Extends LibGdx.Pool<Event>.
 * This is a factory used to create, track and limit the amount of events.
 * The EventPool allows reuse of individual Events.
 * Instead of creating thousands of events and letting the
 * Garbage Collector free their memory when no longer used.
 * We are going to keep the objects and reuse them.
 * The EventDispatcher can use obtainEvent() to get an event.
 * When it does this an event will be taken out of the pool,
 * if the pool is empty then a new Event Object will be created.
 * When the EventReceiver is done with the event, it will use
 * the EventDispatcher to free the event.
 * The Event will be put back in the regular queue,
 * or if there are too many idle events a few of them will
 * be removed manually by the superclass.
 */
public class EventPool{
}
