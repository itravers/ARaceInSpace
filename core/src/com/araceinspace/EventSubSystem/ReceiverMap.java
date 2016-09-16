package com.araceinspace.EventSubSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Isaac Assegai on 9/16/16.
 * The ReceiverMap extends java’s
 * HashMap<Event.TYPE, List<EventReceiver>>.
 * It is a map that returns a List of
 * EventReceivers when referenced by a specific
 * Event.TYPE.
 */
public class ReceiverMap extends HashMap<Event.TYPE, ArrayList<EventReceiver>>{

/* Field Variables */
    private String id;
/* Constructors */

    public ReceiverMap(){
        super();
        id = String.valueOf(new Random().nextInt());
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
        ArrayList l = super.get(type);
        if(l == null)l = new ArrayList<EventReceiver>();
        l.add(item);
        super.put(type, l);
    }

    /**
     * Returns a list of EventReceiver
     * that are referenced by a certain type.
     * @param type The type of EventReceiver we are looking for.
     * @return A List of EventReceivers that want to be notified of this type.
     */
    public ArrayList<EventReceiver> get(Event.TYPE type){
        return super.get(type);
    }
}
