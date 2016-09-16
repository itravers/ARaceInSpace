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
}
