package com.araceinspace.EventSubSystem;

import com.badlogic.gdx.utils.Pool;

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
public class EventPool extends Pool<Event>{

    /**
     * Constructor
     * @param initialCapacity The initial number of Events the pool is set up to hold.
     * @param maxCapacity The maximum number of Events that the pool can hold
     *                    new Events cannot be made if the pool is beyond capacity.
     */
    public EventPool(int initialCapacity, int maxCapacity){
        super(initialCapacity, maxCapacity);
    }

    /**
     * Returns an Event from the pool
     * The Event may be new from newObject() or be reused.
     * @return The Event Returned
     */
    public Event obtainEvent(){
        return super.obtain();
    }

    /**
     * Puts the specified Event back into the pool
     * making the event available to be obtained by
     * obtainEvent().
     * If an Event already exists in the Event pool, we do not
     * wan't to add it again
     * @param toFree The Event we want to add back to the Pool.
     */
    public void freeEvent(Event toFree){

        super.free(toFree);
    }

    /**
     * Creates a new Event to add to the pool.
     * This will be called automagically by the
     * extended Pool.
     * @returnA newly Created Event.
     */
    @Override
    protected Event newObject() {
        return new Event();
    }
}
