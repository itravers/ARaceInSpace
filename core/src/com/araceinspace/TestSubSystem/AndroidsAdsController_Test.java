package com.araceinspace.TestSubSystem;

import com.araceinspace.MonetizationSubSystem.AdsController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Isaac Assegai on 9/18/16.
 * A Test libgdx application that allows us
 * to test the AndroidAdsControllers several functions.
 */
public class AndroidsAdsController_Test extends ApplicationAdapter{
    AdsController adsController;
    SpriteBatch batch;
    Texture img;
    float adTimer = 0;

    public AndroidsAdsController_Test(AdsController adsController){
        this.adsController = adsController;

        /** Now have our ads controller setupAds(). */
        adsController.setupAds();

        //adsController.loadBannerAd();
        adsController.loadInterstitialAd();
    }

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        adsController.loadBannerAd();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

        /* show a banner ad if:
         *
         */
        if(adsController.isBannerLoaded() && !adsController.isBannerAdShowing() && adTimer > 10){
            adsController.showBannerAd();
            adTimer = 0;
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}
