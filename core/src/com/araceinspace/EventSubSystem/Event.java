package com.araceinspace.EventSubSystem;

/**
 * Created by Isaac Assegai on 9/15/16.
 * An event is the object that the dispatcher forwards along.
 * An Event is created by the EventPool.
 * When an Event is done being used it is supposed to be returned to the EventPool.
 * An Event has an associated TYPE, ID and DATA.
 * These pieces of information are used by the EventReceiver
 * when it consumes the Event. The TYPE is what the EventDispatcher will filter on.
 * The ID is what the EventReceiver will filter on to make sure it
 * only uses the Events that pertain to it.
 * The DATA is Json Data(maybe) that the EventReceiver will use.

 */
public class Event {

    /**
     * Defines the type of Event available.
     */
    public static enum TYPE{INPUT, GHOST_INPUT, GAME_STATE, RENDER};

    /**
     * Stores the type of this event.
     */
    private Event.TYPE type;

    /**
     * The id of this particular incarnation of the event.
     * This will be changed by EventSenders to whatever they desire.
     */
    private String id;

    /**
     * This is the data that the Event encapsulates. It could be a string, object
     * or even json data. The object Receiver and Sender will decide.
     */
    private Object data;

    /**
     *
     * @param type {@link Event#type}
     * @param id {@link Event#id}
     * @param data {@link Event#data}
     */
    public Event(Event.TYPE type, String id, Object data){
        this.type = type;
        this.id = id;
        this.data = data;
    }
}
