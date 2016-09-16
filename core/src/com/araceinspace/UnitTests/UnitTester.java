package com.araceinspace.UnitTests;

import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 9/15/16.
 * The UnitTests package is for development purposes only.
 * None of these classes should end up being included in any
 * other SubSystems.
 *
 * As classes in the game are developed, new UnitTests will be
 * developed to make sure they do not break with later development.
 */
public class UnitTester {

    private ArrayList<UnitTest>tests;

    /**
     * Entry point for the UnitTester to start.
     * @param args Not doing anything with these yet.
     */
    public static void main(String[] args){
        new UnitTester();
    }

    public UnitTester(){
        tests = new ArrayList<UnitTest>();
        addTests();
        test();
    }

    /**
     * As we develop this is where we will wire up new unit tests.
     */
    private void addTests(){

    }

    /**
     * Loops through all the tests in the list
     * and executes their tests.
     */
    private void test(){
        int failed = 0; //used to print final warning.
        System.out.println("Running Unit Tests");
        for(int i = 0; i < tests.size(); i ++){
            UnitTest test = tests.get(i);
            if(test.test()){
                System.out.println("Passed: " + test.getName());
            }else{
                System.out.println("Failed: " + test.getName());
                failed++;
            }
        }

        //Print out Failed/Passed report.
        System.out.println("PASSED: " + (tests.size()-failed));
        System.out.println("FAILED: " + failed);
        System.out.println("TOTAL :" + tests.size());
    }
}
