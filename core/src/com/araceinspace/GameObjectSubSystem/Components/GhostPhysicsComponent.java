package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.InputSubSystem.Action;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.InputSubSystem.KeyAction;
import com.araceinspace.Managers.RenderManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Isaac Assegai on 8/7/17.
 * Provides the ghost physics,
 * extends playerPhysicsComponent,
 * but overrides the update method
 * to read the inputrecorder to
 * place the ghost at a more correct location.
 */
public class GhostPhysicsComponent extends  PlayerPhysicsComponent{
    /**
     * Create a new PlayerPhysicsComponent
     *
     * @param p
     * @param world
     */
    public GhostPhysicsComponent(PlayerPrototype p, World world, Vector2 loc) {
        super(p, world, loc);
    }

    @Override
    public void update(float elapsedTime){
        KeyAction nextAction = (KeyAction)parent.getInput().inputRecorder.getNextAction(RenderManager.frameNum, Action.Type.KEY);
        if(nextAction != null){
           // System.out.println("type " + nextAction.getType());
            if(nextAction.getType() == Action.Type.KEY){
               // System.out.println("Setting position from KeyAction: "+nextAction.getPosition());
                parent.getPhysics().getBody().setTransform(nextAction.getPosition(), nextAction.getAngle());
                parent.getPhysics().getBody().setAngularVelocity(nextAction.getAngularVelocity());
                parent.getPhysics().getBody().setLinearVelocity(nextAction.getVelocity());
            }
        }
        super.update(elapsedTime);
    }
}
