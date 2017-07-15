package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.Managers.AnimationManager;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 7/11/17.
 * The graphics component the player will use
 */
public class PlayerGraphicsComponent extends TwoDGraphicsComponent {
    Player parent;

    public PlayerGraphicsComponent(Player p, Vector2 loc, TextureAtlas atlas, Animation animations) {
        super(atlas, animations);
        parent = p;
        this.setX(loc.x);
        this.setY(loc.y);
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

    public void update(float timeElapsed){
        PlayerState state = parent.getState().getCurrentState();
        AnimationManager animationManager = parent.parent.parent.animationManager;
        switch(state){
            case STAND_STILL_FORWARD:
                setAnimation(animationManager.getStandingStillForwardsAnimation());
                break;
            case WALK_SLOW:
                setAnimation(animationManager.getWalkSlowAnimation());
                break;
            case STAND_STILL_SIDEWAYS:
               // setAnimation(animationManager.getStandingStillSidewaysAnimation());
                break;
        }
    }

    public void setAnimation(Animation animation){
        this.currentAnimation = animation;
    }
}
