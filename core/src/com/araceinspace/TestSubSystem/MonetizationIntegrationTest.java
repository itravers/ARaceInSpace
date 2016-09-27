package com.araceinspace.TestSubSystem;

import com.araceinspace.InputSubSystem.GameInputListener;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Isaac Assegai on 9/26/16.
 * Tests the Google Play Monetization system in coorelation with the libgdx system.
 * We will show a test page that allows the user to load and view ads,
 * and allows the user to view a video ad for a credit.
 * We also give the user the ability to buy a credit via in-app purchasing.
 */
public class MonetizationIntegrationTest extends ApplicationAdapter{
    ToastInterface toastInterface;
    public boolean showToast = false;
    public boolean toastSet = false;
    public MonetizationController monetizationController;
    SpriteBatch batch;
    Texture img;
    int xCoords = 0;

    int credits = 0; //the num of in-game test credits


    /**
     * Used to contain all the gui items.
     */
    Stage guiStage;



    public MonetizationIntegrationTest(MonetizationController monetizationController, ToastInterface toastInterface){
        this.monetizationController = monetizationController;
        this.toastInterface = toastInterface;
    }

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        //Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(this)));
        setupGUI();
    }

    /**
     * Creates all the buttons and labels and such we use on this test
     */
    public void setupGUI(){
        guiStage = new Stage();
        Gdx.input.setInputProcessor(guiStage);
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");
        TextButton button = new TextButton("Button1", textButtonStyle);
        button.setPosition(20, 20);
        guiStage.addActor(button);
    }

    @Override
    public void render () {
        xCoords++;
        if(xCoords >= Gdx.graphics.getWidth()){
            xCoords = 0;
        }
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, xCoords, 0);
        batch.end();
        monetizationController.updateVisibility();
        guiStage.draw();

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