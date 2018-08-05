package com.araceinspace.Screens;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by The screen to be rendered when the GAME_STATE is INGAME on 7/16/17.
 */
public class INGAMEScreen extends Screen implements EventSender{

    /* Static Variables */
    int spacer = 25;

    /* Field Variables & Objects */
    private OrthCamera backgroundCamera;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private Matrix4 debugMatrix;
    private SpriteBatch backgroundBatch;
    private BitmapFont font;
    private ClickListener menuButtonListener;
    private ClickListener rewardButtonListener;
    private Skin skin;
    private OrthCamera menuCamera;
    private OrthCamera shapeCamera; // need this because other cameras zoom

    private Touchpad touchPad;
    private ImageButton boostButton;
    private InputListener boostButtonListener;

    private Image velocityIndicatorOutline;
    private TextureRegion velocityIndicatorOutlineTexture;
    private TextureRegion velocityIndicatorTexture;
    private TextureRegion velocityIndicatorGreenTexture;
    private TextureRegion velocityIndicatorRedTexture;

    private TextureRegion healthMeterEmpty;
    private TextureRegion healthMeterBlue;
    private TextureRegion healthMeterRed;

    private TextureRegion boostMeterEmpty;
    private TextureRegion boostMeterBlue;
    private TextureRegion boostMeterRed;

    private TextureRegion dirIndicatorRed;
    private TextureRegion dirIndicatorGreen;
    private TextureRegion dirIndicatorWhite;
    private TextureRegion dirIndicatorYellow;

    private TextureRegion indicatorLegend;


    private SpriteBatch healthBatch;
    Rectangle velocityClip;
    Rectangle healthClip;
    Rectangle boostClip;
    Rectangle indicatorClip;
    Rectangle scissors;

    //Vectors
    Vector2 gravityVector;
    Vector2 renderGoalMiddleOfGoal;
    Vector2 tmp;
    Vector2 tmp2;
    Vector2 O1;
    Vector2 O2;
    Vector2 O3;
    Vector2 O4;
    Vector2 O5;
    Vector2 O6;
    Vector2 O7;
    Vector2 O8;
    Vector2 L1;
    Vector2 L2;
    Vector2 L3;
    Vector2 L4;
    Vector2 L5;
    Vector2 O;
    Vector2 G;
    float rotation;

    Label boostLabel;
    Label healthLabel;
    Label speedLabel;

    private SimpleMovingAverage runningAvgGravity;

    MonetizationController monetizationController;

    /* Constructors */

    public INGAMEScreen(RenderManager p) {
        super(p);

        // parent.parent.elapsedTime = 0;//reset elapsed time
        Touchpad t;
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundBatch.dispose();
        font.dispose();
        debugRenderer.dispose();
        // skin.dispose();
    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return backgroundCamera;
    }

    @Override
    public void setup() {
        // System.out.println("SETUP INGAME SCREEN");
        parent.resetFrameNum();
        monetizationController= parent.monetizationController;
        monetizationController.hideBannerAd();
        // stage = new Stage(menuCamera)
        setupVectors();
        font = new BitmapFont();
        backgroundCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch = new SpriteBatch();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);

