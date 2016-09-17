package com.araceinspace.EventSubSystem;

import java.util.ArrayList;

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

    /**
     * Used to give Static Singleton access to an EventDispatcher.
     */
    private static EventDispatcher singletonDispatcher = null;

/* Constructors */

    /**
     * Construct a new EventDispatcher with a default EventPool and ReceiverMap
     */
    public EventDispatcher(){
        pool = new EventPool(MIN_POOL_SIZE, MAX_POOL_SIZE);
        map = new ReceiverMap();
        singletonDispatcher = this;
    }

    /**
     * Construct a new EventDispatcher with the given EventPool and ReceiverMap
     * @param p The EventPool to initialize with.
     * @param m The ReceiverMap to initialize with.
     */
    public EventDispatcher(EventPool p, ReceiverMap m){
        pool = p;
        map = m;
        singletonDispatcher = this;
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

    /**
     * Dispatches the given event to every EventReceiver that
     * has formerly registered using registerReceiver
     * under the same Event.TYPE as the given Event.
     * @param e
     */
    public void dispatch(Event e){
        //First we will need the Type of the Event
        Event.TYPE type = e.getType();

        //Next we get an ArrayList of all the EventReceivers registered under this type.
        ArrayList<EventReceiver>list = map.get(type);

        //If there are any items in the list, we will send the Event to all of them.
        if(!list.isEmpty()){
            for(int i = 0; i < list.size(); i++){
                list.get(i).receiveEvent(e);
            }
        }
    }

    /**
     * Returns the Singleton instance of this Dispatcher.
     * @return
     */
    public static EventDispatcher getSingletonDispatcher(){
        if(singletonDispatcher == null){
            synchronized (EventDispatcher.class){
                if(singletonDispatcher == null){//this line is actually useful in multithreading
                    singletonDispatcher = new EventDispatcher();
                }
            }
        }
        return singletonDispatcher;
    }
}
