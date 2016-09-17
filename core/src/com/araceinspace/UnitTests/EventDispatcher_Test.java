package com.araceinspace.UnitTests;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventReceiver;
import com.araceinspace.EventSubSystem.EventSender;

/**
 * Created by Isaac Assegai on 9/16/16.
 * Test the Ability of the EventDipatcher by doing:
 * 1. Create 4 EventReceivers using and Register them with the Dispatcher using 2 different types.
 * 2. Create an EventSender.
 * 3. use the Sender to obtain an event from the dispatcher.
 * 4. Modify the Events type.
 * 5. Dispatch the event with the dispatcher.
 *
 * 6. TEST: the correct EventReceivers received the event, the other two didn't.
 *
 * 7. Free the Event
 */
public class EventDispatcher_Test implements UnitTest{
    EventDispatcher dispatcherTester = null;

    public EventDispatcher_Test(){

    }

    @Override
    public boolean test() {
        boolean passed = true;

        passed = constructorTest();
        if(!passed)return passed;

        passed = obtainEventTest();
        if(!passed)return passed;

        passed = freeEventTest();
        if(!passed)return passed;

        passed = registerReceiverAndSendTest();
        if(!passed)return passed;

        return passed;
    }

    @Override
    public String getName() {
        return "EventDispatcher_Test";
    }

    /**
     * Test that the EventDispatcher constructor works as expected.
     * @return
     */
    private boolean constructorTest(){
        boolean passed = false;
        String passedMsg = "Failed";

        EventDispatcher dispatcher = new EventDispatcher();

        if(dispatcher != null){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" constructorTest()");
        return passed;
    }

    /**
     * Tests that obtainEvent() returns an event.
     * @return
     */
    private boolean obtainEventTest(){
        boolean passed = false;
        String passedMsg = "Failed";

        EventDispatcher dispatcher = new EventDispatcher();
        Event event = dispatcher.obtainEvent();

        if(event != null){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" obtainEventTest()");
        return passed;
    }

    /**
     * Test that the dispatchers FreeEvent works and reset the event
     * @return True if test passes.
     */
    private boolean freeEventTest(){
        boolean passed = false;
        String passedMsg = "Failed";

        EventDispatcher dispatcher = new EventDispatcher();
        Event event = dispatcher.obtainEvent();
        event.setId("testID");
        dispatcher.freeEvent(event);

        if(event.getId() == null){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" freeEventTest()");
        return passed;
    }

    /**
     * Create a EventDispatcher singleton
     * and check it.
     *
     * Create an EventReceiver and register it.
     *
     * Create an EventSender, get an event, modify and dispatch it.
     *
     * Check that the proper EventReceiver actually Received the Event.
     * @return
     */
    private boolean registerReceiverAndSendTest(){
        boolean passed = false;
        String passedMsg = "Failed";

        dispatcherTester = EventDispatcher.getSingletonDispatcher();

        System.out.println("     "+passedMsg+" registerReceiverAndSendTest()");
        return passed;
    }

    /**
     * A class that implements EventReceiver to test Registering and Receiving.
     */
    private class Receiver implements EventReceiver{
        EventDispatcher dispatcher = null;
        public Event eventReceived = null;

        public Receiver(){
            dispatcher = EventDispatcher.getSingletonDispatcher();
        }

        @Override
        public void receiveEvent(Event e) {
            eventReceived = e;
        }

        public void register(Event.TYPE type){
            dispatcher.registerReceiver(type, this);
        }
    }

    /**
     * A class that implements EventSender to test sending an event.
     */
    private class Sender implements EventSender{

        EventDispatcher dispatcher;

        public Sender(){
            dispatcher = EventDispatcher.getSingletonDispatcher();
        }

        @Override
        public Event initiateEvent() {
            return dispatcher.obtainEvent();
        }

        @Override
        public void sendEvent(Event e) {
            dispatcher.dispatch(e);
        }
    }
}