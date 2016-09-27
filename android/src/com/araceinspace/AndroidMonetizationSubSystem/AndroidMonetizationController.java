package com.araceinspace.AndroidMonetizationSubSystem;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import com.araceinspace.AndroidLauncher;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.badlogic.gdx.Gdx;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The AndroidMonetizationController is created by the AndroidLauncher
 * and passed to the main Game Controller: ARaceInSpace.java
 * The Ads
 */
public class AndroidMonetizationController implements MonetizationController {

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


    private static final String REWARD_AD_ID = "ca-app-pub-5553172650479270/6900797543"; //AdMob Mediation id.
    //private static final String REWARD_AD_ID = "app97267127423b4837aa"; //AdColon

    private AdColonyAndroidRewardAd rewardedVideoAd;

    //GooglePlayIAP inAppPurchaser;
    PlayPurchaseManager playPurchaseManager;

    /**
     * This is a reference to the main android app.
     * needed here to add bannerAd to the main apps view.
     */
    AndroidLauncher app;


/* Constructors. */

    /**
     * Construct an AndroidMonetizationController
     * @param app The Application context this controller wil be ran in.
     */
    public AndroidMonetizationController(AndroidLauncher app){
        this.app = app;
    }

/* Private Methods. */

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


/* Public Methods. */

    /**
     * Sets up the ads so they are useful.
     */
    public void setupAds() {
        Gdx.app.log("GameAds", "AndroidMonetizationController.setupAds() called");

        //first setup the banner ads
        bannerAd = new AndroidBannerAd(this.BANNER_AD_ID, app);
        bannerAd.setup();

        //next setup the interstitial ads
        interstitialAd = new AndroidInterstitialAd(this.INTERSTITIAL_AD_ID, app);
        interstitialAd.setup();

        //next setup the rewarded video ads
        rewardedVideoAd = new AdColonyAndroidRewardAd(app);
        rewardedVideoAd.setup();

        //next setup the in-app purchase system.
        //inAppPurchaser = new GooglePlayIAP(app);
       // inAppPurchaser.setup();
        playPurchaseManager = new PlayPurchaseManager(app);
        playPurchaseManager.setup();
    }

    /**
     * Implements the MonetizationController abstract updateVisiblity.
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

    /**
     * Lets the RewardAd Asyncronously load itself.
     */
    public void loadRewardAd(){rewardedVideoAd.loadAd();}

    /**
     * Lets the rewardsedVideoAd show itself.
     */
    public void showRewardAd(){rewardedVideoAd.showAd();}

    /**
     * Lets the interstitial ad Asynchronously load itself.
     */
    public void loadInterstitialAd(){
        interstitialAd.loadAd();
    }

    /**
     * Lets the interstitial ad show itself.
     */
    public void showInterstitialAd(){
        interstitialAd.showAd();
    }

    /**
     * Makes the contained banner ad load itself Asynchronously
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

    /**
     * When google in-app billing returns results it calls this method in the Main AndroidLauncher Class
     * which then routes it here. Where we decide if we can use it or not.
     * If we can use it, we return true, so the calling method will know we are using it and
     * won't use it itself.
     * @param requestCode
     * @param resultCode
     * @param data
     * @param <T> The Generic Type
     * @return
     */
    public <T> boolean onActivityResult(int requestCode, int resultCode, T data){
        return onActivityResult(requestCode, resultCode, (Intent)data); //cast data to an intent and call ungeneric method.
    }

    /**
     * This Un-genericizes our implemented method. We cast the data to an Intent
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data){
        //return inAppPurchaser.onActivityResult(requestCode, resultCode, data);
        return playPurchaseManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Directs the iap to initiate a purchase for the specified item.
     */
    public void buyItem(){
        //inAppPurchaser.buyItem();
        //playPurchaseManager.purchaseItem(playPurchaseManager.getDefaultItems().get("test_product_0001")); //buy test_product_0001 which is a default item.
        playPurchaseManager.purchaseItem(playPurchaseManager.defaultItems.get("test_product_0001")); //buy test_product_0001 which is a default item.
    }

    /**
     * Pauses any systems that have the ability to pause
     */
    public void pause(){
        bannerAd.pause();
        rewardedVideoAd.pause();
    }

    /**
     * Resumes any systems that have been paused.
     */
    public void resume(){
        bannerAd.resume();
        rewardedVideoAd.resume();
    }

    /**
     * Destroys any Systems with the ability to be destroyed.
     * Called at app close.
     */
    public void destroy(){
        bannerAd.destroy();
        rewardedVideoAd.destroy();
        //inAppPurchaser.destroy();
        playPurchaseManager.destroy();
    }
}