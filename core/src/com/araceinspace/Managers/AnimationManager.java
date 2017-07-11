package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Keeps track of all the sprite animations and data
 */
public class AnimationManager {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;

    //Player Sprite Animation Data
    private TextureAtlas standingStillForwardsAtlas;
    private Array<TextureAtlas.AtlasRegion> standingStillForwardsRegion;
    private Animation standingStillForwardsAnimation;

    /* Constructors */
    public AnimationManager(GameWorld p){
        System.out.println("Animation Constructor");
        parent = p;
    }

    /* Private Methods */

    /**
     * Loads from file and set's up the standingStillForwards Animation
     */
    private void setupStandingStillForwardsAnimation(){

        standingStillForwardsAtlas = new TextureAtlas(Gdx.files.internal("data/StandingStillForward.pack"));
        standingStillForwardsRegion = standingStillForwardsAtlas.getRegions();
        standingStillForwardsAnimation = new Animation(1/30f, standingStillForwardsRegion);
    }

    /* Public Methods */

    /**
     * Called when game is loaded.
     * Setup all needed animations.
     */
    public void setupAnimations(){
        setupStandingStillForwardsAnimation();
    }
}
