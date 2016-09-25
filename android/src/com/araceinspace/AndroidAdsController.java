package com.araceinspace;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.ads.reward.RewardedVideoAd;


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
    private static final String BANNER_AD_ID = "ca-app-pub-5553172650479270/1591123946";

    private AndroidBannerAd bannerAd;

    /**
     * Admob generated interstitial id.
     */
    private static final String INTERSTITIAL_AD_ID = "ca-app-pub-5553172650479270/1671849140";

    private AndroidInterstitialAd interstitialAd;


    private static final String REWARD_AD_ID = "ca-app-pub-5553172650479270/6900797543";

    private AndroidRewardAd2 rewardedVideoAd;

    GooglePlayIAP inAppPurchaser;

    /**
     * This is a reference to the main android app.
     * needed here to add bannerAd to the main apps view.
     */
    AndroidLauncher app;


/* Constructors. */

    /**
     * Construct an AndroidAdsController
     * @param app The Application context this controller wil be ran in.
     */
    public AndroidAdsController(AndroidLauncher app){
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
        bannerAd = new AndroidBannerAd(this.BANNER_AD_ID, app);
        bannerAd.setup();

        interstitialAd = new AndroidInterstitialAd(this.INTERSTITIAL_AD_ID, app);
        interstitialAd.setup();

        rewardedVideoAd = new AndroidRewardAd2(this.REWARD_AD_ID, app);
        rewardedVideoAd.setup();

        inAppPurchaser = new GooglePlayIAP(app);

    }

    /**
     * Implements the AdsController abstract updateVisiblity.
     * This method should ONLY be called directly from the render() method.
     * On the SAME thread.
     * Checks each Ads state to see whether it should be visible, or invisible
     * If it should be visibble and is not, it is set to visible.
     * If it should be Invisible and is not, it is set to Invisible.
     */
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

    public void loadRewardAd(){rewardedVideoAd.loadAd();}

    public void showRewardAd(){rewardedVideoAd.showAd();}

    public void loadInterstitialAd(){
        interstitialAd.loadAd();
    }

    public void showInterstitialAd(){
        interstitialAd.showAd();
    }

    /**
     * Makes the contained banner ad load a new ad.
     * This will cause the ad to show as unloaded
     * until it is done.
     */
    public void loadBannerAd(){
        bannerAd.loadAd();;
    }

    /**
     * Sets the banner ad itself to the showing state.
     * This does not change the underlying Graphics visibility
     * as that must be done from render() through the updateVisibility()
     * method.
     */
    public void showBannerAd(){
        bannerAd.setShowing(true);
    }

    /**
     * Sets the banner ad to not the not showing state.
     * This also does not change the underlying graphics visibility
     * as that must be done from the render() through updateVisibility()
     */
    public void hideBannerAd(){
        bannerAd.setShowing(false);
        bannerAd.loadAd(); //we may want to load another ad as soon as the previous was hidden.
    }

    /**
     * Any Ads controller should be able to setup a banners
     * layout. However the implementation of how this is
     * done is different for every type of program.
     * So We use generics, and cast them to the
     * correct object before we call the actual method.
     * @param layout The Generic Layout we are going to cast to a RelativeLayout
     * @param <T> The generic layout type.
     * @return A RelativeLayout cast to a Generic Layout.
     */
    @Override
    public <T> T setupBannerLayout(T layout) {
        RelativeLayout newLayout = ((RelativeLayout)layout);
        newLayout = setupBannerLayout(newLayout);
        return (T)newLayout;
    }

    public <T> boolean onActivityResult(int requestCode, int resultCode, T data){
        return onActivityResult(requestCode, resultCode, (Intent)data);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data){
        return inAppPurchaser.onActivityResult(requestCode, resultCode, data);
    }

    public void buyItem(){
        inAppPurchaser.buyItem();
    }

    /**
     * Since this is android we are using A RelativeLayout
     * as our banner layout, We use the AndroidBanner
     * to set this up for us, so all the logic is in
     * the same place. This method routes the call
     * to the right place.
     * @param layout The Relative Layout to set.
     * @return The same layout, with the banner added.
     */
    public RelativeLayout setupBannerLayout(RelativeLayout layout){
        return bannerAd.setupBannerLayout( layout);
    }

    /**
     * Pauses any Ads that have the ability to pause
     */
    public void pause(){
        bannerAd.pause();
        rewardedVideoAd.pause();
    }

    public void resume(){
        bannerAd.resume();
        rewardedVideoAd.resume();
    }

    public void destroy(){
        bannerAd.destroy();
        rewardedVideoAd.destroy();
        inAppPurchaser.destroy();
    }

    public void consumeOwnedItems(){
        inAppPurchaser.consumeOwnedItems();
    }

}
