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

    /**
     * The actual AdMob Android Banner ad, which extends GameAd.
     */
    private AndroidBannerAd bannerAd;

    /**
     * Admob generated interstitial id.
     */
    private static final String INTERSTITIAL_AD_ID = "ca-app-pub-5553172650479270/1671849140";

    /**
     * The actual AdMob Android Interstitial ad, which also extends GameAd.
     */
    private AndroidInterstitialAd interstitialAd;

    /**
     * This is the AdMob id for a mediated reward ad, currently Admob mediation isn't working, so we
     * don't use it.
     */
    private static final String REWARD_AD_ID = "ca-app-pub-5553172650479270/6900797543"; //AdMob Mediation id.

    /**
     * The actual AdColony Android Reward Ad, which also extends GameAd.
     */
    private AdColonyAndroidRewardAd rewardedVideoAd;

    /**
     * The in-app purchase manager, which gives app users the ability to buy in-app items
     * and subscriptions.
     */
    PlayPurchaseManager playPurchaseManager;

    /**
     * This is a reference to the main android app.
     * needed here to add bannerAd to the main apps view.
     */
    AndroidLauncher app;

    /**
     * When show ad or hide ad is called, this is set to true. The code that
     * does that actual showing or hiding sets it back to false.
     * This is because updateVisibility was causing thousands of ad views to be allocated.
     */
    private boolean bannerVisibilityChanged = true;


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
    @Override
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
    @Override
    public void updateVisibility(){
        if(bannerVisibilityChanged){
            System.out.println("game ads: monetizationController.updateVisibility()");
            if(bannerAd.isShowing()){
                bannerAd.setVisibility(View.VISIBLE);
                // System.out.println("game ads: setting ad visible");
            }else{
                bannerAd.setVisibility(View.INVISIBLE);
            }
            bannerVisibilityChanged = false;
        }
    }

    /**
     * Lets the RewardAd Asyncronously load itself.
     */
    @Override
    public void loadRewardAd(){rewardedVideoAd.loadAd();}

    /**
     * Lets the rewardsedVideoAd show itself.
     */
    @Override
    public void showRewardAd(){rewardedVideoAd.showAd();}

    /**
     * Lets the interstitial ad Asynchronously load itself.
     */
    @Override
    public void loadInterstitialAd(){
        interstitialAd.loadAd();
    }

    /**
     * Lets the interstitial ad show itself.
     */
    @Override
    public void showInterstitialAd(){
        interstitialAd.showAd();
    }

    /**
     * Makes the contained banner ad load itself Asynchronously
     */
    @Override
    public void loadBannerAd(){
        bannerAd.loadAd();;
    }

    /**
     * Sets the banner ad itself to the showing state.
     * This does not change the underlying Graphics visibility
     * as that must be done from render() through the updateVisibility()
     * method.
     */
    @Override
    public void showBannerAd(){
      //  System.out.println("game ads: showBannerAd() called");
        if(!isBannerAdShowing()){
            bannerAd.setShowing(true);
            bannerVisibilityChanged = true;
        }

    }

    /**
     * Sets the banner ad to not the not showing state.
     * This also does not change the underlying graphics visibility
     * as that must be done from the render() through updateVisibility()
     */
    @Override
    public void hideBannerAd(){
        bannerAd.setShowing(false);
        bannerVisibilityChanged = true;
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
    @Override
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
        return playPurchaseManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Directs the iap to initiate a purchase for the specified item.
     */
    @Override
    public void buyItem(String sku){
        playPurchaseManager.purchaseItem(playPurchaseManager.defaultItems.get(sku)); //buy test_product_0001 which is a default item.
    }

    /**
     * Pauses any systems that have the ability to pause
     */
    @Override
    public void pause(){
        bannerAd.pause();
        rewardedVideoAd.pause();
    }

    /**
     * Resumes any systems that have been paused.
     */
    @Override
    public void resume(){
        bannerAd.resume();
        rewardedVideoAd.resume();
    }

    /**
     * Destroys any Systems with the ability to be destroyed.
     * Called at app close.
     */
    @Override
    public void destroy(){
        bannerAd.destroy();
        rewardedVideoAd.destroy();
        //inAppPurchaser.destroy();
        playPurchaseManager.destroy();
    }

    /**
     * Checks if a Banner is Currently Loaded.
     * @return True if loaded, false if not.
     */
    @Override
    public boolean isBannerAdLoaded(){
        return bannerAd.isLoaded();
    }

    /**
     * Checks if a Banner Ad is currently showing.
     * @return True if showing, false if not.
     */
    @Override
    public boolean isBannerAdShowing(){
        return bannerAd.isShowing();
    }

    /**
     * Checks if an interstitial ad has been loaded.
     * @return True if loaded, false if not.
     */
    @Override
    public boolean isInterstitialAdLoaded(){
        return interstitialAd.isLoaded();
    }

    /**
     * Checks if a reward ad is loaded.
     * @return True if loaded, false if not.
     */
    @Override
    public boolean isRewardAdLoaded(){
        return rewardedVideoAd.isLoaded();
    }
}