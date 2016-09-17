package com.araceinspace.TestSubSystem;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventReceiver;
import com.araceinspace.EventSubSystem.ReceiverMap;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 9/16/16.
 * Test all the used methods of ReceiverMap.java
 */
public class ReceiverMap_Test implements UnitTest {
    /**
     * Implements the tests.
     * @return True if the
     */
    @Override
    public boolean test() {
        boolean passed = true;

        passed = constructorTest();
        if(!passed)return passed;

        passed = putTest();
        if(!passed)return passed;

        passed = getTest();
        if(!passed)return passed;

        return passed;
    }

    /**
     * Gives the name of this test.
     * @return
     */
    @Override
    public String getName() {
        return "ReceiverMap_Test";
    }

    private boolean constructorTest(){
        boolean passed = true;
        String passedMessage = "Passed";
        ReceiverMap map = null;
        map = new ReceiverMap();

        if(map == null){
            passed = false;
            passedMessage = "Failed";
        }
        System.out.println("     "+passedMessage+" constructorTest()");
        return passed;
    }

    private boolean putTest(){
        boolean passed = false;
        String passedMessage = "Failed";

        ReceiverMap map = null;
        map = new ReceiverMap();

        ReceiverTest r = new ReceiverTest();
        map.put(Event.TYPE.GAME_STATE, r);

        if(map.get(Event.TYPE.GAME_STATE) instanceof ArrayList){
            if(map.get(Event.TYPE.GAME_STATE).get(0) == r){
                passed = true;
                passedMessage = "Passed";
            }
        }
        System.out.println("     "+passedMessage+" putTest()");
        return passed;
    }

    /**
     * Tests the get() feature of the ReceiverMap.
     * Creates a Receiver map and adds several
     * EventReceivers to it. Then we use the get()
     * method to extract specific lists referenced
     * by event types. Then we check that the
     * objects we created and added are the
     * same objects that are returned.
     * @return
     */
    private boolean getTest(){
        boolean passed = true;
        String passedMessage = "Passed";

        ReceiverMap map = null;
        map = new ReceiverMap();

        ReceiverTest r1 = new ReceiverTest();
        map.put(Event.TYPE.GAME_STATE, r1);

        ReceiverTest r2 = new ReceiverTest();
        map.put(Event.TYPE.GAME_STATE, r2);

        ReceiverTest r3 = new ReceiverTest();
        map.put(Event.TYPE.INPUT, r3);

        ReceiverTest r4 = new ReceiverTest();
        map.put(Event.TYPE.INPUT, r4);

        ArrayList<EventReceiver> stateList = map.get(Event.TYPE.GAME_STATE);
        ArrayList<EventReceiver> inputList = map.get(Event.TYPE.INPUT);

        //Do the tests
        if(stateList.get(0) != r1 || stateList.get(1) != r2 || inputList.get(0) != r3 || inputList.get(1) != r4){
            passed = false;
            passedMessage = "Failed";
        }

        System.out.println("     "+passedMessage+" getTest()");
        return passed;
    }

    /**
     * Used by the putTest() to instantiate a ReceiverMap
     * by giving it a EventReceiver (this one)
     */
    private class ReceiverTest implements EventReceiver{
        /**
         * Print out Event Received if we Receive
         * event, this will never be called in this test.
         * @param e
         */
        @Override
        public void receiveEvent(Event e) {
            System.out.println("Event Received");
        }
    }
}
