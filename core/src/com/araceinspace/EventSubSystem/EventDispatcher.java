package com.araceinspace.EventSubSystem;

/**
 * Created by Isaac Assegai on 9/16/16.
 * The EventDispatcher has a dual purpose.
 * First it uses its EventPool to create/pool events.
 * An EventSender can use the EventDispatcher
 * to actually create the Event that will be
 * dispatched by the EventDispatcher.
 * Secondly, the EventDispatcher keeps track
 * of registered EventReceivers and the
 * Event.TYPE they are registered for.
 * When an Event is dispatched the EventDispatcher
 * forwards the event to any EventReceivers that
 * are registered for that specific type of event.
 * The EventDispatcher uses a ReceiverMap to keep
 * track of registered EventReceivers.

 */
public class EventDispatcher {

/* Field Variables */
    /**
     * The Minimiumu size a pool will be initialized at.
     */
    public static int MIN_POOL_SIZE = 1;

    /**
     * The Maximum size a pool will be allowed to grow to.
     * Any Events free'd into a pool greater than this size
     * will be dereferenced and left to the Garabage Collector.
     */
    public static int MAX_POOL_SIZE = 20;

    /**
     * The pool is used to create and store Events.
     */
    private EventPool pool;

    /**
     * The map allows us to register Receivers under a specific Event.TYPE
     */
    private ReceiverMap map;

/* Constructors */

    /**
     * Construct a new EventDispatcher with a default EventPool and ReceiverMap
     */
    public EventDispatcher(){
       new EventDispatcher(new EventPool(MIN_POOL_SIZE, MAX_POOL_SIZE), new ReceiverMap());
    }

    /**
     * Construct a new EventDispatcher with the given EventPool and ReceiverMap
     * @param p The EventPool to initialize with.
     * @param m The ReceiverMap to initialize with.
     */
    public EventDispatcher(EventPool p, ReceiverMap m){
        pool = p;
        map = m;
    }
/* Private Methods */

/* Public Methods */

    /**
     * Let EventSenders obtain an Event to build and dispatch.
     * @return An Event from the EventPool
     */
    public Event obtainEvent(){
        return pool.obtainEvent();
    }

    /**
     * Puts the Referenced Event back into the pool as free.
     * @param e The Event to free.
     */
    public void freeEvent(Event e){
        pool.freeEvent(e);
    }

    /**
     * Registers an EventReceiver with the ReceiverMap under a specific type key.
     * @param type The key the receiver is being registered under.
     * @param receiver The receiver being registered.
     */
    public void registerReceiver(Event.TYPE type, EventReceiver receiver){
        map.put(type, receiver);
    }
}