        healthBatch = new SpriteBatch();
        healthBatch.setProjectionMatrix(menuCamera.combined);

        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);
        setupStage();
        parent.parent.elapsedTime = 0;
        parent.parent.renderManager.resetFrameNum();
    }


    /* Private Methods */

    private void setupVectors(){
        renderGoalMiddleOfGoal = new Vector2();
        tmp = new Vector2();
        tmp2 = new Vector2();
        O1 = new Vector2();
        O2 = new Vector2();
        O3 = new Vector2();
        O4 = new Vector2();
        O5 = new Vector2();
        O6 = new Vector2();
        O7 = new Vector2();
        O8 = new Vector2();
        L1 = new Vector2();
        L2 = new Vector2();
        L3 = new Vector2();
        L4 = new Vector2();
        L5 = new Vector2();
        O = new Vector2();
        G = new Vector2();
        runningAvgGravity = new SimpleMovingAverage(10);
    }

    private void setupStage(){
        viewport = new ScreenViewport(menuCamera);
        stage = new Stage(viewport, batch);
        skin = parent.parent.resourceManager.getSkin();

        boostLabel = new Label("BOOST", skin, "optional");
        Label.LabelStyle style = boostLabel.getStyle();
        style.font = parent.parent.resourceManager.Font20;
        boostLabel.setStyle(style);
        boostLabel.setPosition((viewport.getScreenWidth()/6)-boostLabel.getWidth()/2, viewport.getScreenHeight()-boostLabel.getHeight());

        healthLabel = new Label("HEALTH", skin, "optional");
        Label.LabelStyle  style2 = healthLabel.getStyle();
        style2.font = parent.parent.resourceManager.Font20;
        healthLabel.setStyle(style2);
        healthLabel.setPosition(5*(viewport.getScreenWidth()/6)-healthLabel.getWidth()/2, viewport.getScreenHeight()-healthLabel.getHeight());

        speedLabel = new Label("SPEED", skin, "optional");
        Label.LabelStyle   style3 = speedLabel.getStyle();
        style3.font = parent.parent.resourceManager.Font20;
        speedLabel.setStyle(style3);
        speedLabel.setPosition((viewport.getScreenWidth()/2)-speedLabel.getWidth()/2, viewport.getScreenHeight()-speedLabel.getHeight()*1.5f);

        touchPad = new Touchpad(10, skin, "default");
        touchPad.setBounds(15, 15, Gdx.graphics.getWidth()/2-20,Gdx.graphics.getWidth()/2-20);
        touchPad.getStyle().knob.setMinWidth(touchPad.getWidth()/4);
        touchPad.getStyle().knob.setMinHeight(touchPad.getHeight()/4);
        touchPad.addListener(parent.parent.inputManager);
        touchPad.setDeadzone(touchPad.getWidth()/5);

        boostButton = new ImageButton(skin, "boostButton");
        boostButton.setWidth(Gdx.graphics.getWidth()/2-20);
        boostButton.setHeight(Gdx.graphics.getWidth()/2-20);
        boostButton.setBounds(Gdx.graphics.getWidth()-15-boostButton.getWidth(), 15, boostButton.getWidth(), boostButton.getHeight());

        boostButtonListener = new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                sendEvent(new Event(Event.TYPE.INPUT, "PlayerInput", GameInput.BOOST_PRESSED));
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                sendEvent(new Event(Event.TYPE.INPUT, "PlayerInput", GameInput.BOOST_RELEASED));
            }
        };

        boostButton.addListener(boostButtonListener);


        Table mainTable;
        Table headerTable;
        ImageButton menuButton;
        ImageButton rewardButton;

        mainTable = new Table();
        mainTable.setDebug(parent.parent.devMode);
        mainTable.setWidth(viewport.getScreenWidth());
        mainTable.align(Align.center | Align.top);
        mainTable.setPosition(0, viewport.getScreenHeight());

        rewardButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                monetizationController.showRewardAd();
                monetizationController.loadRewardAd();
            }
        };

        menuButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.MENU);
            }
        };

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.setDebug(parent.parent.devMode);
        rewardButton.addListener(rewardButtonListener);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(parent.parent.devMode);
        menuButton.addListener(menuButtonListener);

        headerTable = new Table();
        headerTable.setDebug(parent.parent.devMode);
        headerTable.align(Align.center | Align.top);
        mainTable.add(headerTable).fill().expandX();
        stage.addActor(mainTable);
        stage.addActor(touchPad);
        stage.addActor(boostButton);


        //Direction Indicators
        Array<TextureAtlas.AtlasRegion> dirIndicatorRedNewRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/indicator-red-new");
        dirIndicatorRed = dirIndicatorRedNewRegion.get(0);

        Array<TextureAtlas.AtlasRegion> dirIndicatorGreenNewRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/indicator-green-new");
        dirIndicatorGreen = dirIndicatorGreenNewRegion.get(0);

        Array<TextureAtlas.AtlasRegion> dirIndicatorWhiteNewRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/indicator-white-new");
        dirIndicatorWhite = dirIndicatorWhiteNewRegion.get(0);

        Array<TextureAtlas.AtlasRegion> dirIndicatorYellowNewRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/indicator-yellow-new");
        dirIndicatorYellow = dirIndicatorYellowNewRegion.get(0);


        Array<TextureAtlas.AtlasRegion> ghostIndicatorOutlineRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_empty_text");
        velocityIndicatorOutlineTexture = ghostIndicatorOutlineRegion.get(0);

        velocityIndicatorOutline = new Image(velocityIndicatorOutlineTexture);
        // ghostIndicatorOutline.setPosition((viewport.getScreenWidth()/2)-ghostIndicatorOutline.getWidth()/2, viewport.getScreenHeight()-ghostIndicatorOutline.getHeight());
        velocityIndicatorOutline.setPosition(-velocityIndicatorOutline.getWidth()/2,(viewport.getScreenHeight()/2)-velocityIndicatorOutline.getHeight());

        Array<TextureAtlas.AtlasRegion> ghostIndicatorRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_fill_text");
        velocityIndicatorTexture = ghostIndicatorRegion.get(0);

        Array<TextureAtlas.AtlasRegion> ghostIndicatorGreenRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_fill_green_text");
        velocityIndicatorGreenTexture = ghostIndicatorGreenRegion.get(0);

        Array<TextureAtlas.AtlasRegion> ghostIndicatorRedRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_red_text");
        velocityIndicatorRedTexture = ghostIndicatorRedRegion.get(0);

        Array<TextureAtlas.AtlasRegion> indicatorLegendRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/indicator_legend");
        indicatorLegend = indicatorLegendRegion.get(0);


        float gclipWidth = velocityIndicatorOutline.getWidth()+4;
        float gclipX = (viewport.getScreenWidth()/2)-gclipWidth/2;
        float gclipHeight = 50;
        float gclipY = viewport.getScreenHeight()-gclipHeight;
        velocityClip = new Rectangle(gclipX,gclipY, gclipWidth, gclipHeight);

        indicatorClip = new Rectangle();


        Array<TextureAtlas.AtlasRegion> healthMeterEmptyRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_empty_text");
        healthMeterEmpty = healthMeterEmptyRegion.get(0);
        healthMeterEmpty.setRegionWidth(2+viewport.getScreenWidth()/2);

        // healthMeterEmpty

        Array<TextureAtlas.AtlasRegion> healthMeterBlueRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_blue_text");
        healthMeterBlue = healthMeterBlueRegion.get(0);
        healthMeterBlue.setRegionWidth(2+viewport.getScreenWidth()/2);

        Array<TextureAtlas.AtlasRegion> healthMeterRedRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_red_text");
        healthMeterRed = healthMeterRedRegion.get(0);
        healthMeterRed.setRegionWidth(2+viewport.getScreenWidth()/2);

        Array<TextureAtlas.AtlasRegion> boostMeterEmptyRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/boostMeter_empty_text");
        boostMeterEmpty = boostMeterEmptyRegion.get(0);
        //boostMeterEmpty.flip(true, false);

        Array<TextureAtlas.AtlasRegion> boostMeterBlueRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/boostMeter_blue_text");
        boostMeterBlue = boostMeterBlueRegion.get(0);
        boostMeterBlue.flip(false, false);

        Array<TextureAtlas.AtlasRegion> boostMeterRedRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/boostMeter_red_text");
        boostMeterRed = boostMeterRedRegion.get(0);
        boostMeterRed.flip(false, false);

        float hclipWidth = getHealthMeterWidth();
        float hclipX = (viewport.getScreenWidth()/2);
        float hclipHeight = healthMeterEmpty.getRegionHeight();
        float hclipY = viewport.getScreenHeight()-healthMeterEmpty.getRegionHeight();
        healthClip = new Rectangle(hclipX, hclipY, hclipWidth, hclipHeight);

        float bclipWidth =  getBoostMeterWidth();
        float bclipX =  (viewport.getScreenWidth()/2)-bclipWidth;
        float bclipHeight = 100;
        float bclipY = viewport.getScreenHeight()-boostMeterEmpty.getRegionHeight();
        boostClip = new Rectangle(bclipX, bclipY, bclipWidth, bclipHeight);

        scissors = new Rectangle();
        parent.parent.inputManager.addInputProcessor(stage);

    }

    private void renderVersion(SpriteBatch batch){
        font.setColor(Color.WHITE);
        font.draw(batch, "Version: " +((ARaceInSpace)parent.parent.parent).version, parent.parent.levelManager.getPlayer().getX(),parent.parent.levelManager.getPlayer().getY());
    }

    /* Public Methods */

    @Override
    public void render(float timeElapsed) {
        boostLabel.setPosition((viewport.getScreenWidth()/2)-viewport.getScreenWidth()/3.3f, viewport.getScreenHeight()-boostLabel.getHeight());
        healthLabel.setPosition((viewport.getScreenWidth()/2)+viewport.getScreenWidth()/6.3f, viewport.getScreenHeight()-healthLabel.getHeight()*.75f);
        speedLabel.setPosition((viewport.getScreenWidth()/2)-speedLabel.getWidth()/2, viewport.getScreenHeight()-speedLabel.getHeight()*1.0f);
        //healthLabel.setPosition((viewport.getScreenWidth()/2), viewport.getScreenHeight()-healthLabel.getHeight());

        // boostLabel.setZIndex(100);
        // System.out.println("timeElapsed: " + timeElapsed);
        Player p = parent.parent.levelManager.getPlayer();
        ArrayList<Planet> planets = parent.parent.levelManager.getPlanets();
        camera.zoom = parent.getCameraZoom();
        backgroundCamera.zoom = parent.getCameraZoom();
        camera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0);
        camera.setToAngle(p.getPhysics().getBody().getAngle());
        camera.update();

        backgroundCamera.position.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2, 0);
        backgroundCamera.setToAngle(p.getPhysics().getBody().getAngle());
        backgroundCamera.update();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        batch.setProjectionMatrix(camera.combined);

        shapeCamera.zoom = parent.getCameraZoom();
        shapeCamera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0);

        shapeCamera.setToAngle(p.getPhysics().getBody().getAngle());
        shapeCamera.update();

        menuCamera.update();

        //clear screen
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(.447f, .2784f, .3843f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        parent.renderBackground(backgroundCamera, timeElapsed, backgroundBatch);

        batch.begin();
        parent.renderPlanets(planets, timeElapsed, batch);
        parent.renderPlayer(p, timeElapsed, batch);

        if(parent.parent.devMode){
            renderVersion(batch);
        }

        batch.end();

        renderGoal(0, batch);

        //Calculate Vector to nearest planet and render and indicator
        O.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2);
        Planet planet = parent.parent.levelManager.getPlayer().getPhysics().getClosestPlanet();
        float planetRadius = ((Planet) planet).getGraphics().getWidth();
        G.set(planet.getX() + planetRadius / 2, planet.getY() + planetRadius / 2);
        float dist = parent.parent.levelManager.getPlayer().getPhysics().getDistanceFromPlanet(planet);
        float clipHeight = getIndicatorClipHeight(dist, dirIndicatorRed);
        renderIndicator(O, G, batch, dirIndicatorRed, 140, clipHeight);

        planet = ((Planet)parent.parent.levelManager.getGoal()); /* This object is our goal. */
        planetRadius = ((Planet) planet).getGraphics().getWidth();
        G.set(planet.getX() + planetRadius / 2, planet.getY() + planetRadius / 2);
        dist = parent.parent.levelManager.getPlayer().getPhysics().getDistanceFromPlanet(planet);
        clipHeight = getIndicatorClipHeight(dist, dirIndicatorGreen);
        renderIndicator(O, G, batch, dirIndicatorGreen, 114 , clipHeight);

        gravityVector = parent.parent.levelManager.getPlayer().getPhysics().getGravityForce();
        tmp.set(gravityVector).setLength(200);
        G.set(O).add(tmp);
        dist = gravityVector.len();
        if(!G.equals(O)){
            clipHeight = getIndicatorClipHeight(dist, dirIndicatorWhite);
            renderIndicator(O, G, batch, dirIndicatorWhite, 114, clipHeight);
        }

        //calculate velocity and display indicator
        O.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2);
        Vector2 velocityVector = parent.parent.levelManager.getPlayer().getPhysics().getBody().getLinearVelocity();
        tmp.set(velocityVector).setLength(200);
        G.set(O).add(tmp);
        dist = velocityVector.len();
        clipHeight = getIndicatorClipHeight(dist, dirIndicatorYellow);
        if(dist > .15f){
            renderIndicator(O, G, batch, dirIndicatorYellow, 88, clipHeight);
        }

        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);

        if(parent.parent.devMode){
            debugRenderer.render(parent.parent.levelManager.getWorld(), debugMatrix);

        }
        stage.setDebugAll(parent.parent.devMode);
        if(!parent.parent.devMode2){
            renderHealth(healthBatch);
            touchPad.setVisible(true);
            boostButton.setVisible(true);
        }else{
            touchPad.setVisible(false);
            boostButton.setVisible(false);
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private float getIndicatorClipHeight(float dist, TextureRegion indicator){
        float clipHeight = 0;
        if(indicator == dirIndicatorRed){
            //System.out.print("dist: " + dist);
            float smallestClip = (indicator.getRegionHeight()/2)/3.4f;
            float biggestClip = indicator.getRegionHeight()/2.5f;
            clipHeight = parent.map(dist, 150, 50, smallestClip, biggestClip);
            if(clipHeight > biggestClip)clipHeight = biggestClip;
            if(clipHeight < smallestClip)clipHeight = smallestClip;
            //  System.out.println("  dist: " + dist + "   clipHeight: " + clipHeight);
        }else if(indicator == dirIndicatorGreen){
            float smallestClip = (indicator.getRegionHeight()/2)/3.4f;
            float biggestClip = indicator.getRegionHeight()/2.5f;
            //System.out.print("dist: " + dist);
            clipHeight = parent.map(dist, 150, 50, smallestClip, biggestClip);
            if(clipHeight > biggestClip)clipHeight = biggestClip;
            if(clipHeight < smallestClip)clipHeight = smallestClip;
        }else if(indicator == dirIndicatorWhite){
            float smallestClip = (indicator.getRegionHeight()/2)/3.4f;
            float biggestClip = indicator.getRegionHeight()/2.5f;

            runningAvgGravity.addData(dist);
            float avg = (float)runningAvgGravity.getMean();
            //System.out.println("gravity: " + avg  +  " : " + dist );
            clipHeight = parent.map(avg, 100 , 500, smallestClip, biggestClip);
            if(clipHeight > biggestClip)clipHeight = biggestClip;
            if(clipHeight < smallestClip)clipHeight = smallestClip;
        }else if(indicator == dirIndicatorYellow){
            float smallestClip = indicator.getRegionHeight() /5f;
            float biggestClip = indicator.getRegionHeight()/2.5f;
            clipHeight = parent.map(dist, 0, parent.parent.levelManager.getPlayer().getPhysics().MAX_VELOCITY*1f, smallestClip, biggestClip);

            if(clipHeight > biggestClip)clipHeight = biggestClip;
            if(clipHeight < smallestClip)clipHeight = smallestClip;

        }

        return clipHeight;
    }

    /**
     * Returns the height of the ghost timer based on the amount of time until ghost timer resets
     * @return
     */
    public float getGhostTimerHeight(){
        float timer = parent.parent.getGhostTimer();
        //  System.out.println("ghostTimer: " + timer);
        //  if(timer <= 0)timer = parent.parent.GHOST_TIMER_LIMIT-.001f;
        return parent.map(timer, parent.parent.GHOST_TIMER_LIMIT, 0, 0, velocityIndicatorOutline.getHeight());
    }

    public float getVelocityHeight(){
        float velocity = parent.parent.levelManager.getPlayer().getPhysics().getBody().getLinearVelocity().len();
        float maxVel = ((PlayerPhysicsComponent)parent.parent.levelManager.getPlayer().getPhysics()).MAX_VELOCITY;
        return parent.map(velocity, 0, maxVel, 1, velocityIndicatorOutline.getHeight());
    }

    public float getHealthMeterWidth(){
        float health = parent.parent.levelManager.getPlayer().getHealth();
        return parent.map(health, 0, parent.parent.levelManager.getPlayer().HEALTH_TOTAL, velocityIndicatorOutline.getWidth()/2, boostMeterEmpty.getRegionWidth());
    }

    public float getBoostMeterWidth(){
        float boost = parent.parent.levelManager.getPlayer().getBoost();
        return parent.map(boost, 0, parent.parent.levelManager.getPlayer().BOOST_TOTAL, velocityIndicatorOutline.getWidth()/2, boostMeterEmpty.getRegionWidth());
    }

    private void renderHealth(SpriteBatch batch){
        float velocity = parent.parent.levelManager.getPlayer().getPhysics().getBody().getLinearVelocity().len();
        float crashVel = ((PlayerPhysicsComponent)parent.parent.levelManager.getPlayer().getPhysics()).CRASH_VELOCITY;

        float gclipWidth = velocityIndicatorOutline.getWidth()+4;
        float gclipX = (viewport.getScreenWidth()/2)-gclipWidth/2;
        float gclipHeight = getVelocityHeight();
        float gclipY = viewport.getScreenHeight()-velocityIndicatorOutline.getHeight();
        //ghostMeterClip = new Rectangle(gclipX,gclipY, gclipWidth, gclipHeight);
        velocityClip.set(gclipX,gclipY, gclipWidth, gclipHeight);

        float hclipWidth = getHealthMeterWidth();
        float hclipX = (viewport.getScreenWidth()/2);
        float hclipHeight = healthMeterEmpty.getRegionHeight();
        float hclipY = viewport.getScreenHeight()-healthMeterEmpty.getRegionHeight();
        // healthClip = new Rectangle(hclipX, hclipY, hclipWidth, hclipHeight);
        healthClip.set(hclipX, hclipY, hclipWidth, hclipHeight);

        float bclipWidth =  getBoostMeterWidth();
        float bclipX =  (viewport.getScreenWidth()/2)-bclipWidth;
        float bclipHeight = 100;
        float bclipY = viewport.getScreenHeight()-boostMeterEmpty.getRegionHeight();
        // boostClip = new Rectangle(bclipX, bclipY, bclipWidth, bclipHeight);
        boostClip.set(bclipX, bclipY, bclipWidth, bclipHeight);

        batch.begin();

        //draw ghostMeter
        //Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(menuCamera, batch.getTransformMatrix(), velocityClip, scissors);
        ScissorStack.pushScissors(scissors);
        if(velocity <= (crashVel/2)){
            batch.draw(velocityIndicatorGreenTexture, velocityIndicatorOutline.getX(), velocityIndicatorOutline.getY());
        }else if(velocity >= crashVel){
            batch.draw(velocityIndicatorRedTexture, velocityIndicatorOutline.getX(), velocityIndicatorOutline.getY());
        }else{
            batch.draw(velocityIndicatorTexture, velocityIndicatorOutline.getX(), velocityIndicatorOutline.getY());
        }
        batch.flush();
        ScissorStack.popScissors();


        //draw healthMeter
        // scissors = new Rectangle();
        ScissorStack.calculateScissors(menuCamera, batch.getTransformMatrix(), healthClip, scissors);
        ScissorStack.pushScissors(scissors);
        if(parent.parent.levelManager.getPlayer().getHealth() > 40){
            batch.draw(healthMeterBlue,  (velocityIndicatorOutline.getX()+velocityIndicatorOutline.getWidth()/2)-2, (viewport.getScreenHeight()/2)-healthMeterEmpty.getRegionHeight()+2);
        }else{
            batch.draw(healthMeterRed,  (velocityIndicatorOutline.getX()+velocityIndicatorOutline.getWidth()/2)-2, (viewport.getScreenHeight()/2)-healthMeterEmpty.getRegionHeight()+2);
        }

        batch.flush();
        ScissorStack.popScissors();

        //draw boostMeter
        //scissors = new Rectangle();
        ScissorStack.calculateScissors(menuCamera, batch.getTransformMatrix(), boostClip, scissors);
        ScissorStack.pushScissors(scissors);
        if(parent.parent.levelManager.getPlayer().getBoost() > 1){
            batch.draw(boostMeterBlue,  -boostMeterBlue.getRegionWidth()+2, (viewport.getScreenHeight()/2)-boostMeterBlue.getRegionHeight()+2);
        }else{
            batch.draw(boostMeterRed,  -boostMeterRed.getRegionWidth()+2, (viewport.getScreenHeight()/2)-boostMeterRed.getRegionHeight()+2);
        }

        batch.flush();
        ScissorStack.popScissors();


        batch.draw(velocityIndicatorOutlineTexture, velocityIndicatorOutline.getX(), velocityIndicatorOutline.getY());

        //these next two draw the outline of the health and boost meter, except they were also drawing other things in the texture, i think it's because we not using scissorstack here
         batch.draw(healthMeterEmpty,  (velocityIndicatorOutline.getX()+velocityIndicatorOutline.getWidth()/2)-2, (viewport.getScreenHeight()/2)-healthMeterEmpty.getRegionHeight()+2);
         batch.draw(boostMeterEmpty,  -boostMeterEmpty.getRegionWidth()+2, (viewport.getScreenHeight()/2)-boostMeterEmpty.getRegionHeight()+2);

        batch.draw(indicatorLegend, (viewport.getScreenWidth()/2)-indicatorLegend.getRegionWidth()-10, (viewport.getScreenHeight()/2.5f)-indicatorLegend.getRegionHeight()+2);


        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(menuCamera.combined);
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.setColor(Color.YELLOW);

        if(parent.parent.devMode){
            shapeRenderer.rect(velocityClip.getX(), velocityClip.getY(), velocityClip.getWidth(), velocityClip.getHeight());
            shapeRenderer.rect(healthClip.getX(), healthClip.getY(), healthClip.getWidth(), healthClip.getHeight());
            shapeRenderer.rect(boostClip.getX(), boostClip.getY(), boostClip.getWidth(), boostClip.getHeight());
        }

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.end();
    }


    /**
     * Renders the directional indicators
     * @param origin
     * @param goal
     * @param batch
     * @param indicator
     * @param distanceFromOrigin
     * @param magnitude
     */
    private void renderIndicator(Vector2 origin, Vector2 goal, ClipBatch batch, TextureRegion indicator, float distanceFromOrigin, float magnitude) {
        float regionHeight = indicator.getRegionHeight()/2;
        float regionWidth = indicator.getRegionWidth()/2;
        //L1 = G - O
        tmp.set(origin);
        L1.set(goal).sub(origin);

        float goalAngle = L1.angle();

        //L2 = LIM(L1, L2.Length) // L2.Length = distanceFromOrigin, we will pass in
        L2.set(L1).limit(distanceFromOrigin);

        //O1 = O + L2
        tmp.set(L2);
        O1.set(origin).add(tmp);

        //L3 = LIM(rot(L2, 90), L3.limit); //L3.limit = dirIndicator.width/2
        L3.set(L2).rotate(90).limit(regionWidth / 2);


        //O2 = O1 + L3
        tmp.set(L3);
        O2.set(O1).add(tmp);

        //L4 = LIM(rot(L3, 90), L4.length); //L4.length = dirIndicator.height
        L4.set(L3).rotate(90).limit(regionHeight);

        //O3 = O2 + L4
        tmp.set(L4);
        tmp.setLength(regionHeight);
        O3.set(O2).add(tmp);

        tmp.set(L2).setLength(100);
        O4.set(O2).add(tmp);

        L5.set(L3);
        L5.setLength((indicator.getRegionWidth()/4)+0);

        tmp.set(L5);
        O5.set(O3).add(tmp);

        tmp.set(L5);
        O6.set(O2).add(tmp);

        tmp.set(L5);
        O7.set(O3).sub(tmp);

        tmp.set(L5);
        O8.set(O2).sub(tmp);

        rotation = (float)-parent.parent.levelManager.getPlayer().getPhysics().getBody().getAngle()+(float)Math.toRadians(goalAngle-180);


        if(indicator == dirIndicatorRed){
            //System.out.println("magnitude: " + magnitude + "   indicator.Height :" + indicator.getRegionHeight());
            //width and height arebackwards here for some reason, i don't care though, it's working
            batch.begin(O2.x, O2.y, magnitude, regionWidth, rotation, shapeRenderer);
            batch.draw(indicator,
                    O3.x, O3.y,
                    0, 0,
                    regionWidth, regionHeight,
                    1f,1f,
                    goalAngle - 90);
            batch.end();
        }else if(indicator == dirIndicatorGreen){
            batch.begin(O6.x, O6.y, magnitude, regionWidth, rotation, shapeRenderer);
            batch.draw(indicator,
                    O5.x, O5.y,
                    0, 0,
                    regionWidth, regionHeight,
                    1f,1f,
                    goalAngle - 90);
            batch.end();
        }else if(indicator == dirIndicatorWhite){
            batch.begin(O8.x, O8.y, magnitude, regionWidth, rotation, shapeRenderer);
            batch.draw(indicator,
                    O7.x, O7.y,
                    0, 0,
                    regionWidth, regionHeight,
                    1f,1f,
                    goalAngle - 90);
            batch.end();
        }else{
            batch.begin(O2.x, O2.y, magnitude, regionWidth, rotation, shapeRenderer);
            batch.draw(indicator,
                    O3.x, O3.y,
                    0, 0,
                    regionWidth, regionHeight,
                    1f,1f,
                    goalAngle - 90);
            batch.end();
        }
    }

    private void renderGoal(float elapsedTime, SpriteBatch batch){
        GameObject g = parent.parent.levelManager.getGoal(); // This object is our goal. /
        Player p = parent.parent.levelManager.getPlayer();

        if(g instanceof Planet){ //D
            Planet goal = (Planet)g;
            float goalRadius = ((Planet)goal).getGraphics().getWidth();//getPhysics().getBody().getFixtureList().first().getShape().getRadius();
            renderGoalMiddleOfGoal.set(goal.getX()+(goalRadius/2), goal.getY()+(goalRadius/2));
            Gdx.gl.glLineWidth(10);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(camera.combined);


            shapeRenderer.setColor(Color.CHARTREUSE);

            shapeRenderer.circle((renderGoalMiddleOfGoal.x), (renderGoalMiddleOfGoal.y), (goalRadius / 2)-8);

            shapeRenderer.setProjectionMatrix(shapeCamera.combined);
            shapeRenderer.end();
            Gdx.gl.glLineWidth(2);
        }
    }


    public void setCameraZoom(float zoom){
        super.setCameraZoom(zoom);
        backgroundCamera.zoom = zoom;
    }

    @Override
    public void sendEvent(Event e) {
        EventDispatcher.getSingletonDispatcher().dispatch(e);
    }

    // Java program to calculate
// Simple Moving Average

    /**
     * A class to keep track of a simple moving average
     */
    private class SimpleMovingAverage {

        // queue used to store list so that we get the average
        private final Queue<Double> Dataset = new LinkedList<Double>();
        private final int period;
        private double sum;

        /**
         * Constructor
         * @param period The number to average over
         */
        public SimpleMovingAverage(int period) {
            this.period = period;
        }

        /**
         * Add new data to the list and update the sum for the new mean
         * @param num
         */
        public void addData(double num) {
            sum += num;
            Dataset.add(num);

            /**
             * Limit the size of the list
             */
            if (Dataset.size() > period) {
                sum -= Dataset.remove();
            }
        }

        /**
         * Calculate the average
         * @return
         */
        public double getMean() {
            return sum / period;
        }
    }
}
