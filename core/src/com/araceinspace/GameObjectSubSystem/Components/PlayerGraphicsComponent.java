package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.PlayerPrototype;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.Managers.ResourceManager;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Isaac Assegai on 7/11/17.
 * The graphics component the player will use
 */
public class PlayerGraphicsComponent extends TwoDGraphicsComponent {
    PlayerPrototype parent;
    float yOffset; //Used to allow sprite to overlap physics
    Group labelGroup = new Group();
    Label nameLabel;
    Vector2 tmp; //used to calculate name label placement

    public PlayerGraphicsComponent(PlayerPrototype p, Vector2 loc, TextureAtlas.AtlasRegion region, Animation animations) {
        super(region, animations);
        parent = p;
        this.setX(loc.x-getWidth()/2);//center is in middle of players feet
        this.setY(loc.y);
        setupRendering(animations);
        yOffset = getHeight()/6;
        nameLabel = new Label(parent.labelName, parent.parent.parent.resourceManager.getSkin(), "extra_small");

        labelGroup.addActor(nameLabel);
        tmp = new Vector2();



    }

    public void setupRendering(Animation currentAnimation){
        this.currentAnimation = currentAnimation;
    }

    public void render(float elapsedTime, SpriteBatch batch){
        boolean flip;
        if(parent == null){//takes care of when input isn't constructed yet
            flip = false;
        }else{
            flip = flip();
        }
       // System.out.println("currentAnimation: "+ currentAnimation.name);
        TextureRegion frame = currentAnimation.getKeyFrame(elapsedTime, true);

        //batch.draw(frame, getX(), getY(), getWidth(), getHeight()); seems to work similar to original

        //batch.draw(frame, getX(), getY()); //player is big, and planets are scewed

        //originial
        /*batch.draw(frame,
                    flip ? getX()+getWidth() : getX(), getY(),
                    flip ? -getOriginX(): getOriginX(), getOriginY(),
                    flip ? -getWidth() : getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    getRotation());*/
       // parent.parent.parent.resourceManager.Font20.draw(batch, "Test Label", 0, 0);
       // parent.parent.parent.resourceManager.Font20.draw(batch, "Test 2", getX(), getY()-yOffset, 0, 0,  0, 0, false);
        //nameLabel.setX(getX());
        //nameLabel.setY(getY());
        //nameLabel.setRotation(45);
        //nameLabel.draw(batch, 1);
        //nameLabel.rotateBy(20);

        float x = getX()+getWidth()/2;
        float y = getY()+getWidth()/2;
        //System.out.println("x: " + getX() + " y: " + getY());

        //System.out.println("xB: " + parent.getPhysics().getBody().getPosition().x + " yB: " + parent.getPhysics().getBody().getPosition().y);
        nameLabel.setFontScale(.4f);

        tmp.set(x, y);
        tmp = tmp.setLength(20);
        tmp = tmp.setAngle(getRotation()+140);
        //tmp.x = tmp.x - nameLabel.getWidth()/2;



        labelGroup.setPosition(x+tmp.x, y+tmp.y);

        labelGroup.setRotation(getRotation());
       // nameLabel.setDebug(parent.parent.parent.devMode);
       // nameLabel.setEllipsis(true);
       // labelGroup.setDebug(parent.parent.parent.devMode);

        labelGroup.draw(batch, 1);
        //batch.draw();


        batch.draw(frame,
                flip ? getX()+getWidth() : getX(), getY()-yOffset,
                flip ? -getOriginX(): getOriginX(), getOriginY()+yOffset,
                flip ? -getWidth() : getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());

        //to help find the difference between player space, and planet space.
        //batch.draw(frame, 0, 0, 0, 0, 15, 15, 1, 1, 0);
    }

