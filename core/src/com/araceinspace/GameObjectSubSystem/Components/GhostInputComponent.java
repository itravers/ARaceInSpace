package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.InputSubSystem.Action;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.InputSubSystem.InputRecorder;
import com.araceinspace.Managers.RenderManager;

/**
 * Created by Isaac Assegai on 7/10/17.
 * When a GhostInputComponent receives an input from the EventDispatcher
 * it will process it normally and then it will NOT record it to it’s InputRecorder.
 * This is because with a GhostInputComponent the Input Recorder will have originated
 * the input event.
 * It will use the EventDispatcher to get the event to the GhostInputComponent.
 * Note, this is different than how a PlayerInputComponent processes Input Events
 */
public class GhostInputComponent extends InputComponent{

    /* Field Variables & Objects */
    InputRecorder inputRecorder;

    /* Constructors */
    public GhostInputComponent(){
        inputRecorder = new InputRecorder();
    }

    /* Methods */

    /**
     * Updates the input component
     * @param o
     */
    public void update(float timeElapsed) {
        Action nextAction = inputRecorder.getNextAction(RenderManager.frameNum);
        if(nextAction != null){
            GameInput input = nextAction.getInput();
        }
    }
}
