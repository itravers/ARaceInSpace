Event SubSystem
===============
The Event system uses the [Service Locator Pattern](gameprogrammingpatterns.com/service-locator.html) to decouple Events, such as inputs, from the Objects that receive the events, such as Players and Ghosts. A EventSender creates and sends an Event to the EventDispatcher. The EventDispatcher forwards the Event along to the appropriate EventReceiver which receives the Event and does with it what it wants.  This is supposed to help with extensibility in the future. Perhaps in a future game we will add multiplayer support. In that case we want to start building our event system now.

<p align="center" alt="A mockup, not a screenshot">
  <img src="http://i.imgur.com/EeOmFO3.png">
</p>

### EventDispatcher
The EventDispatcher has a dual purpose. First it uses its EventPool to create/pool events. An EventSender can use the EventDispatcher to actually create the Event that will be dispatched by the EventDispatcher.  Secondly, the EventDispatcher keeps track of registered EventReceivers and the Event.TYPE they are registered for. When an Event is dispatched the EventDispatcher forwards the event to any EventReceivers that are registered for that specific type of event. The EventDispatcher uses a ReceiverMap to keep track of registered EventReceivers.

### EventPool
Extends LibGdx.Pool<Event>. This is a factory used to create, track and limit the amount of events. The EventPool allows reuse of individual Events. Instead of creating thousands of events and letting the Garbage Collector free their memory when no longer used. We are going to keep the objects and reuse them. The EventDispatcher can use obtainEvent() to get an event. When it does this an event will be taken out of the pool, if the pool is empty then a new Event Object will be created. When the EventReceiver is done with the event, it will use the EventDispatcher to free the event. The Event will be put back in the regular queue, or if there are too many idle events a few of them will be removed manually by the superclass.

### ReceiverMap
The ReceiverMap extends java’s HashMap<Event.TYPE, List<EventReceiver>>. It is a map that returns a List of EventReceivers when referenced by a specific Event.TYPE.

### Event
An event is the object that the dispatcher forwards along. An Event is created by the EventPool. When an Event is done being used it is supposed to be returned to the EventPool. An Event has an associated TYPE, ID and DATA. These pieces of information are used by the EventReceiver when it consumes the Event. The TYPE is what the EventDispatcher will filter on. The ID is what the EventReceiver will filter on to make sure it only uses the Events that pertain to it. The DATA is Json Data that the EventReceiver will use.

#### Event.TYPE
An enum used to track the type of event, such as input type, etc. This is what the EventDispatcher filters on.

### EventSender
This is an interface to be implemented by any objects that want to send an event to another group of object. This class will use the EventDispatcher to create its Event. It’ll then change the data as it needs to. Finally it will use the EventDispatcher to send it’s new event.

### EventReceiver
This is an interface to be implemented by any object that want to receive a type of event. The EventReceiver registers itself with the EventDispatcher using a specific Event.TYPE. After doing so it will receive any dispatches that match it’s designated Event.TYPE. An EventReceiver can register multiple times and receive multiple Event.TYPE’s from the EventDispatcher.



