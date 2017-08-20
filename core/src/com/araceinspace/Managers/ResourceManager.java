package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Isaac Assegai on 7/11/17.
 * Loads all resources at the start of the game.
 * Uses an Asset Manager to asynchronously load the assets.
 * Keeps track of the progress of loading so LOADINGScreen
 * can display a progress bar.
 */
public class ResourceManager {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;
    AssetManager assetManager;
    public boolean loadingAssets;
    public float progress = 0;

    //Player Sprite Animation Data

    private Animation standingStillForwardsAnimation;
    private Animation walkSlowAnimation;
    private Animation standingStillSidewaysAnimation;
    private Animation jumpForwardAnimation;
    private Animation waveAnimation;
    private Animation flyingAnimation;
    private Animation landingForwardAnimation;
    private Animation floatingSidewaysAnimation;
    private Animation landingSidewaysAnimation;
    private Animation  walkFastAnimation;
    private Animation runSlowAnimation;
    private Animation  runFastAnimation;
    private Animation  jumpSidewaysAnimation;
    private Animation flyingNoThrustAnimation;
    private Animation explosionAnimation;


    /** Player Atlas */
    public TextureAtlas heroAtlas;

    //Planet Animation data
    private TextureAtlas planetAtlas = null;
    private TextureAtlas gravityWellAtlas = null;

    //Game Skin
    Skin skin;

    //Game Sounds
    public Music beethovens7th;
    public Music risingSun;
    public Music hearthAndHills;

    /* Constructors */
    public ResourceManager(GameWorld p){
        System.out.println("ResourceManager Constructor");
        parent = p;
        assetManager = new AssetManager();
        loadAssets();
        //setupAnimations();

    }


    /* Private Methods */

    private void loadAssets(){
        loadingAssets = true;
        assetManager.load("data/Planets.pack", TextureAtlas.class);
        assetManager.load("data/gravity_Well.txt", TextureAtlas.class);
        assetManager.load("data/HeroAnimations.atlas", TextureAtlas.class);
        assetManager.load("aris_uiskin.atlas", TextureAtlas.class);
        assetManager.load("aris_uiskin.json", Skin.class, new SkinLoader.SkinParameter("aris_uiskin.atlas"));
        assetManager.load("data/beethoven7th.mp3", Music.class);
        assetManager.load("data/RisingSun.ogg", Music.class);
        assetManager.load("data/hearthAndHills.ogg", Music.class);
    }

    /**
     * Sets the planets atlas
     */
    private void setupPlanets(){
       // planetAtlas = new TextureAtlas((Gdx.files.internal("data/Planets.pack")));
        planetAtlas = assetManager.get("data/Planets.pack", TextureAtlas.class);
        //gravityWellAtlas = new TextureAtlas(Gdx.files.internal("data/gravity_Well.txt"));
        gravityWellAtlas = assetManager.get("data/gravity_Well.txt", TextureAtlas.class);
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
        Array<TextureAtlas.AtlasRegion> jumpSidewaysRegion = heroAtlas.findRegions("JumpSideways/JumpSideways");
        jumpSidewaysAnimation = new Animation(1/30f, jumpSidewaysRegion);
    }

    private void setupRunSlowAnimation(){
        Array<TextureAtlas.AtlasRegion> runSlowRegion = heroAtlas.findRegions("RunSlow/RunSlow");
        runSlowAnimation = new Animation(1/30f, runSlowRegion);
    }

    private void setupRunFastAnimation(){
        Array<TextureAtlas.AtlasRegion> runFastRegion = heroAtlas.findRegions("RunFast/RunFast");
        runFastAnimation = new Animation(1/30f, runFastRegion);
    }

    private void setupWalkFastAnimation(){
        Array<TextureAtlas.AtlasRegion> walkFastRegion = heroAtlas.findRegions("WalkingFast/WalkingFast");
        walkFastAnimation = new Animation(1/30f, walkFastRegion);
    }