    public void update(float timeElapsed){
        PlayerState state = ((PlayerStateComponent)parent.getState()).getCurrentState();
        ResourceManager resourceManager = parent.parent.parent.resourceManager;
        switch(state){
            case STAND_STILL_FORWARD:
                setAnimation(resourceManager.getStandingStillForwardsAnimation());
                break;
            case WALK_SLOW:
                setAnimation(resourceManager.getWalkSlowAnimation());
                break;
            case STAND_STILL_SIDEWAYS:
                setAnimation(resourceManager.getStandingStillSidewaysAnimation());
                break;
            case WAVE:
                setAnimation(resourceManager.getWaveAnimation());
                break;
            case JUMP_FORWARD:
                setAnimation(resourceManager.getJumpForwardAnimation());
                break;
            case FLYING:
                /*if(!parent.getInput().upPressed){
                    setAnimation(resourceManager.getFlyingNoThrustAnimation());
                }else{
                    setAnimation(resourceManager.getFlyingAnimation());
                }*/
                /*
                InputComponent input = parent.getInput();
                if(input.upPressed && input.rightPressed){
                    setAnimation(resourceManager.getFlyingRightAnimation());

                }else if(input.upPressed && input.leftPressed){
                    setAnimation(resourceManager.getFlyingLeftAnimation());

                }else if(input.upPressed){
                    setAnimation(resourceManager.getFlyingAnimation());
                }else{
                    setAnimation(resourceManager.getFlyingNoThrustAnimation());
                }
                */
                //Choose which flying animation to show based on key inputs
                InputComponent input = parent.getInput();
                if(input.upPressed && input.rightPressed && input.leftPressed){
                    setAnimation(resourceManager.getFlyingAnimation());
                }else if(input.upPressed && input.rightPressed){
                    setAnimation(resourceManager.getFlyingRightAnimation());
                }else if(input.upPressed && input.leftPressed){
                    setAnimation(resourceManager.getFlyingLeftAnimation());
                }else if(input.upPressed){
                    setAnimation(resourceManager.getFlyingAnimation());
                }else if(input.downPressed && input.rightPressed && input.leftPressed){
                    setAnimation(resourceManager.getFlyingBackwardAnimation());
                }else if(input.downPressed && input.rightPressed){
                    setAnimation(resourceManager.getFlyingRightBackAnimation());
                }else if(input.downPressed && input.leftPressed){
                    setAnimation(resourceManager.getFlyingLeftBackAnimation());
                }else if(input.downPressed) {
                    setAnimation(resourceManager.getFlyingBackwardAnimation());
                }else if(input.leftPressed && input.rightPressed) {
                    setAnimation(resourceManager.getFlyingNoThrustAnimation());
                }else if(input.rightPressed) {
                    setAnimation(resourceManager.getFlyingRightNoThrustAnimation());
                }else if(input.leftPressed){
                    setAnimation(resourceManager.getFlyingLeftNoThrustAnimation());
                }else{
                    setAnimation(resourceManager.getFlyingNoThrustAnimation());
                }
                break;
            case LAND_FORWARD:
                setAnimation(resourceManager.getLandForwardAnimation());
                break;
            case FLOAT_SIDEWAYS:
                setAnimation(resourceManager.getFloatSidewaysAnimation());
                break;
            case LAND_SIDEWAYS:
                setAnimation(resourceManager.getLandSidewaysAnimation());
                break;
            case WALK_FAST:
                setAnimation(resourceManager.getWalkFastAnimation());
                break;
            case RUN_SLOW:
                setAnimation(resourceManager.getRunSlowAnimation());
                break;
            case RUN_FAST:
                setAnimation(resourceManager.getRunFastAnimation());
                break;
            case JUMP_SIDEWAYS:
                setAnimation(resourceManager.getJumpSidewaysAnimation());
                break;
            case EXPLODING:
                setAnimation(resourceManager.getExploadingAnimation());
        }
    }

    public void setAnimation(Animation animation){
        this.currentAnimation = animation;

    }

    /**
     * returns true if players leftINput is pressed
     * @return
     */
    public boolean flip(){
        //return false;//test
        //*
        GameInput lastWalkInput = parent.getInput().getLastWalkInput();
        if(parent.getState().onPlanet() && (lastWalkInput == GameInput.LEFT_PRESSED || lastWalkInput == GameInput.TOUCH_LEFT || lastWalkInput == GameInput.TOUCH_DOWN_LEFT || lastWalkInput == GameInput.TOUCH_UP_LEFT)){
            return true;
        }else{
            return false;
        }
        //*/
    }

}
