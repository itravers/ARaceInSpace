package com.araceinspace.GameObjectSubSystem.Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Isaac Assegai on 7/11/17.
 * The graphics component the player will use
 */
public class PlayerGraphicsComponent extends TwoDGraphicsComponent {
    public PlayerGraphicsComponent(TextureAtlas atlas, Animation animations) {
        super(atlas, animations);
        setupRendering(animations);
    }

    public void setupRendering(Animation currentAnimation){
        this.currentAnimation = currentAnimation;
    }

    public void render(float elapsedTime, SpriteBatch batch){
        TextureRegion frame = currentAnimation.getKeyFrame(elapsedTime, true);
        batch.draw(frame,
                    getX(), getY(),
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    getRotation());
    }
}
