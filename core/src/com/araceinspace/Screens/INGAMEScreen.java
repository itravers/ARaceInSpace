package com.araceinspace.Screens;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
import com.araceinspace.GameObjectSubSystem.Components.PlayerPhysicsComponent;
import com.araceinspace.GameObjectSubSystem.Components.PlayerState;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

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

    private SpriteBatch healthBatch;
    Rectangle velocityClip;
    Rectangle healthClip;
    Rectangle boostClip;
    Rectangle scissors;

    //Vectors
    Vector2 renderVelStartPos;
    Vector2 renderVelGoalPos;
    Vector2 renderVelEndLine;
    Vector2 renderVelPerpLine1;
    Vector2 renderVelPerpLine2;
    Vector2 gravityVector;
    Vector2 renderGravityStartPos;
    Vector2  renderGravityGoalPos;
    Vector2 renderGravityEndLine;
    Vector2 renderGravityPerpLine1;
    Vector2 renderGravityPerpLine2;
    Vector2 renderNearestStartPos;
    Vector2 renderNearestGoalPos;
    Vector2 renderGoalStartPos;
    Vector2 renderGoalGoalPos;
    Vector2 renderGoalMiddleOfGoal;
    Vector2 renderGoalEndLine;
    Vector2 renderGoalPerpLine1;
    Vector2 renderGoalPerpLine2;
    Vector2 tmp;
    Vector2 renderNearestMiddleOfGoal;
    Vector2 renderNearestEndLine;
    Vector2 renderNearestPerpLine1;
    Vector2  renderNearestPerpLine2;

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
        System.out.println("SETUP INGAME SCREEN");
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
       // monetizationController.loadRewardAd();
       // monetizationController.hideBannerAd();
        setupStage();
        parent.parent.elapsedTime = 0;
        parent.parent.renderManager.resetFrameNum();
    }



    /* Private Methods */

    private void setupVectors(){
        renderVelStartPos = new Vector2();
        renderVelGoalPos = new Vector2();
        renderVelEndLine = new Vector2();
        renderVelPerpLine1 = new Vector2();
        renderVelPerpLine2 = new Vector2();
        renderGravityStartPos = new Vector2();
        renderGravityGoalPos = new Vector2();
        renderGravityEndLine = new Vector2();
        renderGravityPerpLine1 = new Vector2();
        renderGravityPerpLine2 = new Vector2();
        renderNearestStartPos = new Vector2();
        renderNearestGoalPos = new Vector2();
        renderGoalStartPos = new Vector2();
        renderGoalGoalPos = new Vector2();
        renderGoalMiddleOfGoal = new Vector2();
        renderGoalEndLine = new Vector2();
        renderGoalPerpLine1  = new Vector2();
        renderGoalPerpLine2 = new Vector2();
        renderNearestMiddleOfGoal = new Vector2();
        renderNearestEndLine = new Vector2();
        renderNearestPerpLine1 = new Vector2();
        renderNearestPerpLine2 = new Vector2();
        tmp = new Vector2();
    }

    private void setupStage(){
        viewport = new ScreenViewport(menuCamera);
        stage = new Stage(viewport, batch);
       // TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
       // skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
        skin = parent.parent.resourceManager.getSkin();

        touchPad = new Touchpad(10, skin, "default");
        touchPad.setBounds(15, 15, Gdx.graphics.getWidth()/2-20,Gdx.graphics.getWidth()/2-20);
        touchPad.getStyle().knob.setMinWidth(touchPad.getWidth()/4);
        touchPad.getStyle().knob.setMinHeight(touchPad.getHeight()/4);
        touchPad.addListener(parent.parent.inputManager);
        touchPad.setDeadzone(touchPad.getWidth()/5);

        boostButton = new ImageButton(skin, "boostButton");
        boostButton.setWidth(Gdx.graphics.getWidth()/3-20);
        boostButton.setHeight(Gdx.graphics.getWidth()/3-20);
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





       // touchPad.addListener(touchpadListener);

        Table mainTable;
        Table headerTable;
        Table bodyTable;
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
                System.out.println("Reward Clicked");

                monetizationController.showRewardAd();
                monetizationController.loadRewardAd();
            }
        };

        menuButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("Menu Clicked");
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
       // headerTable.add(rewardButton).padLeft(spacer).padTop(0).size(viewport.getScreenWidth()/8, viewport.getScreenHeight()/10);
        //headerTable.add(menuButton).padLeft(spacer).padTop(0).align(Align.left).size(viewport.getScreenWidth()/8, viewport.getScreenHeight()/10);
        mainTable.add(headerTable).fill().expandX();
        stage.addActor(mainTable);
        stage.addActor(touchPad);
        stage.addActor(boostButton);

        Array<TextureAtlas.AtlasRegion> ghostIndicatorOutlineRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_empty");
        velocityIndicatorOutlineTexture = ghostIndicatorOutlineRegion.get(0);
        velocityIndicatorOutline = new Image(velocityIndicatorOutlineTexture);
       // ghostIndicatorOutline.setPosition((viewport.getScreenWidth()/2)-ghostIndicatorOutline.getWidth()/2, viewport.getScreenHeight()-ghostIndicatorOutline.getHeight());
        velocityIndicatorOutline.setPosition(-velocityIndicatorOutline.getWidth()/2,(viewport.getScreenHeight()/2)-velocityIndicatorOutline.getHeight());

        Array<TextureAtlas.AtlasRegion> ghostIndicatorRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_fill");
        velocityIndicatorTexture = ghostIndicatorRegion.get(0);

        Array<TextureAtlas.AtlasRegion> ghostIndicatorGreenRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_fill_green");
        velocityIndicatorGreenTexture = ghostIndicatorGreenRegion.get(0);

        Array<TextureAtlas.AtlasRegion> ghostIndicatorRedRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/ghostIndicator_red");
        velocityIndicatorRedTexture = ghostIndicatorRedRegion.get(0);


        float gclipWidth = velocityIndicatorOutline.getWidth()+4;
        float gclipX = (viewport.getScreenWidth()/2)-gclipWidth/2;
        float gclipHeight = 50;
        float gclipY = viewport.getScreenHeight()-gclipHeight;
        velocityClip = new Rectangle(gclipX,gclipY, gclipWidth, gclipHeight);



        Array<TextureAtlas.AtlasRegion> healthMeterEmptyRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_empty");
        healthMeterEmpty = healthMeterEmptyRegion.get(0);
        healthMeterEmpty.setRegionWidth(2+viewport.getScreenWidth()/2);
       // healthMeterEmpty

        Array<TextureAtlas.AtlasRegion> healthMeterBlueRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_blue");
        healthMeterBlue = healthMeterBlueRegion.get(0);
        healthMeterBlue.setRegionWidth(2+viewport.getScreenWidth()/2);

        Array<TextureAtlas.AtlasRegion> healthMeterRedRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_red");
        healthMeterRed = healthMeterRedRegion.get(0);
        healthMeterRed.setRegionWidth(2+viewport.getScreenWidth()/2);

        Array<TextureAtlas.AtlasRegion> boostMeterEmptyRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_empty");
        boostMeterEmpty = boostMeterEmptyRegion.get(0);
        boostMeterEmpty.flip(true, false);

        Array<TextureAtlas.AtlasRegion> boostMeterBlueRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_blue");
        boostMeterBlue = boostMeterBlueRegion.get(0);
        boostMeterBlue.flip(true, false);

        Array<TextureAtlas.AtlasRegion> boostMeterRedRegion = parent.parent.resourceManager.heroAtlas.findRegions("GhostIndicator/healthMeter_red");
        boostMeterRed = boostMeterRedRegion.get(0);
        boostMeterRed.flip(true, false);

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

        renderGoal(timeElapsed, batch);
        renderGravityIndicator(batch);
        renderVelocityIndicator(batch);
        renderNearestPlanetIndicator(batch);
        //stage.act(timeElapsed);
        // stage.draw();



        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);

        if(parent.parent.devMode){
            debugRenderer.render(parent.parent.levelManager.getWorld(), debugMatrix);

        }
        stage.setDebugAll(parent.parent.devMode);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        renderHealth(healthBatch);

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
        return parent.map(health, 0, parent.parent.levelManager.getPlayer().HEALTH_TOTAL, velocityIndicatorOutline.getWidth()/2, healthMeterEmpty.getRegionWidth());
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
        batch.draw(healthMeterEmpty,  (velocityIndicatorOutline.getX()+velocityIndicatorOutline.getWidth()/2)-2, (viewport.getScreenHeight()/2)-healthMeterEmpty.getRegionHeight()+2);
        batch.draw(boostMeterEmpty,  -boostMeterEmpty.getRegionWidth()+2, (viewport.getScreenHeight()/2)-boostMeterEmpty.getRegionHeight()+2);

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

    private void renderVelocityIndicator(SpriteBatch batch){


        Player p = parent.parent.levelManager.getPlayer();

        //don't render velocity indicator if player  is standing still forward
        if(p.getState().getCurrentState() == PlayerState.STAND_STILL_FORWARD)return;
        Vector2 velocityVector = parent.parent.levelManager.getPlayer().getPhysics().getBody().getLinearVelocity();
        float velocityPower = velocityVector.len();
        //Vector2 renderVelStartPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
        renderVelStartPos.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
        renderVelGoalPos = velocityVector.add(renderVelStartPos);
        //Vector2 goalPos =new Vector2(0,0);

        renderVelEndLine = renderVelGoalPos.sub(renderVelStartPos);
        renderVelEndLine.setLength(75f);

        renderVelPerpLine1.set(renderVelEndLine.x, renderVelEndLine.y).rotate(135f);
        //renderVelPerpLine1 = renderVelEndLine.cpy().rotate(135f); //get rotated difference vector
        renderVelPerpLine2.set(renderVelEndLine.x, renderVelEndLine.y).rotate(-135f);
        //renderVelPerpLine2 = renderVelEndLine.cpy().rotate(-135f); //get rotated difference vector

        float lineLength = 15f;

        lineLength = 15f + (velocityPower/1);


        renderVelPerpLine1.setLength(lineLength); //set length of perpLineVector
        renderVelPerpLine2.setLength(lineLength); //set length of perpLineVector

        tmp.set(renderVelEndLine.x, renderVelEndLine.y);
        renderVelEndLine.set(renderVelStartPos.x, renderVelStartPos.y).add(tmp); //convert back to point
        //renderVelEndLine = renderVelStartPos.cpy().add(renderVelEndLine); //convert back to point

        tmp.set(renderVelPerpLine1.x, renderVelPerpLine1.y);
        renderVelPerpLine1.set(renderVelEndLine.x, renderVelEndLine.y).add(tmp); // convert back to point
        //renderVelPerpLine1 = renderVelEndLine.cpy().add(renderVelPerpLine1); // convert back to point

        tmp.set(renderVelPerpLine2.x, renderVelPerpLine2.y);
        renderVelPerpLine2.set(renderVelEndLine.x, renderVelEndLine.y).add(tmp);// convert back to point
       // renderVelPerpLine2 = renderVelEndLine.cpy().add(renderVelPerpLine2); // convert back to point

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(shapeCamera.combined);
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.setColor(Color.YELLOW);

        shapeRenderer.rectLine(renderVelEndLine, renderVelPerpLine1, 3);
        shapeRenderer.rectLine(renderVelEndLine, renderVelPerpLine2, 3);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.end();
    }

    private void renderGravityIndicator(SpriteBatch batch){
        Player p = parent.parent.levelManager.getPlayer();
        gravityVector = parent.parent.levelManager.getPlayer().getPhysics().getGravityForce();
        float gravityPower = gravityVector.len();
       // System.out.println("gravityPower: " + gravityPower);
        //Vector2 renderGravityStartPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
        renderGravityStartPos.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
        renderGravityGoalPos = gravityVector.add(renderGravityStartPos);
        //Vector2 goalPos =new Vector2(0,0);



        renderGravityEndLine = renderGravityGoalPos.sub(renderGravityStartPos);
        renderGravityEndLine.setLength(85f);

        float lineLength = 15f;

        //if(gravityPower >= 300){
            lineLength = 15f + (gravityPower/16);
       // }

        renderGravityPerpLine1.set(renderGravityEndLine.x, renderGravityEndLine.y).rotate(135f);
        //renderGravityPerpLine1 = renderGravityEndLine.cpy().rotate(135f); //get rotated difference vector

        renderGravityPerpLine2.set(renderGravityEndLine.x, renderGravityEndLine.y).rotate(-135f);
        renderGravityPerpLine2 = renderGravityEndLine.cpy().rotate(-135f); //get rotated difference vector



        renderGravityPerpLine1.setLength(lineLength); //set length of perpLineVector
        renderGravityPerpLine2.setLength(lineLength); //set length of perpLineVector

        tmp.set(renderGravityEndLine.x, renderGravityEndLine.y);
        renderGravityEndLine.set(renderGravityStartPos.x, renderGravityStartPos.y).add(tmp);
        //renderGravityEndLine = renderGravityStartPos.cpy().add(renderGravityEndLine); //convert back to point

        tmp.set(renderGravityPerpLine1.x, renderGravityPerpLine1.y);
        renderGravityPerpLine1.set(renderGravityEndLine.x, renderGravityEndLine.y).add(tmp);
        //renderGravityPerpLine1 = renderGravityEndLine.cpy().add(renderGravityPerpLine1); // convert back to point

        tmp.set(renderGravityPerpLine2.x, renderGravityPerpLine2.y);
        renderGravityPerpLine2.set(renderGravityEndLine.x, renderGravityEndLine.y).add(tmp);
       // renderGravityPerpLine2 = renderGravityEndLine.cpy().add(renderGravityPerpLine2); // convert back to point

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(shapeCamera.combined);
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.setColor(Color.WHITE);

        shapeRenderer.rectLine(renderGravityEndLine, renderGravityPerpLine1, 3);
        shapeRenderer.rectLine(renderGravityEndLine, renderGravityPerpLine2, 3);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.end();
    }

    private void renderNearestPlanetIndicator(SpriteBatch batch){

        Player p = parent.parent.levelManager.getPlayer();


        Planet planet = parent.parent.levelManager.getPlayer().getPhysics().getClosestPlanet();
        float goalRadius = ((Planet)planet).getGraphics().getWidth();//getPhysics().getBody().getFixtureList().first().getShape().getRadius();

           // Vector2 renderNearestStartPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
            //Vector2 renderNearestGoalPos = new Vector2(planet.getX()+goalRadius/2, planet.getY()+goalRadius/2);
        renderNearestStartPos.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
        renderNearestGoalPos.set(planet.getX()+goalRadius/2, planet.getY()+goalRadius/2);

            //renderNearestPlanetMiddleOfGoal = new Vector2(planet.getX()+(goalRadius/2), planet.getY()+(goalRadius/2));
        renderNearestMiddleOfGoal.set(planet.getX()+(goalRadius/2), planet.getY()+(goalRadius/2));

        tmp.set(renderNearestStartPos.x, renderNearestStartPos.y);
        renderNearestEndLine.set(renderNearestMiddleOfGoal.x, renderNearestMiddleOfGoal.y).sub(tmp);
        //renderNearestEndLine = renderNearestMiddleOfGoal.cpy().sub(renderNearestStartPos); //get difference vector

        renderNearestPerpLine1.set(renderNearestEndLine.x, renderNearestEndLine.y).rotate(135f);
        //renderNearestPerpLine1 = renderNearestEndLine.cpy().rotate(135f); //get rotated difference vector

        renderNearestPerpLine2.set(renderNearestEndLine.x, renderNearestEndLine.y).rotate(-135f);
        //renderNearestPerpLine2 = renderNearestEndLine.cpy().rotate(-135f); //get rotated difference vector

        tmp.set(renderNearestStartPos.x, renderNearestStartPos.y);
        float distanceToItem = ((tmp.sub(renderNearestGoalPos).len()-(planet.getWidth()/2)-p.getHeight()/2)/parent.parent.renderManager.PIXELS_TO_METERS);
        //float distanceToItem = (renderNearestStartPos.cpy().sub(renderNearestGoalPos).len()-(planet.getWidth()/2)-p.getHeight()/2)/parent.parent.renderManager.PIXELS_TO_METERS;
           // System.out.println("distanceToGoal: " +distanceToItem);

            float lineLength = 15f;

             if(distanceToItem <= 100){
                lineLength = 15f + ((100-distanceToItem)/4);
             }


        renderNearestEndLine.setLength(70f); // set length of distance vector
        renderNearestPerpLine1.setLength(lineLength); //set length of perpLineVector
        renderNearestPerpLine2.setLength(lineLength); //set length of perpLineVector

        tmp.set(renderNearestEndLine.x, renderNearestEndLine.y);
        renderNearestEndLine.set(renderNearestStartPos.x, renderNearestStartPos.y).add(tmp);
       // renderNearestEndLine = renderNearestStartPos.cpy().add(renderNearestEndLine); //convert back to point

        tmp.set(renderNearestPerpLine1.x, renderNearestPerpLine1.y);
        renderNearestPerpLine1.set(renderNearestEndLine.x, renderNearestEndLine.y).add(tmp);
        //renderNearestPerpLine1 = renderNearestEndLine.cpy().add(renderNearestPerpLine1); // convert back to point

        tmp.set(renderNearestPerpLine2.x, renderNearestPerpLine2.y);
        renderNearestPerpLine2.set(renderNearestEndLine.x, renderNearestEndLine.y).add(tmp);
        //renderNearestPerpLine2 = renderNearestEndLine.cpy().add(renderNearestPerpLine2); // convert back to point




                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setProjectionMatrix(shapeCamera.combined);
                shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rectLine(renderNearestEndLine, renderNearestPerpLine1, 3);
                shapeRenderer.rectLine(renderNearestEndLine, renderNearestPerpLine2, 3);

                 shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.end();



    }

    private void renderGoal(float elapsedTime, SpriteBatch batch){
        GameObject g = parent.parent.levelManager.getGoal(); /* This object is our goal. */
        Player p = parent.parent.levelManager.getPlayer();

        if(g instanceof Planet){ //D
            Planet goal = (Planet)g;
            float goalRadius = ((Planet)goal).getGraphics().getWidth();//getPhysics().getBody().getFixtureList().first().getShape().getRadius();

            //Vector2 renderGoalStartPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
            //Vector2 renderGoalGoalPos = new Vector2(goal.getX()+goalRadius/2, goal.getY()+goalRadius/2);
            renderGoalStartPos.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
            renderGoalGoalPos.set(goal.getX()+goalRadius/2, goal.getY()+goalRadius/2);

            //Vector2 renderGoalMiddleOfGoal = new Vector2(goal.getX()+(goalRadius/2), goal.getY()+(goalRadius/2));
            renderGoalMiddleOfGoal.set(goal.getX()+(goalRadius/2), goal.getY()+(goalRadius/2));

            tmp.set(renderGoalStartPos.x, renderGoalStartPos.y);
            renderGoalEndLine.set(renderGoalMiddleOfGoal.x, renderGoalMiddleOfGoal.y).sub(tmp);
            //renderGoalEndLine = renderGoalMiddleOfGoal.cpy().sub(renderGoalStartPos); //get difference vector

            renderGoalPerpLine1.set(renderGoalEndLine.x, renderGoalEndLine.y).rotate(135f);
            //renderGoalPerpLine1 = renderGoalEndLine.cpy().rotate(135f); //get rotated difference vector

            renderGoalPerpLine2.set(renderGoalEndLine.x, renderGoalEndLine.y).rotate(-135f);
            //renderGoalPerpLine2 = renderGoalEndLine.cpy().rotate(-135f); //get rotated difference vector

            tmp.set(renderGoalStartPos.x, renderGoalStartPos.y);
            float distanceToItem = ((tmp.sub(renderGoalGoalPos).len()-(goal.getWidth()/2)-p.getHeight()/2)/parent.parent.renderManager.PIXELS_TO_METERS);
            //float distanceToItem = (renderGoalStartPos.cpy().sub(renderGoalGoalPos).len()-(goal.getWidth()/2)-p.getHeight()/2)/parent.parent.renderManager.PIXELS_TO_METERS;

            float lineLength = 15f;

            if(distanceToItem <= 100){
                lineLength = 15f + ((100-distanceToItem)/4);
            }

            renderGoalEndLine.setLength(80f); // set length of distance vector
            renderGoalPerpLine1.setLength(lineLength); //set length of perpLineVector
            renderGoalPerpLine2.setLength(lineLength); //set length of perpLineVector

            tmp.set(renderGoalEndLine.x, renderGoalEndLine.y);
            renderGoalEndLine.set(renderGoalStartPos.x, renderGoalStartPos.y).add(tmp);
            //renderGoalEndLine = renderGoalStartPos.cpy().add(renderGoalEndLine); //convert back to point

            tmp.set(renderGoalPerpLine1.x, renderGoalPerpLine1.y);
            renderGoalPerpLine1.set(renderGoalEndLine.x, renderGoalEndLine.y).add(tmp);
            //renderGoalPerpLine1 = renderGoalEndLine.cpy().add(renderGoalPerpLine1); // convert back to point

            tmp.set(renderGoalPerpLine2.x, renderGoalPerpLine2.y);
            renderGoalPerpLine2.set(renderGoalEndLine.x, renderGoalEndLine.y).add(tmp);
            //renderGoalPerpLine2 = renderGoalEndLine.cpy().add(renderGoalPerpLine2); // convert back to point


            if(g != null){ /* The goal can be null, make sure it isn't here. */
                //

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setProjectionMatrix(shapeCamera.combined);
                shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rectLine(renderGoalEndLine, renderGoalPerpLine1, 3);
                shapeRenderer.rectLine(renderGoalEndLine, renderGoalPerpLine2, 3);

                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setProjectionMatrix(camera.combined);

                Gdx.gl.glLineWidth(100);

                shapeRenderer.circle((renderGoalMiddleOfGoal.x), (renderGoalMiddleOfGoal.y), goalRadius / 2);

                shapeRenderer.circle((renderGoalMiddleOfGoal.x), (renderGoalMiddleOfGoal.y), 2);

                Gdx.gl.glLineWidth(2);

                shapeRenderer.setProjectionMatrix(shapeCamera.combined);
                shapeRenderer.end();
            }
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


}
