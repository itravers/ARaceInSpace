package com.araceinspace.EventSubSystem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Isaac Assegai on 9/16/16.
 * The ReceiverMap extends java’s
 * HashMap<Event.TYPE, List<EventReceiver>>.
 * It is a map that returns a List of
 * EventReceivers when referenced by a specific
 * Event.TYPE.
 */
public class ReceiverMap extends HashMap<Event.TYPE, List<EventReceiver>>{

/* Field Variables */

/* Constructors */

    public ReceiverMap(){
        super();
    }
/* Private Methods */

/* Public  Methods */

    /**
     * Puts a designated EventReceiver into the
     * appropriate list in the super Hashmap.
     * @param type The Type of event we will reference this EventReceiver with.
     * @param item The EventReceiver we are mapping to the type.
     */
    public void put(Event.TYPE type, EventReceiver item){
        List l = super.get(type);
        l.add(item);
        super.put(type, l);
    }

    /**
     * Returns a list of EventReceiver
     * that are referenced by a certain type.
     * @param type The type of EventReceiver we are looking for.
     * @return A List of EventReceivers that want to be notified of this type.
     */
    public List<EventReceiver> get(Event.TYPE type){
        return super.get(type);
    }
}
