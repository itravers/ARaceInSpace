package com.araceinspace.Screens;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.FontGenerator;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by The screen to be rendered when the GAME_STATE is TITLE on 7/16/17.
 */
public class TITLEScreen extends Screen implements EventSender {

    /* Static Variables */

    /* Field Variables & Objects */
    private Skin skin;
    private Table mainTable;
    private Table headerTable;
    private Label titleLabel1;
    private Label titleLabel2;
    private Label titleLabel3;
    private Label titleLabel4;
    private ClickListener startButtonListener;
    private ClickListener leaderboardButtonListener;
    private ClickListener menuButtonListener;
    Viewport menuViewport;

    private ImageButton menuButton;
    private ImageTextButton startButton;
    private ImageTextButton leaderboardButton;
    private SpriteBatch backgroundBatch;
    private OrthCamera backgroundCamera;
    private OrthCamera menuCamera;
    private SpriteBatch menuBatch;

    int spacer;


    /* Constructors */

    public TITLEScreen(RenderManager p) {
        super(p);
        parent.parent.levelManager.setLevel(1);
       // parent.parent.elapsedTime = 0;
    }

    @Override
    public void dispose() {
       // skin.dispose();
        stage.dispose();;
        batch.dispose();
        backgroundBatch.dispose();
        menuBatch.dispose();
    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return backgroundCamera;
    }


    /* Private Methods */

