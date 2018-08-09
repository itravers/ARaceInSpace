package com.araceinspace.InputSubSystem;

import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.Managers.RenderManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 7/10/17.
 * The InputRecorder serves two different purposes depending on what type of InputComponent
 * owns it. If a PlayerInputComponent owns it, then whenever the PlayerInputComponent
 * consumes an InputEvent it passes that Event to it’s InputRecorder to be saved.
 If a GhostInputComponent owns the InputRecorder then it no longer functions as a recorder.
 Instead the InputRecorder originates the Ghost’s events. The Input asks the EventDispatcher
 for an InputEvent, it then loads the data of that event with a specific GameInput. It then
 sets the ID of the InputEvent to be the same ID of the Ghost. When the Ghost receives an Event
 from the EventDispatcher it will check to see if the ID matches. If it does it will consume
 the event, if it doesn’t it will just discard it. This allows an InputRecorder to control a Ghost.

 */
public class InputRecorder{
    /* Static Variables */
    public static int FRAMES_PER_KEYFRAME = 60;


    /* Field Variables & Objects */
    ArrayList<Action> actions;

    /* Constructor */
    public InputRecorder(){
        actions = new ArrayList<Action>();
    }

    public InputRecorder(ArrayList<Action>a){
        actions = a;
    }

    /* Methods */

    /**
     * Records a GameInput into a new Action and saves into the actions ArrayList
     * @param input The GameInput to save
     */
    public void record(GameInput input){
        //actions.add(new Action(RenderManager.frameNum, input, Action.Type.INPUT));
        actions.add(new KeyAction(RenderManager.frameNum, input, Action.Type.INPUT, null, null, 0, 0));

    }

    public void record(KeyAction keyAction){
        actions.add(keyAction);
       // System.out.println(this + " Setting position from KeyActiona: "+keyAction.getPosition());
        //System.out.println(this + " Setting position from KeyActionb: "+actions.get(actions.size()-1).getPosition());
    }

    public void writeToFile(String fileName, int playTime, String name){
        KeyAction playTimeAction = new KeyAction(playTime, GameInput.PLAYTIME, Action.Type.INPUT, null, null , 0, 0);

       // System.out.println("name: " + name + " inthash: " + playTime + " decoded: "+ Integer.toString(playTime));
        InfoAction infoAction = new InfoAction(playTime+1, GameInput.NAME, Action.Type.INFO,name);
       // System.out.println(this + " Setting position from KeyAction: "+keyAction.getPosition());
        actions.add(infoAction);
        actions.add(playTimeAction);

        Json json = new Json();
        //System.out.println(json.toJson(json.prettyPrint(actions)));
        FileHandle file = Gdx.files.local(fileName);
        file.writeString(json.toJson(actions, ArrayList.class), false);
    }

    public String getReplay(int playTime, String name){
        actions.add(new InfoAction(playTime+1, GameInput.NAME, Action.Type.INFO, name));
        actions.add(new KeyAction(playTime, GameInput.PLAYTIME, Action.Type.INPUT, null, null, 0, 0));

        Json json = new Json(JsonWriter.OutputType.json);
        String jsonString = json.toJson(actions, ArrayList.class);

        return jsonString;
    }

    /**
     * Returns the next action that occured at or before the current frameNum
     * @param currentFrame the currenet frame num
     * @return Null if no actions recorded under current frame Time, else returns the action.
     */
    public Action getNextAction(int currentFrame, Action.Type type){
        Action returnVal = null;
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i).getFrameNum() <= currentFrame && actions.get(i).getType() == type &&
                    actions.get(i).getInput() != GameInput.PLAYTIME && actions.get(i).getInput() != GameInput.NAME){//we don't process PLAYTIME INPUTS
                returnVal =  actions.remove(i);
                break;
            }
        }
        return returnVal;
    }

    /**
     * The last input in every record file will be the ghosts playtime, the framenum will reflect the ms of playtime
     * @return
     */
    public int getPlayTime(){
        return actions.get(actions.size()-1).getFrameNum();
    }

    public String getName(){
        //return actions.get(actions.size()-1).getFrameNum();
        String name = "";
        if(actions.get(actions.size()-2).getType() == Action.Type.INFO){
            InfoAction action = (InfoAction)actions.get(actions.size()-2);
            name = action.getInfo();
        }else{
            name = "";
        }
        return name;
    }
}