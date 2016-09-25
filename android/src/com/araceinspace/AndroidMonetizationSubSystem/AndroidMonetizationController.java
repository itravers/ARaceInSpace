package com.araceinspace.AndroidMonetizationSubSystem;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import com.araceinspace.AndroidLauncher;
import com.araceinspace.MonetizationSubSystem.MonetizationController;

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

    GooglePlayIAP inAppPurchaser;

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
     * Sets up the ads so they are useful.
     */

    public void setupAds() {
        System.out.println("game ads :setupAds() called");
        bannerAd = new AndroidBannerAd(this.BANNER_AD_ID, app);
        bannerAd.setup();

        interstitialAd = new AndroidInterstitialAd(this.INTERSTITIAL_AD_ID, app);
        interstitialAd.setup();

        rewardedVideoAd = new AdColonyAndroidRewardAd(app);
        rewardedVideoAd.setup();

        inAppPurchaser = new GooglePlayIAP(app);

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
