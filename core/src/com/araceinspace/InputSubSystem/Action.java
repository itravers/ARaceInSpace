package com.araceinspace.InputSubSystem;

import com.badlogic.gdx.Game;

/**
 * Created by Isaac Assegai on 7/10/17.
 * An Action is just a GameInput associated with a specific game time. In theory by recording
 * every action a Player takes we should be able to completely duplicate his play behaviour
 * in a future game. This is why the InputRecorder keeps an array of Actions.
 */
public class Action {
    /* Static Variables */
    public static enum Type {INPUT, KEY, INFO}

    /* Field Variables */
    private int frameNum;
    private GameInput input;
    private Type type;

    /* Constructor */
    public Action(int frameNum, GameInput input, Type type){
        this.frameNum = frameNum;
        this.input = input;
        this.type = type;
    }

    /*
    Construct an empty action.
     */
    public Action(){

    }

    /* Methods */
    public int getFrameNum(){
        return frameNum;
    }

    public GameInput getInput(){
        return input;
    }

    public Type getType(){ return type; }
}
