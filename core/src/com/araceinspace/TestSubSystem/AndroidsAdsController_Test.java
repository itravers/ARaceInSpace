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

    public AndroidsAdsController_Test(AdsController adsController){
        this.adsController = adsController;



        /** Now have our ads controller setupAds(). */
       // adsController.setupAds();

       // adsController.loadBannerAd();
       // adsController.loadInterstitialAd();
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
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        adsController.updateVisibility();
        //System.out.println("render thread is:" + Thread.currentThread().getName());

        /* show a banner ad if:
         *banner is loaded
         * banner is not showing,
         * adTimer is more than 10
         */
       /* if(adsController.isBannerLoaded() && !adsController.isBannerAdShowing() && !adsController.isInterstitialAdShowing() && adsController.getStateTime() > 10){
            System.out.println("game ads : timer: " + adsController.getStateTime());
            System.out.println("game ads BANNERADDTEST GOOD, SHOW BANNER AD NOW");
            adsController.showBannerAd();
            adsController.setStateTime(0);

        }

        if(adsController.isInterstitialAdLoaded() && adsController.isBannerAdShowing() && adsController.getStateTime() > 10){
            System.out.println("game ads : timer: " + adsController.getStateTime());
            System.out.println("game ads INTERSTITIALTEST GOOD, SHOW INTERSTITIAL AD NOW");
            adsController.hideBannerAd();
            adsController.loadBannerAd();
            adsController.showInterstitialAd();
            adsController.setStateTime(0);
        }*/

        //if(((int)adTimer) % 2 == 0)System.out.println("game ads : timer: " + adTimer);

        //adTimer += Gdx.graphics.getDeltaTime();
       // adsController.setStateTime(adsController.getStateTime()+Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }


}
