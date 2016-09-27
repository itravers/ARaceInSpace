package com.araceinspace.TestSubSystem;

import com.araceinspace.InputSubSystem.GameInputListener;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by Isaac Assegai on 9/18/16.
 * A Test libgdx application that allows us
 * to test the AndroidAdsControllers several functions.
 */
public class AndroidsMonetizationController_Test extends ApplicationAdapter{
    ToastInterface toastInterface;
    public boolean showToast = false;
    public boolean toastSet = false;
    public MonetizationController monetizationController;
    SpriteBatch batch;
    Texture img;
    int xCoords = 0;

    int credits = 0; //the num of in-game test credits



    public AndroidsMonetizationController_Test(MonetizationController monetizationController, ToastInterface toastInterface){
        this.monetizationController = monetizationController;
        this.toastInterface = toastInterface;
    }

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
       // Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(this)));
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
