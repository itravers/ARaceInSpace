package com.araceinspace.EventSubSystem;

import java.util.HashMap;

/**
 * Created by Isaac Assegai on 9/16/16.
 * The ReceiverMap extends java’s
 * HashMap<Event.TYPE, List<EventReceiver>>.
 * It is a map that returns a List of
 * EventReceivers when referenced by a specific
 * Event.TYPE.
 */
public class ReceiverMap extends HashMap<Event.TYPE, List<EventReceiver>>{
}
