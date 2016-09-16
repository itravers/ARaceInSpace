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


/* Field Variables */

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


/* Constructors */

    /**
     * Creates a new Event with a specific type, id and certain data.
     * @param type {@link Event#type}
     * @param id {@link Event#id}
     * @param data {@link Event#data}
     */
    public Event(Event.TYPE type, String id, Object data){
        this.type = type;
        this.id = id;
        this.data = data;
    }

    /**
     * Creates a new Event with the type, id and data being NULL.
     */
    public Event(){
        new Event(null, null, null);
    }


/* Private Methods */


/* Public Methods */

    /**
     * Resets this Event.
     * The type, id and data are set to NULL.
     */
    public void reset(){
        this.type = null;
        this.id = null;
        this.data = null;
    }

    /* Getters and Setters */

    /**
     * Returns the type of this Event.
     * @return {@link Event#type}
     */
    public TYPE getType() {
        return type;
    }

    /**
     * Sets the type of this Event.
     * @param type {@link Event#type}
     */
    public void setType(TYPE type) {
        this.type = type;
    }

    /**
     * Returns the id of this Event.
     * @return {@link Event#id}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of this Event.
     * @param id {@link Event#id}
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the data encapsulated by this Event.
     * @return {@link Event#data}
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data to be encapsulated by this Event.
     * @param data {@link Event#data}
     */
    public void setData(Object data) {
        this.data = data;
    }
}