    private void setupLandingSidewaysAnimation(){
        Array<TextureAtlas.AtlasRegion> landSidewaysRegion = heroAtlas.findRegions("LandingSideways/LandingSideways");
        landingSidewaysAnimation = new Animation(1/30f, landSidewaysRegion);
    }

    private void setupFloatingSidewaysAnimation(){
        Array<TextureAtlas.AtlasRegion> floatingSidewaysRegion = heroAtlas.findRegions("FloatingSideways/FloatingSideways");
        floatingSidewaysAnimation = new Animation(1/30f, floatingSidewaysRegion);
    }

    private void setupLandingForwardAnimation(){
        Array<TextureAtlas.AtlasRegion> landingForwardRegion = heroAtlas.findRegions("LandingForward/LandingForward");
        landingForwardAnimation = new Animation(1/30f, landingForwardRegion);
    }

    /**
     * Loads from file and set's up the standingStillForwards Animation
     */
    private void setupStandingStillForwardsAnimation(){
        Array<TextureAtlas.AtlasRegion> standingStillForwardsRegion = heroAtlas.findRegions("StandingStillForward/StandingStillForwar");
        standingStillForwardsAnimation = new Animation(1/30f, standingStillForwardsRegion);
    }

    private void setupWalkSlowAnimation(){
        Array<TextureAtlas.AtlasRegion> walkSlowRegion = heroAtlas.findRegions("WalkingSlow/WalkSlow");
        walkSlowAnimation = new Animation(1/30f, walkSlowRegion);
    }

    private void setupStandingStillSidewaysAnimation(){
        Array<TextureAtlas.AtlasRegion> standingStillSidewaysRegion = heroAtlas.findRegions("StandingStillSideways/StandingStillSideways");
        standingStillSidewaysAnimation = new Animation(1/30f, standingStillSidewaysRegion);
    }

    private void setupWaveAnimation(){
        Array<TextureAtlas.AtlasRegion> waveRegion = heroAtlas.findRegions("Wave/Wave");
        waveAnimation = new Animation(1/30f, waveRegion);
    }

    private void setupJumpForwardAnimation(){
        Array<TextureAtlas.AtlasRegion> jumpForwardRegion = heroAtlas.findRegions("JumpForward/JumpForward");
        jumpForwardAnimation = new Animation(1/30f, jumpForwardRegion);
    }

    private void setupFlyingAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingRegion = heroAtlas.findRegions("Flying/Flying");
        flyingAnimation = new Animation(1/30f, flyingRegion);
    }


    /* Public Methods */

    /**
     * Called whenever loadingAssets = true
     * by the GameWorld update() method.
     * This loads Assets until done, then changes loadingAssets to false
     */
    public void update(){
        if(assetManager.update()){
            //done loading assets
            System.out.println("Done loading assets");


            setupPlanets();
            setupAnimations();
            setupSkin();
            setupSounds();
            loadingAssets = false;
            parent.initializeManagers();//must come after setupAnimations, and setupPlanets
        }

        progress = assetManager.getProgress();
        //System.out.println("AssetLoading Progress : " + progress);
    }

    private void setupSounds(){
        beethovens7th = assetManager.get("data/beethoven7th.mp3", Music.class);
        risingSun = assetManager.get("data/RisingSun.ogg", Music.class);
        hearthAndHills = assetManager.get("data/hearthAndHills.ogg", Music.class);
       // assetManager.finishLoading();
    }

    private void setupSkin(){
        skin = assetManager.get("aris_uiskin.json", Skin.class);
    }

    /**
     * Called when game is loaded.
     * Setup all needed animations.
     */
    public void setupAnimations(){
        //heroAtlas = new TextureAtlas(Gdx.files.internal("data/HeroAnimations.atlas"));
        heroAtlas = assetManager.get("data/HeroAnimations.atlas", TextureAtlas.class);

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

    public Skin getSkin(){
        return skin;
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
