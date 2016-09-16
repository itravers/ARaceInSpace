package com.araceinspace.UnitTests;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventReceiver;
import com.araceinspace.EventSubSystem.ReceiverMap;

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
        /*map.put(Event.TYPE.GAME_STATE, new EventReceiver() {
            @Override
            public void receiveEvent(Event e) {
                System.out.println(e+" received");
            }
        });*/
        if(map == null){
            passed = false;
            passedMessage = "Failed";
        }
        System.out.println("     "+passedMessage+" constructorTest()");
        return passed;
    }
}
