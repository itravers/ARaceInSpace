package com.araceinspace.UnitTests;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventPool;

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
public class EventPool_Test implements UnitTest{

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
}
