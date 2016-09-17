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
    private EventPool pool;

    /* Constructors */

    /* Private Methods */

    /* Public Methods */
}
