package com.araceinspace.Screens;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
import com.araceinspace.GameObjectSubSystem.Components.PlayerState;
import com.araceinspace.GameObjectSubSystem.GameObject;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
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
        skin.dispose();


    }

    @Override
    public void setup() {
       // stage = new Stage(menuCamera)
        font = new BitmapFont();
        backgroundCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch = new SpriteBatch();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);
        monetizationController.loadRewardAd();
        monetizationController.hideBannerAd();
        setupStage();
    }



    /* Private Methods */

    private void setupStage(){
        viewport = new ScreenViewport(menuCamera);
        stage = new Stage(viewport, batch);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);

        touchPad = new Touchpad(10, skin, "default");
        touchPad.setBounds(15, 15, Gdx.graphics.getWidth()/2-20,Gdx.graphics.getWidth()/2-20);
        touchPad.getStyle().knob.setMinWidth(touchPad.getWidth()/4);
        touchPad.getStyle().knob.setMinHeight(touchPad.getHeight()/4);
        touchPad.addListener(parent.parent.inputManager);

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
        headerTable.add(rewardButton).padLeft(spacer).padTop(0).size(viewport.getScreenWidth()/8, viewport.getScreenHeight()/10);
        headerTable.add(menuButton).padLeft(spacer).padTop(0).align(Align.left).size(viewport.getScreenWidth()/8, viewport.getScreenHeight()/10);
        mainTable.add(headerTable).fill().expandX();
        stage.addActor(mainTable);
        stage.addActor(touchPad);
        stage.addActor(boostButton);
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

    }

    private void renderVelocityIndicator(SpriteBatch batch){


        Player p = parent.parent.levelManager.getPlayer();

        //don't render velocity indicator if player  is standing still forward
        if(p.getState().getCurrentState() == PlayerState.STAND_STILL_FORWARD)return;
        Vector2 velocityVector = parent.parent.levelManager.getPlayer().getPhysics().getBody().getLinearVelocity();
        Vector2 startPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
        Vector2 goalPos = velocityVector.add(startPos);
        //Vector2 goalPos =new Vector2(0,0);

        Vector2 endLine = goalPos.sub(startPos);
        endLine.setLength(75f);

        Vector2 perpLine1 = endLine.cpy().rotate(135f); //get rotated difference vector
        Vector2 perpLine2 = endLine.cpy().rotate(-135f); //get rotated difference vector

        perpLine1.setLength(15f); //set length of perpLineVector
        perpLine2.setLength(15f); //set length of perpLineVector

        endLine = startPos.cpy().add(endLine); //convert back to point
        perpLine1 = endLine.cpy().add(perpLine1); // convert back to point
        perpLine2 = endLine.cpy().add(perpLine2); // convert back to point

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(shapeCamera.combined);
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.setColor(Color.YELLOW);

        shapeRenderer.rectLine(endLine, perpLine1, 3);
        shapeRenderer.rectLine(endLine, perpLine2, 3);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.end();
    }

    private void renderGravityIndicator(SpriteBatch batch){
        Player p = parent.parent.levelManager.getPlayer();
        Vector2 gravityVector = parent.parent.levelManager.getPlayer().getPhysics().getGravityForce();
        Vector2 startPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
        Vector2 goalPos = gravityVector.add(startPos);
        //Vector2 goalPos =new Vector2(0,0);

        Vector2 endLine = goalPos.sub(startPos);
        endLine.setLength(85f);

        Vector2 perpLine1 = endLine.cpy().rotate(135f); //get rotated difference vector
        Vector2 perpLine2 = endLine.cpy().rotate(-135f); //get rotated difference vector

        perpLine1.setLength(15f); //set length of perpLineVector
        perpLine2.setLength(15f); //set length of perpLineVector

        endLine = startPos.cpy().add(endLine); //convert back to point
        perpLine1 = endLine.cpy().add(perpLine1); // convert back to point
        perpLine2 = endLine.cpy().add(perpLine2); // convert back to point

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(shapeCamera.combined);
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.setColor(Color.WHITE);

        shapeRenderer.rectLine(endLine, perpLine1, 3);
        shapeRenderer.rectLine(endLine, perpLine2, 3);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.end();
    }

    private void renderNearestPlanetIndicator(SpriteBatch batch){

        Player p = parent.parent.levelManager.getPlayer();


            Planet planet = parent.parent.levelManager.getPlayer().getPhysics().getClosestPlanet();
            float goalRadius = ((Planet)planet).getGraphics().getWidth();//getPhysics().getBody().getFixtureList().first().getShape().getRadius();

            Vector2 startPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
            Vector2 goalPos = new Vector2(planet.getX()+goalRadius/2, planet.getY()+goalRadius/2);

            Vector2 middleOfGoal = new Vector2(planet.getX()+(goalRadius/2), planet.getY()+(goalRadius/2));
            Vector2 endLine = middleOfGoal.cpy().sub(startPos); //get difference vector
            Vector2 perpLine1 = endLine.cpy().rotate(135f); //get rotated difference vector
            Vector2 perpLine2 = endLine.cpy().rotate(-135f); //get rotated difference vector


            endLine.setLength(70f); // set length of distance vector
            perpLine1.setLength(15f); //set length of perpLineVector
            perpLine2.setLength(15f); //set length of perpLineVector

            endLine = startPos.cpy().add(endLine); //convert back to point
            perpLine1 = endLine.cpy().add(perpLine1); // convert back to point
            perpLine2 = endLine.cpy().add(perpLine2); // convert back to point




                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setProjectionMatrix(shapeCamera.combined);
                shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rectLine(endLine, perpLine1, 3);
                shapeRenderer.rectLine(endLine, perpLine2, 3);

                 shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.end();



    }

    private void renderGoal(float elapsedTime, SpriteBatch batch){
        GameObject g = parent.parent.levelManager.getGoal(); /* This object is our goal. */
        Player p = parent.parent.levelManager.getPlayer();

        if(g instanceof Planet){ //D
            Planet goal = (Planet)g;
            float goalRadius = ((Planet)goal).getGraphics().getWidth();//getPhysics().getBody().getFixtureList().first().getShape().getRadius();

            Vector2 startPos = new Vector2(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2);
            Vector2 goalPos = new Vector2(goal.getX()+goalRadius/2, goal.getY()+goalRadius/2);

            Vector2 middleOfGoal = new Vector2(goal.getX()+(goalRadius/2), goal.getY()+(goalRadius/2));
            Vector2 endLine = middleOfGoal.cpy().sub(startPos); //get difference vector
            Vector2 perpLine1 = endLine.cpy().rotate(135f); //get rotated difference vector
            Vector2 perpLine2 = endLine.cpy().rotate(-135f); //get rotated difference vector


            endLine.setLength(80f); // set length of distance vector
            perpLine1.setLength(15f); //set length of perpLineVector
            perpLine2.setLength(15f); //set length of perpLineVector

            endLine = startPos.cpy().add(endLine); //convert back to point
            perpLine1 = endLine.cpy().add(perpLine1); // convert back to point
            perpLine2 = endLine.cpy().add(perpLine2); // convert back to point


            if(g != null){ /* The goal can be null, make sure it isn't here. */
                //

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setProjectionMatrix(shapeCamera.combined);
                shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rectLine(endLine, perpLine1, 3);
                shapeRenderer.rectLine(endLine, perpLine2, 3);

                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setProjectionMatrix(camera.combined);

                Gdx.gl.glLineWidth(100);

                shapeRenderer.circle((middleOfGoal.x), (middleOfGoal.y), goalRadius / 2);

                shapeRenderer.circle((middleOfGoal.x), (middleOfGoal.y), 2);

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
