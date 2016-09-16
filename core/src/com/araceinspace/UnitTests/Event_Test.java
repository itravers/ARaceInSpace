package com.araceinspace.UnitTests;

import com.araceinspace.EventSubSystem.Event;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 9/15/16.
 * This unit test will test the functions of:
 * Event.java
 */
public class Event_Test implements UnitTest {

    private Event event = null;

    public Event_Test(){

    }

    @Override
    public boolean test() {
        boolean passed = true;

        passed = emptyConstructorTest();
        if(!passed)return passed;

        passed = fullConstructorTest();
        if(!passed)return passed;

        passed = idTest();
        if(!passed)return passed;

        passed = typeTest();
        if(!passed)return passed;

        passed = dataTest();
        if(!passed)return passed;

        passed = resetTest();
        if(!passed)return passed;

        return passed;
    }

    @Override
    public String getName() {
        return "Event_Test";
    }

    /**
     * Creates an empty event and check if
     * it exists and is indeed empty.
     * @return True if Test passed, False if test failed.
     */
    private boolean emptyConstructorTest(){
        boolean passed = false;
        String passedString = "Failed";

        //test empty constructor
        Event e = new Event();
        if(e != null){
            if(e.getType() == null && e.getId() == null && e.getData() == null){
                passed = true;
                passedString = "Passed";
            }
        }
        System.out.println("     "+passedString+" emptyConstructorTest()");
        return passed;
    }

    /**
     * Creates an Event using the full constructor.
     * I tests that the Event is constructed with data correctly.
     * @return True if the Event has been constructed correctly.
     */
    private boolean fullConstructorTest(){
        boolean passed = false;
        String passedString = "Failed";
        String data = "testData";
        String id = "testId";
        Event.TYPE type = Event.TYPE.GAME_STATE;

        //test a full constructor
        Event e = new Event(type, id, data);
        if(e != null){
            if(e.getData() == data && e.getType() == type && e.getId() == id){
                passed = true;
                passedString = "Passed";
            }
        }
        System.out.println("     "+passedString+" fullConstructorTest()");
        return passed;
    }

    /**
     * Create a new event, set it's id, and check that it's id is correct
     * several times.
     * @return True if setting and retrieving id's works correctly.
     */
    private boolean idTest(){
        boolean passed = true;
        String passedString = "Passed";
        Event e = new Event();

        //create and assign it's, then test that id's were assigned correctly.
        for(int i = 0; i < 10; i++){
            String id = "idTest"+i;
            e.setId(id);
            if(e.getId() != id){
                passed = false;
                passedString = "Failed";
            }
        }
        System.out.println("     "+passedString+" idTest()");
        return passed;
    }

    /**
     * Test the Events type setters and getters.
     * @return True if type setters and getters are working.
     */
    private boolean typeTest(){
        boolean passed = true;
        String passedString = "Passed";
        Event e = new Event();
        e.setType(Event.TYPE.GAME_STATE);
        if(e.getType() != Event.TYPE.GAME_STATE){
            passed = false;
            passedString = "Failed";
        }
        System.out.println("     "+passedString+" typeTest()");
        return passed;
    }

    /**
     * Tests the data getters and sitters
     * @return True if the getters and sitters work.
     */
    private boolean dataTest(){
        boolean passed = true;
        String passedString = "Passed";
        Event e = new Event();
        Vector2 data = new Vector2(5, 10);
        e.setData(data);
        if(e.getData() != data){
            passed = false;
            passedString = "Failed";
        }
        System.out.println("     "+passedString+" dataTest()");
        return passed;
    }

    /**
     * Test the Events reset ability
     * @return True if the event resets correctly.
     */
    private boolean resetTest(){
        boolean passed = false;
        String passedString = "False";
        String data = "testData";
        String id = "testId";
        Event.TYPE type = Event.TYPE.GAME_STATE;

        //test a full constructor
        Event e = new Event(type, id, data);
        e.reset();
        if(e.getId() == null && e.getType() == null && e.getData() == null){
            passed = true;
            passedString = "Passed";
        }
        System.out.println("     "+passedString+" resetTest()");
        return passed;
    }
}
