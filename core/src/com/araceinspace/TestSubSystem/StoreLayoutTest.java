package com.araceinspace.TestSubSystem;

import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
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
    //Stage guiStage;

   // SpriteBatch batch;
    SpriteBatch portraitBatch;
    SpriteBatch landscapeBatch;

    Stage portraitStage;
    Stage landscapeStage;

    ClickListener coinButtonListener;
    boolean stageLoaded;



    public StoreLayoutTest(MonetizationController monetizationController, ToastInterface toastInterface){
        this.monetizationController = monetizationController;
        this.toastInterface = toastInterface;
    }

    @Override
    public void create () {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera); //holy crap, this whole time we just needed a screenviewport?

        //viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
       // batch = new SpriteBatch();
        landscapeBatch = new SpriteBatch();
        portraitBatch = new SpriteBatch();

        img = new Texture("isaac.png");
       // landscapeStage = new Stage(viewport, landscapeBatch);
        //portraitStage = new Stage(viewport, portraitBatch);
        orientation = getNewOrientation(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
       // updateOrientation(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
               // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                System.out.println("Button Clicked: " + event);
            }

        };
       // checkOrientation();



        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
         skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);


        BitmapFont font = skin.getFont("default-font");
        font.getData().setScale(.13f, .66f);


        spacer = 25;

        //img.
        //Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(this)));



    }

    /**
     * Called when orientation is changed to setup correct input processor
     */
   /* private void checkOrientation(){
        if(orientation == Orientation.landscape){
            Gdx.input.setInputProcessor(landscapeStage);
        }else{
            // guiStage = new Stage(viewport, batch);
            Gdx.input.setInputProcessor(portraitStage);
        }
    }*/







    @Override
    public void render () {
       // table.setDebug(false);
       // headerTable.setDebug(false);
        //bodyTable.setDebug(false);

        //coinLabel.setText("27");


        //update credits on screen
       // updateGUI();


        xCoords++;
        if(xCoords >= Gdx.graphics.getWidth()){
            xCoords = 0;
        }
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       // batch.begin();

        //batch.draw(img, xCoords, 150);

       // batch.end();

        //only update the correct stage based on orientation.
        if(orientation == Orientation.landscape && stageLoaded){
           // System.out.println("drawing landscape");
            Gdx.input.setInputProcessor(landscapeStage);
            landscapeStage.act(Gdx.graphics.getDeltaTime());
            landscapeStage.draw();
        }else if(orientation == Orientation.portrait && stageLoaded){
            // guiStage = new Stage(viewport, batch);
           // System.out.println("drawing portrait");
            Gdx.input.setInputProcessor(portraitStage);
            portraitStage.act(Gdx.graphics.getDeltaTime());
            portraitStage.draw();
        }


        monetizationController.updateVisibility();//used for banner ads to know whether to show


    }


    @Override
    public void resize(int width, int height){
        System.out.println("2resize width:height "+ width + ":" + height);
        super.resize(width, height);
        System.out.println("viewPort was: " + viewport.getScreenWidth() + ":" +viewport.getScreenHeight());
        viewport.setScreenSize(width, height);
        System.out.println("viewPort is: " + viewport.getScreenWidth() + ":" +viewport.getScreenHeight());


        updateOrientation(width, height);


        //the only time we should be calleding setupGUI
        if(orientation == Orientation.landscape){
            setupGUI(viewport.getScreenWidth(), viewport.getScreenHeight(), Orientation.landscape);
            //setupGUI(viewport.getScreenHeight(), viewport.getScreenWidth(), Orientation.portrait);
        }else{
            setupGUI(viewport.getScreenWidth(), viewport.getScreenHeight(), Orientation.portrait);
            //setupGUI(viewport.getScreenHeight(), viewport.getScreenWidth(), Orientation.landscape);
        }



        //on change to an orientation, the former orientation is destroyed and rebuilt
       /* if(orientation == Orientation.landscape){
            //landscapeStage.dispose();
            landscapeStage = new Stage(viewport, landscapeBatch);

            setupLandscapeGUI((float)width, (float)height);
        }else{
            //portraitStage.dispose();
            portraitStage = new Stage(viewport, portraitBatch);
            setupPortraitGUI((float)width, (float)height);
        }

        super.resize(width, height);
        viewport.setScreenSize(width, height);
        setOrientation(width, height);

        System.out.println("Resize to : " + width + " : " + height + " " + orientation);

        checkOrientation();
       // setChangingObjectsSize(width, height);

       // System.out.println("setup portrait    stage w:h " + stage.getWidth() + ":" + stage.getHeight());
        System.out.println("resize viewport w:h " + viewport.getScreenWidth() + ":" + viewport.getScreenHeight());

        */


    }

    public void updateOrientation(int width, int height){
        Orientation newOrientation = getNewOrientation(width, height);
        if(orientation != newOrientation){
            if(newOrientation == Orientation.landscape){
                System.out.println("setOrientation landscape");
                orientation = newOrientation;
                //setupGUI(width, height, newOrientation); we moved this to the first initialzer
               // Gdx.input.setInputProcessor(landscapeStage);
            }else{
                System.out.println("setOrientation portrait");
                orientation = newOrientation;
                //setupGUI(width, height, newOrientation); // we moved this to the first intiializer.
                //Gdx.input.setInputProcessor(portraitStage);
            }

        }

    }

    public Orientation getNewOrientation(int width, int height){
        Orientation newOrientation;
        if(width >= height){
            newOrientation = Orientation.landscape;
        }else{
            newOrientation = Orientation.portrait;
        }
        return newOrientation;
    }


    public void toast(final String t){
        toastInterface.toast(t);
    }

    @Override
    public void dispose () {
       // batch.dispose();
        landscapeBatch.dispose();
        portraitBatch.dispose();
        img.dispose();
    }

    /**
     * Creates all the buttons and labels and such we use on this test
     */
    public void setupGUI(int width, int height, Orientation orient){
        System.out.println("setupGUI() graphics w:h " + width + ":" + height + " g:" + Gdx.graphics.getWidth() + ":" + Gdx.graphics.getHeight() + " Viewport: " + viewport.getScreenWidth() + ":" + viewport.getScreenHeight());
        //setup the landscapeStage and portraitStage with opposite height widths
        if(orient == Orientation.landscape){
            landscapeStage = new Stage(viewport, landscapeBatch);
            setupLandscapeGUI(width, height);
            //setupPortraitGUI(landscapeStage.getHeight(), landscapeStage.getWidth());
        }else{
            portraitStage = new Stage(viewport, landscapeBatch);
            setupPortraitGUI(width,height);
            // setupLandscapeGUI(portraitStage.getHeight(),  portraitStage.getWidth());
        }
    }

    public void setupPortraitGUI(float width, float height){
        stageLoaded = false;
        float butWidth = width/2.6f;
        float butHeight = height/3.955f;


        System.out.println("setup portrait    stage w:h " + width + ":" + height);
        // System.out.println("setup portrait viewport w:h " + viewport.getScreenWidth() + ":" + viewport.getScreenHeight());
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
        table.setDebug(true);
        table.setWidth(width);
        table.align(Align.center|Align.top);
        table.setPosition(0, height);


        backButton = new ImageButton(skin, "backButton");
        backButton.setDebug(false);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(false);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.addListener(coinButtonListener);
        rewardButton.setDebug(false);


        System.out.println("density: portrait, " + Gdx.graphics.getDensity());
        storeTitleLabel = new Label("STORE", skin, "title");
        //storeTitleLabel
        //storeTitleLabel.setFontScale(Gdx.graphics.getDensity()/2);
        storeTitleLabel.setDebug(false);
        //storeTitleLabel.getStyle().
        //System.out.println("xheight: " + storeTitleLabel.getStyle().font.getXHeight() + " labelheight: " + storeTitleLabel.getHeight());
       // float scale = storeTitleLabel.getHeight()/storeTitleLabel.getStyle().font.getXHeight();
        //storeTitleLabel.setFontScale(scale);





        coinLabel = new Label("25", skin, "coinLabel");

        coinButton = new ImageButton(skin, "coinButton");

        coinButton.addListener(coinButtonListener);

        headerTable = new Table();
        headerTable.setDebug(false);
        headerTable.align(Align.center|Align.top);



        headerTable.add(backButton).padLeft(spacer).padTop(spacer/2).size(width/12, height/12);
        headerTable.add(menuButton).padLeft(spacer).padTop(spacer/2).align(Align.left).size(width/8, height/8);
        headerTable.add(storeTitleLabel).expandX().align(Align.center).size(width/3, height/8);
       // System.out.println("xheight: " + storeTitleLabel.getStyle().font.getXHeight() + " labelheight: " + storeTitleLabel.getHeight());

        float fontWidth = storeTitleLabel.getStyle().font.getSpaceWidth()*storeTitleLabel.getText().length();
        //float labelWidth = storeTitleLabel.getWidth();
        //float fontScale = labelWidth/fontWidth;
        //storeTitleLabel.setFontScale(fontScale);

        //test update storeTitleLabel based on difference between it's font width etc
        //get item width, get font width scale by itemWidth/fontwidth


        headerTable.add(coinLabel).size(width/11, height/10).align(Align.right);
        headerTable.add(coinButton).size(width/6, height/10).padTop(spacer).padRight(spacer);

        table.add(headerTable).fill().expandX();
        table.row();


        bodyTable = new Table();
        bodyTable.setDebug(false);
        bodyTable.add(rewardButton).size(width/8, height/12).padLeft(spacer/2).padTop(spacer/4).align(Align.top).spaceLeft(0);


        bodyTable.align(Align.left); //aligns reward video with back button


        buyButtons = new ArrayList<ImageButton>();


        storeTable = new Table();
        storeTable.setDebug(false);
        storeTable.align(Align.top|Align.left);



        Stack buttonStack = makeButtonStack(width, height, "Buy 15", "$0.99", "Like Winning A Challenge", "You", "NO TALENT BUM!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);//sets the button size

        buttonStack = makeButtonStack(width, height, "Buy 30", "$1.50", "You Can Compete", "In 3", "Leaderboard Challenges", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);//sets the button size

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Buy 100", "$2.99", "More than 3 times the", "Coins! - Less Than", "Twice the Cost!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Buy 5000", "$9.99", "Maybe you can actually", "Win A Challenge", "HUH???", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Remove Ads", "$1.99", "Turn Those", "SUCKERS", "OFF!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Unlock Everything", "$29.99", "Everything is YOURS", "No More Ads!!!", "Unlimited Coins!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Buy 15", "$0.99", "Like Winning A Challenge", "You", "NO TALENT BUM!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);//sets the button size

        buttonStack = makeButtonStack(width, height, "Buy 30", "$1.50", "You Can Compete", "In 3", "Leaderboard Challenges", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);//sets the button size

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Buy 100", "$2.99", "More than 3 times the", "Coins! - Less Than", "Twice the Cost!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Buy 5000", "$9.99", "Maybe you can actually", "Win A Challenge", "HUH???", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Remove Ads", "$1.99", "Turn Those", "SUCKERS", "OFF!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Unlock Everything", "$29.99", "Everything is YOURS", "No More Ads!!!", "Unlimited Coins!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);


        scrollPane = new ScrollPane(storeTable, skin, "default");


        bodyTable.add(scrollPane).width(width*.84f).height(height*.79f).padLeft(0).align(Align.top);//set the scroll pane size

        table.add(bodyTable).fill().expandX();

        portraitStage.addActor(table);
        stageLoaded = true;
    }

    public void setupLandscapeGUI(float width, float height){
        stageLoaded = false;
        System.out.println("setup landscape    stage w:h " + width + ":" + height);
        float butWidth = width/4f;

        float butHeight = height/3f;


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
        table.setDebug(true);
        table.setWidth(width);
        table.align(Align.center|Align.top);
        table.setPosition(0, height);


        backButton = new ImageButton(skin, "backButton");
        backButton.setDebug(false);

        menuButton = new ImageButton(skin, "menuButton");
        menuButton.setDebug(false);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.addListener(coinButtonListener);
        rewardButton.setDebug(false);


        System.out.println("density: landscape, " + Gdx.graphics.getDensity());
        storeTitleLabel = new Label("STORE", skin, "title");
        storeTitleLabel.setDebug(false);

        coinLabel = new Label("25", skin, "coinLabel");

        coinButton = new ImageButton(skin, "coinButton");

        coinButton.addListener(coinButtonListener);

        headerTable = new Table();
        headerTable.setDebug(false);
        headerTable.align(Align.center|Align.top);



        headerTable.add(backButton).padLeft(spacer).padTop(spacer/2).size(width/20, height/8);
        headerTable.add(menuButton).padLeft(spacer).padTop(spacer/2).align(Align.left).size(width/12, height/8);
        headerTable.add(storeTitleLabel).expandX().align(Align.center).size(width/5, height/8);
        headerTable.add(coinLabel).size(width/15, height/10).align(Align.right);
        headerTable.add(coinButton).size(width/9, width/9).padTop(spacer).padRight(spacer);

        table.add(headerTable).fill().expandX();
        table.row();


        bodyTable = new Table();
        bodyTable.setDebug(false);
        bodyTable.add(rewardButton).size(width/12, height/8).padLeft(spacer).padTop(spacer/2).align(Align.top).spaceLeft(0);


        bodyTable.align(Align.left); //aligns reward video with back button


        buyButtons = new ArrayList<ImageButton>();


        storeTable = new Table();
        storeTable.setDebug(false);
        storeTable.align(Align.top|Align.left);

        Stack buttonStack = makeButtonStack(width, height, "Buy 15", "$0.99", "Like Winning A Challenge", "You", "NO TALENT BUM!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Buy 30", "$1.50", "You Can Compete", "In 3", "Leaderboard Challenges", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Buy 100", "$2.99", "More than 3 times the", "Coins! - Less Than", "Twice the Cost!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Buy 5000", "$9.99", "Maybe you can actually", "Win A Challenge", "HUH???", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Remove Ads", "$1.99", "Turn Those", "SUCKERS", "OFF!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Unlock Everything", "$29.99", "Everything is YOURS", "No More Ads!!!", "Unlimited Coins!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Buy 15", "$0.99", "Like Winning A Challenge", "You", "NO TALENT BUM!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Buy 30", "$1.50", "You Can Compete", "In 3", "Leaderboard Challenges", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Buy 100", "$2.99", "More than 3 times the", "Coins! - Less Than", "Twice the Cost!!!", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        storeTable.row();

        buttonStack = makeButtonStack(width, height, "Buy 5000", "$9.99", "Maybe you can actually", "Win A Challenge", "HUH???", true);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Remove Ads", "$1.99", "Turn Those", "SUCKERS", "OFF!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);

        buttonStack = makeButtonStack(width, height, "Unlock Everything", "$29.99", "Everything is YOURS", "No More Ads!!!", "Unlimited Coins!!!", false);
        storeTable.add(buttonStack).pad(spacer/4).size(butWidth, butHeight);


        scrollPane = new ScrollPane(storeTable, skin, "default");


        bodyTable.add(scrollPane).width(width*.85f).height(height*.70f).padLeft(0).align(Align.top);//set the scroll pane size

        table.add(bodyTable).fill().expandX();

        landscapeStage.addActor(table);
        stageLoaded = true;

    }

    private Stack makeButtonStack(float width, float height, String title, String price, String s_taunt1, String s_taunt2, String s_taunt3, boolean showImage){
        //create buttons to buy iap
        final Button storeTestButton = new Button(skin, "white");
        storeTestButton.setColor(Color.WHITE);

        //create stuff to put in table button
        Label titleLabel = new Label(title, skin, "button_title");
        titleLabel.setTouchable(Touchable.disabled);
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

        //create table in buttons
        Table purchaseTable = new Table();
        purchaseTable.setDebug(false);
        purchaseTable.align(Align.center);

        //Header for purchase table
        Table purchaseHeaderTable = new Table();
        purchaseHeaderTable.setDebug(false);
        purchaseHeaderTable.align(Align.top|Align.center);
        purchaseHeaderTable.add(titleLabel).padTop(spacer/4).padRight(spacer/4).align(Align.left);//.size(width/8, stage.getHeight()/20);
        if(showImage)purchaseHeaderTable.add(button_image).padTop(0).size(width/13, width/13).align(Align.top);
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

}
