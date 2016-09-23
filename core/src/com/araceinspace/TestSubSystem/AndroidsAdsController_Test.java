package com.araceinspace.TestSubSystem;

import com.araceinspace.InputSubSystem.GameInputListener;
import com.araceinspace.MonetizationSubSystem.AdsController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 9/18/16.
 * A Test libgdx application that allows us
 * to test the AndroidAdsControllers several functions.
 */
public class AndroidsAdsController_Test extends ApplicationAdapter{
    public AdsController adsController;
    SpriteBatch batch;
    Texture img;
    int xCoords = 0;

    public AndroidsAdsController_Test(AdsController adsController){
        this.adsController = adsController;
    }

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
       // adsController.loadBannerAd();
       // adsController.loadInterstitialAd();
        Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(this)));
    }

    @Override
    public void render () {
        xCoords++;
        if(xCoords >= Gdx.graphics.getWidth())xCoords = 0;
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, xCoords, 0);
        batch.end();
        adsController.updateVisibility();
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }


}
