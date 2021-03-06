package com.araceinspace.TestSubSystem;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventPool;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 9/16/16.
 * Tests the ability of the EventPool
 * to:
 * construct an EventPool and make sure its initial setup is correct
 * create and return a new object
 * store a freed object
 * return an object after it was freed
 * return another object by creating it
 *
 * Create a bunch of events and make sure pool doesn't exceed maxCapacity
 */
public class EventPool_Test implements UnitTest {

    /**
     * Constructor
     */
    public EventPool_Test(){

    }

    /**
     * Tests the EventPool
     * @return
     */
    @Override
    public boolean test() {
        boolean passed = true;

        passed = constructorTest();
        if(!passed)return passed;

        passed = createAndReturnTest();
        if(!passed)return passed;

        passed = storeEventWhenNoneHasBeenPreviouslyMade();
        if(!passed)return passed;

        passed = attemptToFreeAnEventTwice();
        if(!passed)return passed;

        passed = attemptToFreeAnEventTwice2();
        if(!passed)return passed;

        passed = testCapacity();
        if(!passed)return passed;

        return passed;
    }

    /**
     * Returns the name of this test.
     * @return
     */
    @Override
    public String getName() {
        return "EventPool_Test";
    }

    /**
     * Test the Constructor and
     * makes sure max gets set, and the
     * proper number of free Events at first.
     * @return
     */
    private boolean constructorTest(){
        boolean passed = false;
        String passedMsg = "Failed";
        EventPool pool = new EventPool(1, 10);

        if(pool != null && pool.max == 10 && pool.getFree() == 0){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" constructorTest()");
        return passed;
    }

    /**
     * Tests the EventPools ability to create a new Event
     * when it doesn't have any events yet created.
     * @return
     */
    private boolean createAndReturnTest(){
        boolean passed = false;
        String passedMsg = "Failed";

        EventPool pool = new EventPool(2, 8);
        Event event = pool.obtainEvent();

        if(event != null){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" createAndReturnTest()");
        return passed;
    }

    /**
     * Manually creates a new event and try's to
     * add it to a pool that hasn't created any events on it's own.
     * The Pool should store the Event even if it didn't create it.
     * @return True if the pool stores an Event that it didn't create.
     */
    private boolean storeEventWhenNoneHasBeenPreviouslyMade(){
        boolean passed = false;
        String passedMsg = "Failed";

        EventPool pool = new EventPool(0, 10);

        //manually create an event
        Event event = new Event(Event.TYPE.INPUT, "testID", null);

        pool.freeEvent(event);

        if(pool.obtainEvent() == event){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" storeEventWhenNoneHasBeenPreviouslyMade()");
        return passed;
    }

    /**
     * This will instantiate a pool
     * Create an event, then
     * try to free that event twice to
     * see if the event Pool accepts duplicates
     * @return True if the EventPool does NOT allow duplicates.
     */
    private boolean attemptToFreeAnEventTwice(){
        boolean passed = false;
        String passedMsg = "Failed";

        EventPool pool = new EventPool(0, 10);

        //obtain an event, then free it twice in a row.
        Event event = pool.obtainEvent();
        pool.freeEvent(event);
        pool.freeEvent(event);

        //test to make sure the pool only has 1 current free object and not more or less
        if(pool.getFree() == 1){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" attemptToFreeAnEventTwice()");
        return passed;
    }

    /**
     * First we obtain an event
     * then we attempt to free it twice
     * then we obtain an event, which should be the free'd one
     * then we test if the pool still shows the
     * event that we just obtained as free.
     * This WAS failing, Had to adapt Pool.java for it to work,
     * submitted a bug report to libgdx.
     * @return True if the EventPool does NOT allow duplicates.
     */
    private boolean attemptToFreeAnEventTwice2(){
        boolean passed = false;
        String passedMsg = "Failed";

        EventPool pool = new EventPool(0, 10);

        //obtain an event, then free it twice in a row.
        Event event = pool.obtainEvent();
        pool.freeEvent(event);
        pool.freeEvent(event);

        Event eventA = pool.obtainEvent();
        Event eventB = pool.obtainEvent();

        if(eventA != eventB){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" attemptToFreeAnEventTwice2()");
        return passed;
    }

    private boolean testCapacity(){
        boolean passed = false;
        String passedMsg = "Failed";

        //create an event pool with a max of 10 items.
        EventPool pool = new EventPool(0, 10);

        ArrayList<Event> eventList = new ArrayList<Event>();

        //obtain 11 events from the pool.
        for(int i = 0; i < 11; i++){
            eventList.add(pool.obtainEvent());
        }

        //free 10 of those events
        for(int i = 10; i > 0; i--){
            pool.freeEvent(eventList.remove(i));
        }
        int poolSize = pool.getFree();

        //try to free the last event
        pool.freeEvent(eventList.get(0));

        //check to make sure the pool didn't add the last item
        if(pool.getFree() == poolSize){
            passed = true;
            passedMsg = "Passed";
        }

        System.out.println("     "+passedMsg+" testCapacity()");
        return passed;
    }
}
