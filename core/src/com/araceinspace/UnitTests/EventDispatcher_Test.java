package com.araceinspace.UnitTests;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;

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
    public EventDispatcher_Test(){

    }

    @Override
    public boolean test() {
        boolean passed = true;

        passed = constructorTest();
        if(!passed)return passed;

        passed = obtainEventTest();
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
}
