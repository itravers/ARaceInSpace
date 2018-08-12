package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.araceinspace.misc.Animation;
import com.araceinspace.misc.FreetypeFontLoader;
import com.araceinspace.misc.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

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

    private Animation landingForwardAnimation;
    private Animation floatingSidewaysAnimation;
    private Animation landingSidewaysAnimation;
    private Animation  walkFastAnimation;
    private Animation runSlowAnimation;
    private Animation  runFastAnimation;
    private Animation  jumpSidewaysAnimation;

    private Animation explosionAnimation;

    //flying Animations
    private Animation flyingRightAnimation;
    private Animation flyingLeftAnimation;
    private Animation flyingNoThrustAnimation;
    private Animation flyingAnimation;
    private Animation flyingBackwardAnimation;
    private Animation flyingLeftNoThrustAnimation;
    private Animation flyingRightNoThrustAnimation;
    private Animation flyingRightBackAnimation;
    private Animation flyingLeftBackAnimation;

    private Animation planet01Animation;
    private Animation planet02Animation;
    private Animation moonAnimation;
    private Animation gravityWellAnimation;

    private com.badlogic.gdx.graphics.g2d.Animation loadingSpinnerAnimation;

    /* Font Data */
    public BitmapFont Font60;
    public BitmapFont Font48;
    public BitmapFont Font36;
    public BitmapFont Font24;
    public BitmapFont Font20;


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

    public Music everybodyDies;

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
       // assetManager.load("data/Planets.pack", TextureAtlas.class);
        //assetManager.load("data/gravity_Well.txt", TextureAtlas.class);
        assetManager.load("data/HeroAnimations.atlas", TextureAtlas.class);
        assetManager.load("aris_uiskin.atlas", TextureAtlas.class);
        assetManager.load("aris_uiskin.json", Skin.class, new SkinLoader.SkinParameter("aris_uiskin.atlas"));
        assetManager.load("data/beethoven7th.mp3", Music.class);
        assetManager.load("data/RisingSun.ogg", Music.class);
        assetManager.load("data/hearthAndHills.ogg", Music.class);
        assetManager.load("data/everybodydies.mp3", Music.class);

        //setup stuff to load fonts
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter size48Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size48Params.fontFileName = "Font_Destroy.ttf";
        size48Params.fontParameters.size = 48;
        assetManager.load("Font48.ttf", BitmapFont.class, size48Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size24Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size24Params.fontFileName = "Font_Destroy.ttf";
        size24Params.fontParameters.size = 24;
        assetManager.load("Font24.ttf", BitmapFont.class, size24Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size36Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size36Params.fontFileName = "Font_Destroy.ttf";
        size36Params.fontParameters.size = 36;
        assetManager.load("Font36.ttf", BitmapFont.class, size36Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size20Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size20Params.fontFileName = "Font_Destroy.ttf";
        size20Params.fontParameters.size = 20;
        assetManager.load("Font20.ttf", BitmapFont.class, size20Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size60Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size60Params.fontFileName = "Font_Destroy.ttf";
        size60Params.fontParameters.size = 60;
        assetManager.load("Font60.ttf", BitmapFont.class, size60Params);
    }

    /**
     * Sets the planets atlas
     */
    private void setupPlanets(){
       // planetAtlas = new TextureAtlas((Gdx.files.internal("data/Planets.pack")));
       // planetAtlas = assetManager.get("data/Planets.pack", TextureAtlas.class);
        //gravityWellAtlas = new TextureAtlas(Gdx.files.internal("data/gravity_Well.txt"));

       // gravityWellAtlas = assetManager.get("data/gravity_Well.txt", TextureAtlas.class);
    }

    private void setupExplosionAnimation(){
        Array<TextureAtlas.AtlasRegion> explosionRegion = heroAtlas.findRegions("explosion/explosion");
        explosionAnimation = new Animation(1/60f, explosionRegion, "explosionAnimation");
    }

    private void setupPlanetAnimations(){
        Array<TextureAtlas.AtlasRegion> planet01Region = heroAtlas.findRegions("Planets/Planet 01");
        planet01Animation = new Animation(1/60f, planet01Region, "planet01Animation");

        Array<TextureAtlas.AtlasRegion> planet02Region = heroAtlas.findRegions("Planets/Planet 02");
        planet02Animation = new Animation(1/60f, planet02Region, "planet02Animation");

        Array<TextureAtlas.AtlasRegion> moonRegion = heroAtlas.findRegions("Planets/Moon");
        moonAnimation = new Animation(1/60f, moonRegion, "moonAnimation");
    }

    private void setupGravityWellAnimation(){
        Array<TextureAtlas.AtlasRegion> gravity_Well = heroAtlas.findRegions("Planets/gravity_Well");
        gravityWellAnimation = new Animation(1/60f, gravity_Well, "gravityWellAnimation");
    }

    private void setFlyingNoThrustAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingNoThrustRegion = heroAtlas.findRegions("FlyingNoThrust/FlyingNoThrus");
        flyingNoThrustAnimation = new Animation(1/30f, flyingNoThrustRegion, "flyingNoThrustAnimation");
    }

    private void setupJumpSidewaysAnimation(){
        Array<TextureAtlas.AtlasRegion> jumpSidewaysRegion = heroAtlas.findRegions("JumpSideways/JumpSideway");
        jumpSidewaysAnimation = new Animation(1/30f, jumpSidewaysRegion, "jumpSidewaysAnimation");
    }

    private void setupRunSlowAnimation(){
        Array<TextureAtlas.AtlasRegion> runSlowRegion = heroAtlas.findRegions("RunSlow/RunSlo");
        runSlowAnimation = new Animation(1/30f, runSlowRegion, "runSlowAnimation");
    }

    private void setupRunFastAnimation(){
        Array<TextureAtlas.AtlasRegion> runFastRegion = heroAtlas.findRegions("RunFast/RunFas");
        runFastAnimation = new Animation(1/30f, runFastRegion, "runFastAnimation");
    }

    private void setupWalkFastAnimation(){
        Array<TextureAtlas.AtlasRegion> walkFastRegion = heroAtlas.findRegions("WalkingFast/WalkingFas");
        walkFastAnimation = new Animation(1/30f, walkFastRegion, "walkFastAnimation");
    }

    private void setupLandingSidewaysAnimation(){
        Array<TextureAtlas.AtlasRegion> landSidewaysRegion = heroAtlas.findRegions("LandingSideways/LandingSideway");
        landingSidewaysAnimation = new Animation(1/30f, landSidewaysRegion, "landingSidewaysAnimation");
    }

    private void setupFloatingSidewaysAnimation(){
        Array<TextureAtlas.AtlasRegion> floatingSidewaysRegion = heroAtlas.findRegions("FloatingSideways/FloatingSideway");
        floatingSidewaysAnimation = new Animation(1/30f, floatingSidewaysRegion, "floatingSidewaysAnimation");
    }

    private void setupLandingForwardAnimation(){
        Array<TextureAtlas.AtlasRegion> landingForwardRegion = heroAtlas.findRegions("LandingForward/LandingForwar");
        landingForwardAnimation = new Animation(1/30f, landingForwardRegion, "landingForwardAnimation");
    }

    private void setupFlyingLeftAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingLeftRegion = heroAtlas.findRegions("FlyingLeft/FlyingLeft");
        flyingLeftAnimation = new Animation(1/30f, flyingLeftRegion, "flyingLeftAnimation");
    }

    private void setupFlyingBackwardAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingBackwardRegion = heroAtlas.findRegions("FlyingBackward/FlyingBackwar");
        flyingBackwardAnimation = new Animation(1/30f, flyingBackwardRegion, "flyingBackwardAnimation");
    }

    private void setupFlyingLeftNoThrustAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingLeftNoThrustRegion = heroAtlas.findRegions("FlyingLeftNoThrust/FlyingLeftNoThrus");
        flyingLeftNoThrustAnimation = new Animation(1/30f, flyingLeftNoThrustRegion, "flyingLeftNoThrustAnimation");
    }

    private void setupFlyingRightNoThrustAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingRightNoThrustRegion = heroAtlas.findRegions("FlyingRightNoThrust/FlyingRightNoThrus");
        flyingRightNoThrustAnimation = new Animation(1/30f, flyingRightNoThrustRegion, "flyingRightNoThrustAnimation");
    }

    private void setupFlyingLeftBackAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingLeftBackRegion = heroAtlas.findRegions("FlyingLeftBack/FlyingLeftBac");
        flyingLeftBackAnimation = new Animation(1/30f, flyingLeftBackRegion, "flyingLeftBackAnimation");
    }

    private void setupFlyingRightBackAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingRightBackRegion = heroAtlas.findRegions("FlyingRightBack/FlyingRightBac");
        flyingRightBackAnimation = new Animation(1/30f, flyingRightBackRegion, "flyingRightBackAnimation");
    }

    private void setupFlyingRightAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingRightRegion = heroAtlas.findRegions("FlyingRight/FlyingRigh");
        flyingRightAnimation = new Animation(1/30f, flyingRightRegion, "flyingRightAnimation");
    }

    /**
     * Loads from file and set's up the standingStillForwards Animation
     */
    private void setupStandingStillForwardsAnimation(){
        Array<TextureAtlas.AtlasRegion> standingStillForwardsRegion = heroAtlas.findRegions("StandingStillForward/StandingStillForwar");
        standingStillForwardsAnimation = new Animation(1/30f, standingStillForwardsRegion, "standingStillForwardsAnimation");
    }

    private void setupWalkSlowAnimation(){
        Array<TextureAtlas.AtlasRegion> walkSlowRegion = heroAtlas.findRegions("WalkingSlow/WalkingSlo");
        walkSlowAnimation = new Animation(1/30f, walkSlowRegion, "walkSlowAnimation");
    }

    private void setupStandingStillSidewaysAnimation(){
        Array<TextureAtlas.AtlasRegion> standingStillSidewaysRegion = heroAtlas.findRegions("StandingStillSideways/StandingStillSideway");
        standingStillSidewaysAnimation = new Animation(1/30f, standingStillSidewaysRegion, "standingStillSidewaysAnimation");
    }

    private void setupWaveAnimation(){
        Array<TextureAtlas.AtlasRegion> waveRegion = heroAtlas.findRegions("Wave/Wav");
        waveAnimation = new Animation(1/30f, waveRegion, "waveAnimation");
    }

    private void setupJumpForwardAnimation(){
        Array<TextureAtlas.AtlasRegion> jumpForwardRegion = heroAtlas.findRegions("JumpForward/JumpForwar");
        jumpForwardAnimation = new Animation(1/30f, jumpForwardRegion, "jumpForwardAnimation");
    }

    private void setupFlyingAnimation(){
        Array<TextureAtlas.AtlasRegion> flyingRegion = heroAtlas.findRegions("Flying/Flyin");
        flyingAnimation = new Animation(1/30f, flyingRegion, "flyingAnimation");
    }

    private void setupLoadingSpinnerAnimation() {
        loadingSpinnerAnimation = GifDecoder.loadGIFAnimation(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP, Gdx.files.internal("loading.gif").read());


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
            setupFonts();
            loadingAssets = false;
            parent.initializeManagers();//must come after setupAnimations, and setupPlanets
        }

        progress = assetManager.getProgress();
        //System.out.println("AssetLoading Progress : " + progress);
    }

    private void setupFonts(){
        Font60 = assetManager.get("Font60.ttf", BitmapFont.class);
        Font48 = assetManager.get("Font48.ttf", BitmapFont.class);
        Font36 = assetManager.get("Font36.ttf", BitmapFont.class);
        Font24 = assetManager.get("Font24.ttf", BitmapFont.class);
        Font20 = assetManager.get("Font20.ttf", BitmapFont.class);
    }

    private void setupSounds(){
        beethovens7th = assetManager.get("data/beethoven7th.mp3", Music.class);
        risingSun = assetManager.get("data/RisingSun.ogg", Music.class);
        hearthAndHills = assetManager.get("data/hearthAndHills.ogg", Music.class);
        everybodyDies = assetManager.get("data/everybodydies.mp3", Music.class);

        //everybodyDies.setLooping(0, false);
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

        setupLoadingSpinnerAnimation();

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
        setupPlanetAnimations();
        setupGravityWellAnimation();
        setupFlyingLeftAnimation();
        setupFlyingRightAnimation();
        setupFlyingLeftBackAnimation();
        setupFlyingRightBackAnimation();
        setupFlyingLeftNoThrustAnimation();
        setupFlyingRightNoThrustAnimation();
        setupFlyingBackwardAnimation();

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

    public Animation getFlyingRightAnimation(){
        return flyingRightAnimation;
    }

    public Animation getFlyingLeftAnimation(){
        return flyingLeftAnimation;
    }

    public Animation getFlyingBackwardAnimation(){
        return flyingBackwardAnimation;
    }

    public Animation getFlyingRightNoThrustAnimation(){
        return flyingRightNoThrustAnimation;
    }

    public Animation getFlyingLeftNoThrustAnimation(){
        return flyingLeftNoThrustAnimation;
    }

    public Animation getFlyingLeftBackAnimation(){
        return flyingLeftBackAnimation;
    }

    public Animation getFlyingRightBackAnimation(){
        return flyingRightBackAnimation;
    }

    public Animation getPlanet01Animation(){
        return planet01Animation;
    }
    public Animation getPlanet02Animation(){
        return planet02Animation;
    }
    public Animation getMoonAnimation(){
        return moonAnimation;
    }
    public Animation getGravityWellAnimation() { return gravityWellAnimation; }

    public TextureAtlas getPlanetAtlas(){
        if(planetAtlas == null)setupPlanets();//incase animation manager constructed after level manager
        return planetAtlas;
    }

    public void setPlanetAtlas(TextureAtlas p){
        planetAtlas = p;
    }

    public Animation getPlanetAnimationFromName(String atlasName){
        Array<TextureAtlas.AtlasRegion> planetRegion = getPlanetAtlas().findRegions(atlasName);
        Animation planetRotateAnimation = new Animation(1/2f, planetRegion, "planetRotateAnimation");
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

    public Animation getPlanetAnimationByAtlasName(String atlasName){
        if(atlasName.equals("Planet 01")){
            return getPlanet01Animation();
        }else if(atlasName.equals("Planet 02")){
            return getPlanet02Animation();
        }else{
            return getMoonAnimation();
        }
    }

    public com.badlogic.gdx.graphics.g2d.Animation getLoadingSpinnerAnimation(){
        return loadingSpinnerAnimation;
    }


}