    private void setupButtons(){
        startButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //System.out.println("StartButtonClicked");
               // parent.parent.levelManager.setLevel(1);
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEVEL_SELECT);
            }
        };
        leaderboardButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //System.out.println("LeaderBoardButtonClicked");
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.LEADERBOARDS);
            }
        };
        menuButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //System.out.println("MenuButtonButtonClicked");
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.MENU);
            }
        };
        startButton = new ImageTextButton("Start", skin);
        startButton.addListener(startButtonListener);

        leaderboardButton = new ImageTextButton("Leader Boards", skin);
        leaderboardButton.addListener(leaderboardButtonListener);
        leaderboardButton.setWidth(leaderboardButton.getWidth()+10);

        startButton.setWidth(leaderboardButton.getWidth());

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.addListener(menuButtonListener);

    }

    private void setupTables(){
        mainTable = new Table();
        mainTable.setDebug(parent.parent.devMode);
        mainTable.setWidth(menuViewport.getScreenWidth());
        mainTable.setFillParent(true);
        mainTable.align(Align.left|Align.top);
        mainTable.setPosition(0, menuViewport.getScreenHeight());

        headerTable = new Table();
        headerTable.setDebug(parent.parent.devMode);
        headerTable.align(Align.center|Align.top);
        headerTable.setWidth(mainTable.getWidth());

        headerTable.add(menuButton).padLeft(spacer).padRight(menuViewport.getScreenWidth()/3).padTop(0).align(Align.left).size(menuViewport.getScreenWidth()/8, menuViewport.getScreenHeight()/10);


        stage.addActor(titleLabel1);
        stage.addActor(titleLabel2);
        stage.addActor(titleLabel3);
        stage.addActor(titleLabel4);
        stage.addActor(startButton);
        stage.addActor(leaderboardButton);

        mainTable.add(headerTable);
        mainTable.row();

    }

    private void setupSkin(){
        //TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        //skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
        skin = parent.parent.resourceManager.getSkin();
    }

    private void setupLabels(){
        BitmapFont titleFont = FontGenerator.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 60);




        titleLabel1 = new Label("A", skin, "title");
        titleLabel1.getStyle().font = titleFont;
        titleLabel2 = new Label("Race", skin, "title");
        titleLabel3 = new Label("In", skin, "title");
        titleLabel4 = new Label("Space", skin, "title");

    }

    /* Public Methods */

    @Override
    public void setup() {
        backgroundCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch = new SpriteBatch();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);

        menuCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuBatch = new SpriteBatch();
        menuBatch.setProjectionMatrix(menuCamera.combined);

        menuViewport = new ScreenViewport(menuCamera);

        stage = new Stage(menuViewport, menuBatch);
        spacer = 25;

        setupSkin();
        setupButtons();
        setupLabels();
        setupTables();

        stage.addActor(mainTable);
        parent.parent.inputManager.addInputProcessor(stage);
        menuBatch.enableBlending();
       // monetizationController.loadBannerAd();


    }

    @Override
    public void render(float elapsedTime) {


       // if(monetizationController.isBannerAdShowing()) monetizationController.hideBannerAd();
        mainTable.setDebug(parent.parent.devMode);
        headerTable.setDebug(parent.parent.devMode);
        //titleTable.setDebug(parent.parent.devMode);

        Player p = parent.parent.levelManager.getPlayer();
        ArrayList<Planet> planets = parent.parent.levelManager.getPlanets();
        camera.zoom = parent.getCameraZoom();
        backgroundCamera.zoom = parent.getCameraZoom();
        camera.position.set(p.getX()+(p.getWidth()/2), p.getY()+p.getWidth()/2, 0);
        camera.setToAngle(p.getPhysics().getBody().getAngle());
        camera.update();

        mainTable.setPosition((p.getX()+p.getWidth()/2)-Gdx.graphics.getWidth()/2, (p.getY()+p.getHeight()/2)-Gdx.graphics.getHeight()/2); //reposition the table

        float titleLabelX = camera.position.x-menuViewport.getScreenWidth()/4;
        float titleLabelY = camera.position.y+menuViewport.getScreenHeight()/3;

        titleLabel1.setPosition(titleLabelX,titleLabelY);
        titleLabel2.setPosition(titleLabelX+6,titleLabelY-titleLabel2.getHeight()/2-6);
        titleLabel3.setPosition(titleLabelX+70,titleLabelY-titleLabel2.getHeight()-12);
        titleLabel4.setPosition(titleLabelX+70,titleLabelY-(titleLabel2.getHeight() * 1.5f)-18);

        startButton.setPosition(camera.position.x-startButton.getWidth()/2, titleLabelY-(titleLabel2.getHeight() * 2f)-24);
        leaderboardButton.setPosition(camera.position.x-leaderboardButton.getWidth()/2, startButton.getY() - startButton.getHeight()-6);



        backgroundCamera.position.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2, 0);
        backgroundCamera.setToAngle(p.getPhysics().getBody().getAngle());
        backgroundCamera.update();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        batch.setProjectionMatrix(camera.combined);

        menuCamera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0);
       // menuCamera.setToAngle(p.getPhysics().getBody().getAngle());
        menuCamera.update();

        //clear screen
       Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(.447f, .2784f, .3843f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);



        parent.renderBackground(backgroundCamera, elapsedTime, backgroundBatch);



        stage.act(Gdx.graphics.getDeltaTime());

        /**
         * These blending lines are needed before stage.draw() to allow the font to blend in
         */
        menuBatch.enableBlending();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.begin();

        parent.renderPlanets(planets, elapsedTime, batch);
        parent.renderPlayer(p, elapsedTime, batch);
        batch.end();
        stage.draw();

        /**
         * These Blending lines are needed after stage.draw() to allow the font to blend.
         */
        Gdx.gl.glDisable(GL20.GL_BLEND);
        menuBatch.disableBlending();

        if(!parent.parent.levelManager.getPlayer().getInput().rightPressed){
            GameInput input;
            input = GameInput.RIGHT_PRESSED;
            sendEvent(new Event(Event.TYPE.INPUT, "PlayerInput", input));

        }
    }



    public void setCameraZoom(float zoom){
        backgroundCamera.zoom = zoom;
        camera.zoom = zoom;
    }

    @Override
    public void sendEvent(Event e) {
        EventDispatcher.getSingletonDispatcher().dispatch(e);
    }
}