package com.araceinspace.InputSubSystem;

import com.badlogic.gdx.Game;

/**
 * Created by Isaac Assegai on 7/10/17.
 * An Action is just a GameInput associated with a specific game time. In theory by recording
 * every action a Player takes we should be able to completely duplicate his play behaviour
 * in a future game. This is why the InputRecorder keeps an array of Actions.
 */
public class Action {

    /* Field Variables */
    private int frameNum;
    private GameInput input;

    /* Constructor */
    public Action(int frameNum, GameInput input){
        this.frameNum = frameNum;
        this.input = input;
    }

    /* Methods */
    public int getFrameNum(){
        return frameNum;
    }

    public GameInput getInput(){
        return input;
    }
}
