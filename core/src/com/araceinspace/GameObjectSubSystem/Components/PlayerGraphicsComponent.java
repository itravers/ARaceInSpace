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

    public PlayerGraphicsComponent(Player p, Vector2 loc, TextureAtlas.AtlasRegion region, Animation animations) {
        super(region, animations);
        parent = p;
        this.setX(loc.x);
        this.setY(loc.y);
        setupRendering(animations);
    }

    public void setupRendering(Animation currentAnimation){
        this.currentAnimation = currentAnimation;
    }

    public void render(float elapsedTime, SpriteBatch batch){
        boolean flip;
        if(parent == null){//takes care of when input isn't constructed yet
            flip = false;
        }else{
            flip = parent.getInput().flip();
        }

        TextureRegion frame = currentAnimation.getKeyFrame(elapsedTime, true);

        //batch.draw(frame, getX(), getY(), getWidth(), getHeight()); seems to work similar to original

        //batch.draw(frame, getX(), getY()); //player is big, and planets are scewed

        //originial
        batch.draw(frame,
                    flip ? getX()+getWidth() : getX(), getY(),
                    flip ? -getOriginX(): getOriginX(), getOriginY(),
                    flip ? -getWidth() : getWidth(), getHeight(),
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
                setAnimation(animationManager.getStandingStillSidewaysAnimation());
                break;
            case WAVE:
                setAnimation(animationManager.getWaveAnimation());
                break;
            case JUMP_FORWARD:
                setAnimation(animationManager.getJumpForwardAnimation());
                break;
            case FLYING:
                if(!parent.getInput().upPressed){
                    setAnimation(animationManager.getFlyingNoThrustAnimation());
                }else{
                    setAnimation(animationManager.getFlyingAnimation());
                }
                break;
            case LAND_FORWARD:
                setAnimation(animationManager.getLandForwardAnimation());
                break;
            case FLOAT_SIDEWAYS:
                setAnimation(animationManager.getFloatSidewaysAnimation());
                break;
            case LAND_SIDEWAYS:
                setAnimation(animationManager.getLandSidewaysAnimation());
                break;
            case WALK_FAST:
                setAnimation(animationManager.getWalkFastAnimation());
                break;
            case RUN_SLOW:
                setAnimation(animationManager.getRunSlowAnimation());
                break;
            case RUN_FAST:
                setAnimation(animationManager.getRunFastAnimation());
                break;
            case JUMP_SIDEWAYS:
                setAnimation(animationManager.getJumpSidewaysAnimation());
                break;
            case EXPLODING:
                setAnimation(animationManager.getExploadingAnimation());
        }
    }

    public void setAnimation(Animation animation){
        this.currentAnimation = animation;

    }
}
