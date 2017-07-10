package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.PlayerInputComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerStateComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Isaac Assegai on 7/10/17.
 * This is the actual player avatar in the game world. This extends ThreeDGameObject.
 * It has PlayerInputComponent as its input component, which allows it to be controlled by
 * the player and to save all inputs to replay a game. It uses a PlayerPhysicsComponent.
 */
public class Player extends TwoDGameObject{

    /**
     * Constructor
     * @param atlas
     * @param regions
     * @param animations
     */
    public Player(TextureAtlas atlas, TextureAtlas.AtlasRegion regions, Animation animations) {
        super(atlas, regions, animations);
        input = new PlayerInputComponent();
        physics = new PlayerPhysicsComponent();
        state = new PlayerStateComponent();
    }

    @Override
    void update() {
        //TODO create player update code
    }

    @Override
    void dispose() {
        //TODO create player dispose code
    }
}
