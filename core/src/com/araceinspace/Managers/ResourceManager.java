package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Keeps track of all the sprite animations and data
 */
public class ResourceManager {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;
    AssetManager assetManager;
    public boolean loadingAssets;

    //Player Sprite Animation Data

    /*
    private TextureAtlas standingStillForwardsAtlas;
    private Array<TextureAtlas.AtlasRegion> standingStillForwardsRegion;
    */
    private Animation standingStillForwardsAnimation;


   // private TextureAtlas walkSlowAtlas;
   // private Array<TextureAtlas.AtlasRegion> walkSlowRegion;
    private Animation walkSlowAnimation;

   // private TextureAtlas standingStillSidewaysAtlas;
   // private Array<TextureAtlas.AtlasRegion> standingStillSidewaysRegion;
    private Animation standingStillSidewaysAnimation;

   // private TextureAtlas jumpForwardAtlas;
   // private Array<TextureAtlas.AtlasRegion> jumpForwardRegion;
    private Animation jumpForwardAnimation;

    //private TextureAtlas waveAtlas;
    //private Array<TextureAtlas.AtlasRegion> waveRegion;
    private Animation waveAnimation;

   // private TextureAtlas flyingAtlas;
   // private Array<TextureAtlas.AtlasRegion> flyingRegion;
    private Animation flyingAnimation;

    //private TextureAtlas landingForwardAtlas;
   // private Array<TextureAtlas.AtlasRegion> landingForwardRegion;
    private Animation landingForwardAnimation;

   // private TextureAtlas floatingSidewaysAtlas;
   // private Array<TextureAtlas.AtlasRegion> floatingSidewaysRegion;
    private Animation floatingSidewaysAnimation;

   // private TextureAtlas landingSidewaysAtlas;
   // private Array<TextureAtlas.AtlasRegion> landingSidewaysRegion;
    private Animation landingSidewaysAnimation;

   // private TextureAtlas walkFastAtlas;
   // private Array<TextureAtlas.AtlasRegion>  walkFastRegion;
    private Animation  walkFastAnimation;

   // private TextureAtlas runSlowAtlas;
   // private Array<TextureAtlas.AtlasRegion> runSlowRegion;
    private Animation runSlowAnimation;

   // private TextureAtlas runFastAtlas;
    //private Array<TextureAtlas.AtlasRegion> runFastRegion;
    private Animation  runFastAnimation;


    //private TextureAtlas jumpSidewaysAtlas;
   // private Array<TextureAtlas.AtlasRegion>jumpSidewaysRegion;
    private Animation  jumpSidewaysAnimation;
    private Animation flyingNoThrustAnimation;
    private Animation explosionAnimation;



    public TextureAtlas heroAtlas;

    //Planet Animation data
    private TextureAtlas planetAtlas = null;
    private TextureAtlas gravityWellAtlas = null;

    /* Constructors */
    public ResourceManager(GameWorld p){
        System.out.println("ResourceManager Constructor");
        parent = p;
        assetManager = new AssetManager();
        loadAssets();
        //setupAnimations();

    }

    public void update(){
       if(assetManager.update()){
           //done loading assets
           System.out.println("Done loading assets");
           parent.initializeManagers();
           loadingAssets = false;
           setupPlanets();
       }

        float progress = assetManager.getProgress();
        System.out.println("AssetLoading Progress : " + progress);
    }



    /* Private Methods */

    private void loadAssets(){
        loadingAssets = true;
        assetManager.load("data/Planets.pack", TextureAtlas.class);
    }

    /**
     * Sets the planets atlas
     */
    private void setupPlanets(){
        planetAtlas = new TextureAtlas((Gdx.files.internal("data/Planets.pack")));
        //planetAtlas = assetManager.get("data/Planets.pack", TextureAtlas.class);
        gravityWellAtlas = new TextureAtlas(Gdx.files.internal("data/gravity_Well.txt"));
    }

    private void setupExplosionAnimation(){
        Array<TextureAtlas.AtlasRegion> explosionRegion = heroAtlas.findRegions("explosion/explosion");
        explosionAnimation = new Animation(1/60f, explosionRegion);

    }

