package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.araceinspace.MonetizationSubSystem.AdsController;
import com.araceinspace.TestSubSystem.AndroidsAdsController_Test;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


/**
 * Created by Isaac Assegai on 9/17/16.
 * The AndroidAdsController is created by the AndroidLauncher
 * and passed to the main Game Controller: ARaceInSpace.java
 * The AndroidAdsController controlls all the ads
 * mediated through the Admob network.
 */
public class AndroidAdsController implements AdsController {

/* Field Variables */
    /**
     * Admob generated Banner ID.
     */
    private static final String BANNED_AD_ID = "ca-app-pub-5553172650479270/1591123946";

    private AndroidBannerAd bannerAd;

    /**
     * Admob generated interstitial id.
     */
    private static final String INTERSTITIAL_AD_ID = "ca-app-pub-5553172650479270/1671849140";

    /**
     * This is set to true while an interstitial ad is currently showing.
     */
    private boolean interstitialAdShowing = false;

    /**
     * used by showInterstitialAd() to decide if the ad has been loaded
     * When loadInterstitialAd() is called this is set to false until
     * a new ad has been loaded.
     */
    private boolean interstitialAdLoaded = false;

    /**
     * This is a reference to the main android app.
     * needed here to add bannerAd to the main apps view.
     */
    AndroidApplication app;

    /**
     * The interstitial ad loaded from google
     */
    private InterstitialAd interstitialAd;



/* Constructors. */

    /**
     * Construct an AndroidAdsController
     * @param app The Application context this controller wil be ran in.
     */
    public AndroidAdsController(AndroidApplication app){
        this.app = app;
    }

/* Private Methods. */

    /**
     * Sets up the banner ads.
     */
    /*
    private void setupBannerAd(){
        bannerAd = new AdView(app);
        bannerAd.setVisibility(View.INVISIBLE);

        bannerAd.setAdUnitId(BANNED_AD_ID);
        bannerAd.setAdSize(AdSize.SMART_BANNER);
    }
    */

    /*
    private void setupInterstitialAd(){
        interstitialAd = new InterstitialAd(app);
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_ID);
        loadInterstitialAd();

    }
    */




    /**
     * Loads a banner ad from google
     * only if wifi is on, if wifi is not on, it won't.
     */
    /*
    @Override
    public void loadBannerAd() {
        System.out.println("game ads : loadBannerAd() called");
        if(isWifiConnected()) {
            //needs to be run on the aps UI thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerAdLoaded = false;
                    AdRequest.Builder builder = new AdRequest.Builder();
                    //builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                    rawBannerAd = builder.build();
                    bannerAd.loadAd(rawBannerAd);
                    bannerAdLoaded = true;
                }
            });
        }
    }
    */

    /**
     * Tells us if a banner has been loaded.
     * @return
     */
    /*
    @Override
    public boolean isBannerLoaded() {
        return bannerAdLoaded;
    }
    */

    /**
     * Shows a Pre-loaded banner ad.
     * If no banner ad is preloaded it will not show anything.
     */
    /*
    @Override
    public void showBannerAd() {
        System.out.println("game ads : showBannerAdAd() called");
        if(isBannerLoaded()){
            //needs to be run on the aps UI thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // bannerAd.loadAd(rawBannerAd);
                    bannerAd.setVisibility(View.VISIBLE);
                    bannerAdShowing = true;
                }
            });
        }else{
            System.out.println("ads ShowBannerAd() called when isBannerLoaded() false, so ignored");
            System.out.println("wifi status: " + isWifiConnected());
        }
    }*/

    /**
     * Hides the banner ad.
     */
    /*
    @Override
    public void hideBannerAd() {
        System.out.println("game ads : hideBannerAd() called");
        //needs to be run on the aps UI thread.
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.INVISIBLE);
                bannerAdShowing = false;
                loadBannerAd();
                System.out.println("game ads hideBannerAd() complete");
            }
        });
    }
    */

    /*
    @Override
    public void loadInterstitialAd() {
        System.out.println("game ads : loadInterStitialsAd() called");
        interstitialAdLoaded = false;


        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something important here, asynchronously to the rendering thread
                AdRequest.Builder builder = new AdRequest.Builder();
                //builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                final AdRequest ad = builder.build();

                // post a Runnable to the rendering thread that processes the result

                app.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        interstitialAd.loadAd(ad);
                        System.out.println("game ads : inerstitialAd.loadAd(ad) is complete");
                        interstitialAdLoaded = true;
                    }
                });


            }
        }).start();

    }
    */

    /*
    @Override
    public boolean isInterstitialAdLoaded() {
        return interstitialAdLoaded;
    }

    @Override
    public void showInterstitialAd() {
        System.out.println("game ads : showInterstitialAd() called");

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("game ads showInterstitialAd() onAdClosed() done ");
            }
        };

        if(isWifiConnected()) {
            if(isInterstitialAdLoaded()) {
                app.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (r != null) {
                            interstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {
                                    Gdx.app.postRunnable(r);
                                    interstitialAdShowing = false;
                                    loadInterstitialAd();
                                }
                            });
                        }
                        interstitialAd.show();
                        interstitialAdShowing = true;
                        System.out.println("game ads : interStitialAd.show() complete");
                    }
                });
            }else{
                System.out.println("game ads showInterstitialAd() called before interstial ad is loaded");
            }
        }else{
            System.out.println("game ads Wifi not connected, can't show ad.");
        }
    }
    */

    /**
     * Sets up the ads so they are useful.
     */

    public void setupAds() {
        System.out.println("game ads :setupAds() called");
        bannerAd = new AndroidBannerAd(this.BANNED_AD_ID, app);
        bannerAd.setup();
        //bannerAd.loadAd();
        //setupInterstitialAd();
    }

    public void updateVisibility(){
        if(bannerAd.isShowing()){
            bannerAd.setVisibility(View.VISIBLE);
        }else{
            bannerAd.setVisibility(View.INVISIBLE);
        }
    }


    /*
    public boolean isBannerAdShowing(){
        return bannerAdShowing;
    }

    public boolean isInterstitialAdShowing(){ return interstitialAdShowing;}

    @Override
    public float getStateTime() {
       // System.out.println("game ads : getStateTime(); called : returned: " + stateTime);
        return stateTime;
    }

    @Override
    public void setStateTime(float time) {
        stateTime = time;
       // System.out.println("game ads : setStateTime("+time+"); called");
    }
    */

    public void showBannerAd(){
        bannerAd.setShowing(true);
    }

    public void hideBannerAd(){
        bannerAd.setShowing(false);
        bannerAd.loadAd();
    }

    public RelativeLayout setupBannerLayout(RelativeLayout layout){
        return bannerAd.setupBannerLayout(layout);
    }

    public void dispose(){
        //
    }
}
