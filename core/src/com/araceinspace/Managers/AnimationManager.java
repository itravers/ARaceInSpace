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

    //Planet Animation data
    private TextureAtlas planetAtlas = null;
    private TextureAtlas gravityWellAtlas = null;

    /* Constructors */
    public AnimationManager(GameWorld p){
        System.out.println("Animation Constructor");
        parent = p;
        setupPlanets();
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

    /**
     * Sets the planets atlas
     */
    private void setupPlanets(){
        planetAtlas = new TextureAtlas((Gdx.files.internal("data/Planets.pack")));
        gravityWellAtlas = new TextureAtlas(Gdx.files.internal("data/gravity_Well.txt"));
       // planetRegion = planetAtlas.findRegions("Moon");
        //planetRotateAnimation = new Animation(1/2f, planetRegion);
    }

    /* Public Methods */

    /**
     * Called when game is loaded.
     * Setup all needed animations.
     */
    public void setupAnimations(){
        setupStandingStillForwardsAnimation();
    }

    public TextureAtlas getStandingStillForwardsAtlas() {
        return standingStillForwardsAtlas;
    }

    public Array<TextureAtlas.AtlasRegion> getStandingStillForwardsRegion() {
        return standingStillForwardsRegion;
    }

    public Animation getStandingStillForwardsAnimation() {
        return standingStillForwardsAnimation;
    }

    public TextureAtlas getPlanetAtlas(){
        if(planetAtlas == null)setupPlanets();//incase animation manager constructed after level manager
        return planetAtlas;
    }

    public void setPlanetAtlas(TextureAtlas p){
        planetAtlas = p;
    }

    public Animation getPlanetAnimationFromName(String atlasName){
        Array<TextureAtlas.AtlasRegion> planetRegion = getPlanetAtlas().findRegions(atlasName);
        Animation planetRotateAnimation = new Animation(1/2f, planetRegion);
        return planetRotateAnimation;
    }

    public TextureAtlas getGravityWellAtlas() {
        if(gravityWellAtlas == null)setupPlanets();
        return gravityWellAtlas;
    }

    public void setGravityWellAtlas(TextureAtlas gravityWellAtlas) {
        this.gravityWellAtlas = gravityWellAtlas;
    }
}
