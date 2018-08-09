package com.araceinspace.InputSubSystem;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 8/8/18.
 * An info action is used to record and read
 * other information from a ghost file

 */
public class InfoAction extends Action{

    private String info;


    /* Constructors */
    public InfoAction(int frame, GameInput input, Type type, String info){
        super(frame, input, type);
        this.info = info;
    }

    /**
     * Need a no arg constructor for fromJson
     */
    public InfoAction(){

    }

    public void setInfo(String info){
        this.info = info;
    }

    public String getInfo(){
        return info;
    }
}