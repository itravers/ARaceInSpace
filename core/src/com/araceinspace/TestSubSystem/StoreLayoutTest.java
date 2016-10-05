package com.araceinspace.TestSubSystem;

import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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



    ArrayList<ImageButton>buyButtons;


    Skin skin;
    int spacer;



    enum Orientation {portrait, landscape}

    public Orientation orientation;

    /**
     * Used to contain all the gui items.
     */
    Stage guiStage;

    Stage portraitStage;
    Stage landscapeStage;

    ClickListener coinButtonListener;



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
        landscapeStage = new Stage(viewport, batch);
        portraitStage = new Stage(viewport, batch);
        setOrientation((int)landscapeStage.getWidth(), (int)landscapeStage.getHeight());
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
               // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                System.out.println("Button Clicked: " + event);
            }

        };
        if(orientation == Orientation.landscape){
            //guiStage = new Stage(viewport, batch);
            guiStage = landscapeStage;//new Stage(viewport, batch);
            Gdx.input.setInputProcessor(guiStage);
        }else{
           // guiStage = new Stage(viewport, batch);
            guiStage = portraitStage;//new Stage(viewport, batch);
            Gdx.input.setInputProcessor(guiStage);
        }



        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
         skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);


        BitmapFont font = skin.getFont("default-font");
        font.getData().setScale(.13f, .66f);


        spacer = 25;

        //img.
        //Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(this)));
        setupGUI(viewport, batch, orientation);
    }

    /**
     * Creates all the buttons and labels and such we use on this test
     */
    public void setupGUI(Viewport viewPort, SpriteBatch batch, Orientation orientation){


        landscapeStage = setupLandscapeGUI(viewPort, batch);
       // portraitStage = setupLandscapeGUI(viewPort, batch);
    }

    /*
    private Stage setupPortraitGUI(Viewport viewport, SpriteBatch batch){
        Table storeTable;
        Stack buttonStack;

        //scene2d.ui items
        Table table;
        Table headerTable;
        Table bodyTable;

        ScrollPane scrollPane;

        Label storeTitleLabel;
        ImageButton backButton;
        ImageButton menuButton;
        Label coinLabel;
        ImageButton coinButton;


        ImageButton rewardButton;


        table = new Table();

        table.setWidth(landscapeStage.getWidth());
        // table.setFillParent(true);
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());


        backButton = new ImageButton(skin, "backButton");
        menuButton = new ImageButton(skin, "menuButton");
        rewardButton = new ImageButton(skin, "rewardButton");


        storeTitleLabel = new Label("STORE", skin, "title");
        System.out.println("density: " + Gdx.graphics.getDensity());
        storeTitleLabel.scaleBy(Gdx.graphics.getDensity());
        coinLabel = new Label("25", skin, "coinLabel");
        coinButton = new ImageButton(skin, "coinButton");
        coinButton.addListener(coinButtonListener);

        headerTable = new Table();
        // headerTable.setWidth(guiStage.getWidth());
        headerTable.align(Align.center|Align.top);


        headerTable.add(backButton).padLeft(spacer);
        headerTable.add(menuButton).padLeft(spacer).align(Align.left);
        headerTable.add(storeTitleLabel).expandX().align(Align.center);
        headerTable.add(coinLabel);
        headerTable.add(coinButton).padTop(spacer).padRight(spacer);

        table.add(headerTable).fill().expandX();
        table.row();


        bodyTable = new Table();
        bodyTable.add(rewardButton).padLeft(spacer).padTop(spacer/2).align(Align.left|Align.top);



        bodyTable.align(Align.left); //aligns reward video with back button


        buyButtons = new ArrayList<ImageButton>();


        //create buttons to buy iap
        final Button storeTestButton = new Button(skin, "white");
        storeTestButton.setColor(Color.WHITE);

        //create stuff to put in table button
        Label titleLabel = new Label("Buy  15", skin, "button_title");
        titleLabel.setTouchable(Touchable.disabled);

        ImageButton button_image = new ImageButton(skin, "coinButton_small");
        button_image.setTouchable(Touchable.disabled);

        Label priceLabel = new Label("$0.99", skin, "button_title");
        priceLabel.setTouchable(Touchable.disabled);

        Label taunt1 = new Label("Like Winning A Challenge", skin, "taunt_small");
        taunt1.setTouchable(Touchable.disabled);

        Label taunt2 = new Label("You", skin, "taunt_small");
        taunt2.setTouchable(Touchable.disabled);

        Label taunt3 = new Label("NO TALENT BUM!!!", skin, "taunt_small");
        taunt3.setTouchable(Touchable.disabled);





        //create table in buttons
        Table purchaseTable = new Table();
        purchaseTable.setDebug(false);
        purchaseTable.align(Align.top|Align.center);

        //Header for purchase table
        Table purchaseHeaderTable = new Table();
        purchaseHeaderTable.setDebug(false);
        purchaseHeaderTable.align(Align.top|Align.center);
        purchaseHeaderTable.add(titleLabel).pad(spacer/2);
        purchaseHeaderTable.add(button_image).padLeft(spacer/2).padRight(spacer/2);
        purchaseTable.add(purchaseHeaderTable);
        purchaseTable.row();

        Table purchaseBodyTable = new Table();
        purchaseBodyTable.setDebug(false);
        purchaseBodyTable.align(Align.top|Align.center);
        purchaseBodyTable.add(priceLabel).expandX().padBottom(spacer/2);
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt1).expandX();
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt2).expandX();
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt3).expandX().padBottom(spacer/2);

        purchaseTable.add(purchaseBodyTable);



        buttonStack = new Stack();
        buttonStack.add(storeTestButton);
        buttonStack.add(purchaseTable);



        storeTable = new Table();
        storeTable.align(Align.top|Align.left);
        storeTable.add(buttonStack).pad(spacer/2).width(portraitStage.getWidth()/4).height(portraitStage.getHeight()/3);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();

        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();

        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();

        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();

        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.row();

        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);
        storeTable.add(new ImageButton(skin, "storeButton")).pad(spacer/2);

        scrollPane = new ScrollPane(storeTable, skin, "default");

        //scrollPane.setColor(255, 255, 255, 255);
        // scrollPane.setHeight(800);


        bodyTable.add(scrollPane).width(portraitStage.getWidth()*.85f).height(portraitStage.getHeight()*.73f).padLeft(spacer/2).align(Align.top);
        //table.row();
        table.add(bodyTable).fill().expandX();

        portraitStage.addActor(table);
        return portraitStage;
    }*/

    public Stage setupLandscapeGUI(Viewport viewport, SpriteBatch batch){
        Table storeTable;


        //scene2d.ui items
        Table table;
        Table headerTable;
        Table bodyTable;

        ScrollPane scrollPane;

        Label storeTitleLabel;
        ImageButton backButton;
        ImageButton menuButton;
        Label coinLabel;
        ImageButton coinButton;


        ImageButton rewardButton;


        table = new Table();

        table.setWidth(landscapeStage.getWidth());
        // table.setFillParent(true);
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());


        backButton = new ImageButton(skin, "backButton");
       // backButton.getImage().setScale(Gdx.graphics.getDensity());
        backButton.setDebug(false);

        menuButton = new ImageButton(skin, "menuButton");
        //menuButton.getImage().setScale(Gdx.graphics.getDensity());
        menuButton.setDebug(false);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.addListener(coinButtonListener);
        //rewardButton.size
        //rewardButton.getImage().setScale(Gdx.graphics.getDensity()); //scales image, but doesn't scale the button container
       // rewardButton.setScale(Gdx.graphics.getDensity());//doesn't do anything
        //rewardButton.scaleBy(Gdx.graphics.getDensity()); //doesn't do anything
        //rewardButton.setSize(rewardButton.getWidth()*Gdx.graphics.getDensity(), rewardButton.getHeight()*Gdx.graphics.getDensity()); doesn't do anything
        //rewardButton.pack(); doesn't do anything
        rewardButton.setDebug(false);


        System.out.println("density: landscape, " + Gdx.graphics.getDensity());
        storeTitleLabel = new Label("STORE", skin, "title");
       // storeTitleLabel.setFontScale(Gdx.graphics.getDensity()); //WORKS for rizing font
        storeTitleLabel.setDebug(false);

        coinLabel = new Label("25", skin, "coinLabel");
       // coinLabel.setFontScale(Gdx.graphics.getDensity());

        coinButton = new ImageButton(skin, "coinButton");
       // coinButton.setScale(Gdx.graphics.getDensity()); doesn't scale it
        //coinButton.getImage().setScale(Gdx.graphics.getDensity());
       // coinButton.setScale(Gdx.graphics.getDensity());

        coinButton.addListener(coinButtonListener);

        headerTable = new Table();
        headerTable.setDebug(false);
        headerTable.align(Align.center|Align.top);



        headerTable.add(backButton).padLeft(spacer).padTop(spacer/2).size(landscapeStage.getWidth()/20, landscapeStage.getHeight()/8);
        headerTable.add(menuButton).padLeft(spacer).padTop(spacer/2).align(Align.left).size(landscapeStage.getWidth()/12, landscapeStage.getHeight()/8);
        headerTable.add(storeTitleLabel).expandX().align(Align.center).size(landscapeStage.getWidth()/5, landscapeStage.getHeight()/8);
        headerTable.add(coinLabel).size(landscapeStage.getWidth()/15, landscapeStage.getHeight()/10).align(Align.right);
        headerTable.add(coinButton).size(landscapeStage.getWidth()/9, landscapeStage.getWidth()/9).padTop(spacer).padRight(spacer);

        table.add(headerTable).fill().expandX();
        table.row();


        bodyTable = new Table();
        bodyTable.setDebug(false);
        bodyTable.add(rewardButton).size(landscapeStage.getWidth()/12, landscapeStage.getHeight()/8).padLeft(spacer).padTop(spacer/2).align(Align.top).spaceLeft(0);




        bodyTable.align(Align.left); //aligns reward video with back button


        buyButtons = new ArrayList<ImageButton>();





        storeTable = new Table();
        storeTable.setDebug(false);
        storeTable.align(Align.top|Align.left);

        Stack buttonStack = makeButtonStack("Buy 15", "$0.99", "Like Winning A Challenge", "You", "NO TALENT BUM!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);//sets the button size

        buttonStack = makeButtonStack("Buy 30", "$1.50", "You Can Compete", "In 3", "Leaderboard Challenges", true);
        storeTable.add(buttonStack).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);//sets the button size

        buttonStack = makeButtonStack("Buy 100", "$2.99", "More than 3 times the", "Coins! - Less Than", "Twice the Cost!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);

        storeTable.row();

        buttonStack = makeButtonStack("Buy 5000", "$9.99", "Maybe you can actually", "Win A Challenge", "HUH???", true);
        storeTable.add(buttonStack).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);

        buttonStack = makeButtonStack("Remove Ads", "$1.99", "Turn Those", "SUCKERS", "OFF!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);

        buttonStack = makeButtonStack("Unlock Everything", "$29.99", "Everything is YOURS", "No More Ads!!!", "Unlimited Coins!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);

        //buttonStack = makeButtonStack();
       /* storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.row();
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.row();
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);
        storeTable.add(makeButtonStack()).pad(spacer/4).size(landscapeStage.getWidth()/4, landscapeStage.getHeight()/3);*/


        scrollPane = new ScrollPane(storeTable, skin, "default");

        //scrollPane.setColor(255, 255, 255, 255);
        // scrollPane.setHeight(800);


        bodyTable.add(scrollPane).width(landscapeStage.getWidth()*.85f).height(landscapeStage.getHeight()*.70f).padLeft(0).align(Align.top);//set the scroll pane size
        //table.row();
        table.add(bodyTable).fill().expandX();

        landscapeStage.addActor(table);
        return landscapeStage;
    }

    private Stack makeButtonStack(String title, String price, String s_taunt1, String s_taunt2, String s_taunt3, boolean showImage){
        //create buttons to buy iap
        final Button storeTestButton = new Button(skin, "white");
        storeTestButton.setColor(Color.WHITE);

        //create stuff to put in table button
        Label titleLabel = new Label(title, skin, "button_title");
        titleLabel.setTouchable(Touchable.disabled);
        //titleLabel.setSize(landscapeStage.getWidth()/8, landscapeStage.getHeight()/15);
        titleLabel.setFontScale(.7f); //WORKS for rizing font, but we also change table container

        ImageButton button_image;

            button_image = new ImageButton(skin, "coinButton_small");
            button_image.setTouchable(Touchable.disabled);



        Label priceLabel = new Label(price, skin, "button_title");
        priceLabel.setTouchable(Touchable.disabled);
        priceLabel.setFontScale(.8f);
        //priceLabel.setFontScale(Gdx.graphics.getDensity()); //WORKS for rizing font

        Label taunt1 = new Label(s_taunt1, skin, "taunt_small");
        taunt1.setTouchable(Touchable.disabled);
        taunt1.setFontScale(.8f);
        //taunt1.setFontScale(Gdx.graphics.getDensity());

        Label taunt2 = new Label(s_taunt2, skin, "taunt_small");
        taunt2.setTouchable(Touchable.disabled);
        taunt2.setFontScale(.8f);
        //taunt2.setFontScale(Gdx.graphics.getDensity());

        Label taunt3 = new Label(s_taunt3, skin, "taunt_small");
        taunt3.setTouchable(Touchable.disabled);
        taunt3.setFontScale(.8f);
        //taunt3.setFontScale(Gdx.graphics.getDensity());





        //create table in buttons
        Table purchaseTable = new Table();
        purchaseTable.setDebug(false);
        purchaseTable.align(Align.top|Align.center);

        //Header for purchase table
        Table purchaseHeaderTable = new Table();
        purchaseHeaderTable.setDebug(false);
        purchaseHeaderTable.align(Align.top|Align.center);
        purchaseHeaderTable.add(titleLabel).padTop(spacer/4).padRight(spacer/4).align(Align.left);//.size(landscapeStage.getWidth()/8, landscapeStage.getHeight()/20);
        if(showImage)purchaseHeaderTable.add(button_image).padTop(0).size(landscapeStage.getWidth()/13, landscapeStage.getWidth()/13).align(Align.top);
        purchaseTable.add(purchaseHeaderTable).align(Align.top);
        purchaseTable.row();

        Table purchaseBodyTable = new Table();
        purchaseBodyTable.setDebug(false);
        purchaseBodyTable.align(Align.top|Align.center);
        purchaseBodyTable.add(priceLabel).expandX().padBottom(spacer/8).align(Align.top);
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt1).expandX();
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt2).expandX();
        purchaseBodyTable.row();
        purchaseBodyTable.add(taunt3).expandX().padBottom(spacer/2);

        purchaseTable.add(purchaseBodyTable);


        Stack buttonStack;
        buttonStack = new Stack();
        buttonStack.add(storeTestButton);
        buttonStack.add(purchaseTable);
        return buttonStack;
    }


    @Override
    public void render () {

       // table.setDebug(false);
       // headerTable.setDebug(false);
        //bodyTable.setDebug(false);

        //coinLabel.setText("27");


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

      //  storeTable.getCell()

    }


    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        viewport.setScreenSize(width, height);
        setOrientation(width, height);

        System.out.println("Resize to : " + width + " : " + height + " " + orientation);

        if(orientation == Orientation.landscape){
            guiStage = landscapeStage;//new Stage(viewport, batch);
            Gdx.input.setInputProcessor(guiStage);
            System.out.println(orientation);
        }else{
            guiStage = portraitStage;//new Stage(viewport, batch);
            Gdx.input.setInputProcessor(guiStage);
            System.out.println(orientation);
        }
       // setChangingObjectsSize(width, height);


    }

    public void setOrientation(int width, int height){
        if(width >= height){
            orientation = Orientation.landscape;
        }else{
            orientation = Orientation.portrait;
        }
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
