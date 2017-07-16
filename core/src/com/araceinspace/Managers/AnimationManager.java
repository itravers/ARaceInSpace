package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.Gdx;
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

    private TextureAtlas walkSlowAtlas;
    private Array<TextureAtlas.AtlasRegion> walkSlowRegion;
    private Animation walkSlowAnimation;

    private TextureAtlas standingStillSidewaysAtlas;
    private Array<TextureAtlas.AtlasRegion> standingStillSidewaysRegion;
    private Animation standingStillSidewaysAnimation;

    private TextureAtlas jumpForwardAtlas;
    private Array<TextureAtlas.AtlasRegion> jumpForwardRegion;
    private Animation jumpForwardAnimation;

    private TextureAtlas waveAtlas;
    private Array<TextureAtlas.AtlasRegion> waveRegion;
    private Animation waveAnimation;

    private TextureAtlas flyingAtlas;
    private Array<TextureAtlas.AtlasRegion> flyingRegion;
    private Animation flyingAnimation;

    private TextureAtlas landingForwardAtlas;
    private Array<TextureAtlas.AtlasRegion> landingForwardRegion;
    private Animation landingForwardAnimation;

    private TextureAtlas floatingSidewaysAtlas;
    private Array<TextureAtlas.AtlasRegion> floatingSidewaysRegion;
    private Animation floatingSidewaysAnimation;

    private TextureAtlas landingSidewaysAtlas;
    private Array<TextureAtlas.AtlasRegion> landingSidewaysRegion;
    private Animation landingSidewaysAnimation;

    private TextureAtlas walkFastAtlas;
    private Array<TextureAtlas.AtlasRegion>  walkFastRegion;
    private Animation  walkFastAnimation;

    private TextureAtlas runSlowAtlas;
    private Array<TextureAtlas.AtlasRegion> runSlowRegion;
    private Animation runSlowAnimation;

    private TextureAtlas runFastAtlas;
    private Array<TextureAtlas.AtlasRegion> runFastRegion;
    private Animation  runFastAnimation;

    private TextureAtlas jumpSidewaysAtlas;
    private Array<TextureAtlas.AtlasRegion>jumpSidewaysRegion;
    private Animation  jumpSidewaysAnimation;

    //Planet Animation data
    private TextureAtlas planetAtlas = null;
    private TextureAtlas gravityWellAtlas = null;

    /* Constructors */
    public AnimationManager(GameWorld p){
        System.out.println("Animation Constructor");
        parent = p;
        //setupAnimations();
        setupPlanets();
    }

    /* Private Methods */

    private void setupJumpSidewaysAnimation(){
        jumpSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/JumpSideways.pack"));
        jumpSidewaysRegion = jumpSidewaysAtlas.getRegions();
        jumpSidewaysAnimation = new Animation(1/30f, jumpSidewaysRegion);
    }

    private void setupRunSlowAnimation(){
        runSlowAtlas = new TextureAtlas(Gdx.files.internal("data/RunSlow.pack"));
        runSlowRegion = runSlowAtlas.getRegions();
        runSlowAnimation = new Animation(1/30f, runSlowRegion);
    }

    private void setupRunFastAnimation(){
        runFastAtlas = new TextureAtlas(Gdx.files.internal("data/RunFast.pack"));
        runFastRegion = runFastAtlas.getRegions();
        runFastAnimation = new Animation(1/30f, runFastRegion);
    }

    private void setupWalkFastAnimation(){
        walkFastAtlas = new TextureAtlas(Gdx.files.internal("data/WalkFast.pack"));
        walkFastRegion = walkFastAtlas.getRegions();
        walkFastAnimation = new Animation(1/30f, walkFastRegion);
    }

    private void setupLandingSidewaysAnimation(){
        landingSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/LandingSideways.pack"));
        landingSidewaysRegion = landingSidewaysAtlas.getRegions();
        landingSidewaysAnimation = new Animation(1/30f, landingSidewaysRegion);
    }

    private void setupFloatingSidewaysAnimation(){
        floatingSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/FloatingSideways.pack"));
        floatingSidewaysRegion = floatingSidewaysAtlas.getRegions();
        floatingSidewaysAnimation = new Animation(1/30f, floatingSidewaysRegion);
    }

    private void setupLandingForwardAnimation(){
        landingForwardAtlas = new TextureAtlas(Gdx.files.internal("data/LandingForward.pack"));
        landingForwardRegion = landingForwardAtlas.getRegions();
        landingForwardAnimation = new Animation(1/30f, landingForwardRegion);
    }

    /**
     * Loads from file and set's up the standingStillForwards Animation
     */
    private void setupStandingStillForwardsAnimation(){
        standingStillForwardsAtlas = new TextureAtlas(Gdx.files.internal("data/StandingStillForward.pack"));
        standingStillForwardsRegion = standingStillForwardsAtlas.getRegions();
        standingStillForwardsAnimation = new Animation(1/30f, standingStillForwardsRegion);
    }

    private void setupWalkSlowAnimation(){
        walkSlowAtlas = new TextureAtlas(Gdx.files.internal("data/WalkSlow.pack"));
        walkSlowRegion = walkSlowAtlas.getRegions();
        walkSlowAnimation = new Animation(1/30f, walkSlowRegion);
    }

    private void setupStandingStillSidewaysAnimation(){
        standingStillSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/StandingStillSideways.pack"));
        standingStillSidewaysRegion = standingStillSidewaysAtlas.getRegions();
        standingStillSidewaysAnimation = new Animation(1/30f, standingStillSidewaysRegion);
    }

    private void setupWaveAnimation(){
        waveAtlas = new TextureAtlas(Gdx.files.internal("data/Wave.pack"));
        waveRegion = waveAtlas.getRegions();
        waveAnimation = new Animation(1/30f, waveRegion);
    }

    private void setupJumpForwardAnimation(){
        jumpForwardAtlas = new TextureAtlas(Gdx.files.internal("data/JumpForward.pack"));
        jumpForwardRegion =  jumpForwardAtlas.getRegions();
        jumpForwardAnimation = new Animation(1/30f,  jumpForwardRegion);
    }

    private void setupFlyingAnimation(){
        flyingAtlas = new TextureAtlas(Gdx.files.internal("data/Flying.pack"));
        flyingRegion =  flyingAtlas.getRegions();
        flyingAnimation = new Animation(1/30f,  flyingRegion);
    }

    /**
     * Sets the planets atlas
     */
    private void setupPlanets(){
        planetAtlas = new TextureAtlas((Gdx.files.internal("data/Planets.pack")));
        gravityWellAtlas = new TextureAtlas(Gdx.files.internal("data/gravity_Well.txt"));
    }

    /* Public Methods */

    /**
     * Called when game is loaded.
     * Setup all needed animations.
     */
    public void setupAnimations(){
        setupStandingStillForwardsAnimation();
        setupWalkSlowAnimation();
        setupStandingStillSidewaysAnimation();
        setupWaveAnimation();
        setupJumpForwardAnimation();
        setupFlyingAnimation();
        setupLandingForwardAnimation();
        setupFloatingSidewaysAnimation();
        setupLandingSidewaysAnimation();
        setupWalkFastAnimation();
        setupRunSlowAnimation();
        setupRunFastAnimation();
        setupJumpSidewaysAnimation();
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

    public Animation getStandingStillSidewaysAnimation(){
        return standingStillSidewaysAnimation;
    }

    public Animation getWalkSlowAnimation(){
        return walkSlowAnimation;
    }

    public Animation getWaveAnimation(){
        return waveAnimation;
    }

    public Animation getFlyingAnimation(){
        return flyingAnimation;
    }

    public Animation getJumpForwardAnimation(){
        return jumpForwardAnimation;
    }

    public Animation getLandForwardAnimation(){
        return landingForwardAnimation;
    }

    public Animation getFloatSidewaysAnimation(){
        return floatingSidewaysAnimation;
    }

    public Animation getLandSidewaysAnimation(){
        return landingSidewaysAnimation;
    }

    public Animation getWalkFastAnimation(){
        return walkFastAnimation;
    }

    public Animation getRunSlowAnimation(){
        return runSlowAnimation;
    }

    public Animation getRunFastAnimation(){
        return runFastAnimation;
    }

    public Animation getJumpSidewaysAnimation(){
        return jumpSidewaysAnimation;
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
