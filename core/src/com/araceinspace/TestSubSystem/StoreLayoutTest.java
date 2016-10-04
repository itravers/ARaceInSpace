package com.araceinspace.TestSubSystem;

import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Isaac Assegai on 10/3/16.
 * Tests the scene2d.ui store layout i'm planning
 */
public class StoreLayoutTest  extends ApplicationAdapter {
    private Viewport viewport;
    private Camera camera;


    ToastInterface toastInterface;
    public boolean showToast = false;
    public boolean toastSet = false;
    public MonetizationController monetizationController;
    SpriteBatch batch;
    Texture img;
    int xCoords = 0;

    boolean debugger = false;


    //scene2d.ui items
    Table table;
    Table headerTable;
    Table bodyTable;

    ScrollPane scrollPane;

    Label titleLabel;
    ImageButton backButton;
    ImageButton menuButton;
    Label coinLabel;
    ImageButton coinButton;
    ImageButton rewardButton;

    ArrayList<ImageButton>buyButtons;





    /**
     * Used to contain all the gui items.
     */
    Stage guiStage;



    public StoreLayoutTest(MonetizationController monetizationController, ToastInterface toastInterface){
        this.monetizationController = monetizationController;
        this.toastInterface = toastInterface;
    }

    @Override
    public void create () {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        //viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        img = new Texture("isaac.png");
        //img.
        //Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(this)));
        setupGUI(viewport, batch);
    }

    /**
     * Creates all the buttons and labels and such we use on this test
     */
    public void setupGUI(Viewport viewPort, SpriteBatch batch){
        guiStage = new Stage(viewPort, batch);
        Gdx.input.setInputProcessor(guiStage);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);


        BitmapFont font = skin.getFont("default-font");
        font.getData().setScale(.13f, .66f);


        int spacer = 25;
        int buttonHeight = Gdx.graphics.getHeight()/16;

        table = new Table();
        table.setWidth(guiStage.getWidth());
       // table.setFillParent(true);
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());

        backButton = new ImageButton(skin, "backButton");
        menuButton = new ImageButton(skin, "menuButton");
        rewardButton = new ImageButton(skin, "rewardButton");


        titleLabel = new Label("STORE", skin, "title");
        coinLabel = new Label("25", skin, "coinLabel");
        coinButton = new ImageButton(skin, "coinButton");

        headerTable = new Table();
       // headerTable.setWidth(guiStage.getWidth());
        headerTable.align(Align.center|Align.top);


        headerTable.add(backButton).padLeft(spacer);
        headerTable.add(menuButton).padLeft(spacer).align(Align.left);
        headerTable.add(titleLabel).expandX().align(Align.center);
        headerTable.add(coinLabel);
        headerTable.add(coinButton).padTop(spacer).padRight(spacer);

        table.add(headerTable).fill().expandX();
        table.row();


        bodyTable = new Table();
        bodyTable.add(rewardButton).padLeft(spacer).padTop(spacer/2).align(Align.left|Align.top);



        bodyTable.align(Align.left); //aligns reward video with back button


        buyButtons = new ArrayList<ImageButton>();

       // bodyTable.add(new ImageButton(skin, "storeButton")).padLeft(spacer);
        //bodyTable.add(new ImageButton(skin, "storeButton")).padLeft(spacer);
        //bodyTable.add(new ImageButton(skin, "storeButton")).padLeft(spacer);

        Table storeTable = new Table();
        storeTable.align(Align.top|Align.left);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);

        scrollPane = new ScrollPane(storeTable, skin, "default");

        //scrollPane.setColor(255, 255, 255, 255);
       // scrollPane.setHeight(800);


        bodyTable.add(scrollPane).width(guiStage.getWidth()*.85f).height(guiStage.getHeight()*.73f).padLeft(spacer/2).align(Align.top);
       // scrollPane.setSize(b.getWidth(), b.getHeight());
        //table.row();
        table.add(bodyTable).fill().expandX();
        //table.add(scrollPane).fill().expandX();
       // table.add(scrollPane);





        //table.setDebug(true);
        //innerTable.setDebug(true);




        guiStage.addActor(table);
    }

    @Override
    public void render () {

        table.setDebug(false);
        headerTable.setDebug(false);
        bodyTable.setDebug(false);
        //table.debug(Table.Debug.actor);
        //able.getCell(backButton).expandX();
       // backButton.align(Align.left);
        //table.getCell(backButton).align(Align.left);
        //coinButton.setPosition(table.getWidth()-coinButton.getWidth(), coinButton.getY());

        coinLabel.setText("25");
       // table.getCell(coinButton)


        //update credits on screen
        updateGUI();


        xCoords++;
        if(xCoords >= Gdx.graphics.getWidth()){
            xCoords = 0;
        }
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        //batch.draw(img, xCoords, 150);

        batch.end();

        guiStage.act(Gdx.graphics.getDeltaTime());

        guiStage.draw();

        monetizationController.updateVisibility();//used for banner ads to know whether to show


    }

    private void updateGUI(){
        //update changing labels
       // super.res

    }


    @Override
    public void resize(int width, int height){
        super.resize(width, height);
       // viewport.setWorldSize(width, height);
        //

        viewport.setScreenSize(width, height);
        //guiStage.getCamera().position.set(width/2, height/2, guiStage.getCamera().position.z);
    }





    public void toast(final String t){
        toastInterface.toast(t);
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}