    private void setFlyingNoThrustAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingNoThrustRegion = heroAtlas.findRegions("FlyingNoThrust/FlyingNoThrust");
        flyingNoThrustAnimation = new Animation(1/30f, flyingNoThrustRegion);
    }

    private void setupJumpSidewaysAnimation(){
        /*
        jumpSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/JumpSideways.pack"));
        jumpSidewaysRegion = jumpSidewaysAtlas.getRegions();
        jumpSidewaysAnimation = new Animation(1/30f, jumpSidewaysRegion);
        */
        Array<TextureAtlas.AtlasRegion> jumpSidewaysRegion = heroAtlas.findRegions("JumpSideways/JumpSideways");
        jumpSidewaysAnimation = new Animation(1/30f, jumpSidewaysRegion);
    }

    private void setupRunSlowAnimation(){
        /*runSlowAtlas = new TextureAtlas(Gdx.files.internal("data/RunSlow.pack"));
        runSlowRegion = runSlowAtlas.getRegions();
        runSlowAnimation = new Animation(1/30f, runSlowRegion);*/

        Array<TextureAtlas.AtlasRegion> runSlowRegion = heroAtlas.findRegions("RunSlow/RunSlow");
        runSlowAnimation = new Animation(1/30f, runSlowRegion);
    }

    private void setupRunFastAnimation(){
        /*
        runFastAtlas = new TextureAtlas(Gdx.files.internal("data/RunFast.pack"));
        runFastRegion = runFastAtlas.getRegions();
        runFastAnimation = new Animation(1/30f, runFastRegion);
        */
        Array<TextureAtlas.AtlasRegion> runFastRegion = heroAtlas.findRegions("RunFast/RunFast");
        runFastAnimation = new Animation(1/30f, runFastRegion);
    }

    private void setupWalkFastAnimation(){
        /*
        walkFastAtlas = new TextureAtlas(Gdx.files.internal("data/WalkFast.pack"));
        walkFastRegion = walkFastAtlas.getRegions();
        walkFastAnimation = new Animation(1/30f, walkFastRegion);
        */

        Array<TextureAtlas.AtlasRegion> walkFastRegion = heroAtlas.findRegions("WalkingFast/WalkingFast");
        walkFastAnimation = new Animation(1/30f, walkFastRegion);
    }

    private void setupLandingSidewaysAnimation(){
        /*
        landingSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/LandingSideways.pack"));
        landingSidewaysRegion = landingSidewaysAtlas.getRegions();
        landingSidewaysAnimation = new Animation(1/30f, landingSidewaysRegion);
        */
        Array<TextureAtlas.AtlasRegion> landSidewaysRegion = heroAtlas.findRegions("LandingSideways/LandingSideways");
        landingSidewaysAnimation = new Animation(1/30f, landSidewaysRegion);
    }

    private void setupFloatingSidewaysAnimation(){
        /*
        floatingSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/FloatingSideways.pack"));
        floatingSidewaysRegion = floatingSidewaysAtlas.getRegions();
        floatingSidewaysAnimation = new Animation(1/30f, floatingSidewaysRegion);
        */
        Array<TextureAtlas.AtlasRegion> floatingSidewaysRegion = heroAtlas.findRegions("FloatingSideways/FloatingSideways");
        floatingSidewaysAnimation = new Animation(1/30f, floatingSidewaysRegion);
    }

    private void setupLandingForwardAnimation(){
       /* landingForwardAtlas = new TextureAtlas(Gdx.files.internal("data/LandingForward.pack"));
        landingForwardRegion = landingForwardAtlas.getRegions();
        landingForwardAnimation = new Animation(1/30f, landingForwardRegion);
        */
        Array<TextureAtlas.AtlasRegion> landingForwardRegion = heroAtlas.findRegions("LandingForward/LandingForward");
        landingForwardAnimation = new Animation(1/30f, landingForwardRegion);
    }

    /**
     * Loads from file and set's up the standingStillForwards Animation
     */
    private void setupStandingStillForwardsAnimation(){
        /*
        standingStillForwardsAtlas = new TextureAtlas(Gdx.files.internal("data/StandingStillForward.pack"));
        standingStillForwardsRegion = standingStillForwardsAtlas.getRegions();
        standingStillForwardsAnimation = new Animation(1/30f, standingStillForwardsRegion);
        */
        Array<TextureAtlas.AtlasRegion> standingStillForwardsRegion = heroAtlas.findRegions("StandingStillForward/StandingStillForwar");
        standingStillForwardsAnimation = new Animation(1/30f, standingStillForwardsRegion);


    }

    private void setupWalkSlowAnimation(){
        /*
        walkSlowAtlas = new TextureAtlas(Gdx.files.internal("data/WalkSlow.pack"));
        walkSlowRegion = walkSlowAtlas.getRegions();
        walkSlowAnimation = new Animation(1/30f, walkSlowRegion);
        */

        Array<TextureAtlas.AtlasRegion> walkSlowRegion = heroAtlas.findRegions("WalkingSlow/WalkSlow");
        walkSlowAnimation = new Animation(1/30f, walkSlowRegion);
    }

    private void setupStandingStillSidewaysAnimation(){
        /*
        standingStillSidewaysAtlas = new TextureAtlas(Gdx.files.internal("data/StandingStillSideways.pack"));
        standingStillSidewaysRegion = standingStillSidewaysAtlas.getRegions();
        standingStillSidewaysAnimation = new Animation(1/30f, standingStillSidewaysRegion);
        */

        Array<TextureAtlas.AtlasRegion> standingStillSidewaysRegion = heroAtlas.findRegions("StandingStillSideways/StandingStillSideways");
        standingStillSidewaysAnimation = new Animation(1/30f, standingStillSidewaysRegion);
    }

    private void setupWaveAnimation(){
        /*waveAtlas = new TextureAtlas(Gdx.files.internal("data/Wave.pack"));
        waveRegion = waveAtlas.getRegions();
        waveAnimation = new Animation(1/30f, waveRegion);*/

        Array<TextureAtlas.AtlasRegion> waveRegion = heroAtlas.findRegions("Wave/Wave");
        waveAnimation = new Animation(1/30f, waveRegion);
    }

    private void setupJumpForwardAnimation(){
        /*
        jumpForwardAtlas = new TextureAtlas(Gdx.files.internal("data/JumpForward.pack"));
        jumpForwardRegion =  jumpForwardAtlas.getRegions();
        jumpForwardAnimation = new Animation(1/30f,  jumpForwardRegion);
        */
        Array<TextureAtlas.AtlasRegion> jumpForwardRegion = heroAtlas.findRegions("JumpForward/JumpForward");
        jumpForwardAnimation = new Animation(1/30f, jumpForwardRegion);
    }

    private void setupFlyingAnimation(){
        /*
        flyingAtlas = new TextureAtlas(Gdx.files.internal("data/Flying.pack"));
        flyingRegion =  flyingAtlas.getRegions();
        flyingAnimation = new Animation(1/30f,  flyingRegion);
        */
        Array<TextureAtlas.AtlasRegion> flyingRegion = heroAtlas.findRegions("Flying/Flying");
        flyingAnimation = new Animation(1/30f, flyingRegion);
    }



    /* Public Methods */

    /**
     * Called when game is loaded.
     * Setup all needed animations.
     */
    public void setupAnimations(){
        heroAtlas = new TextureAtlas(Gdx.files.internal("data/HeroAnimations.atlas"));

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
        setFlyingNoThrustAnimation();
        setupExplosionAnimation();
    }


   /* public TextureAtlas getStandingStillForwardsAtlas() {
        return standingStillForwardsAtlas;
    }

    public Array<TextureAtlas.AtlasRegion> getStandingStillForwardsRegion() {
        return standingStillForwardsRegion;
    }*/

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

    public Animation getFlyingNoThrustAnimation(){
        return flyingNoThrustAnimation;
    }

    public Animation getExploadingAnimation(){
        return explosionAnimation;
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

    public TextureAtlas getHeroAtlas(){
        return heroAtlas;
    }

    public void setGravityWellAtlas(TextureAtlas gravityWellAtlas) {
        this.gravityWellAtlas = gravityWellAtlas;
    }
}
