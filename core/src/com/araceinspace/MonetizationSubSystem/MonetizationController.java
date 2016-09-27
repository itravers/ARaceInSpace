package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The MonetizationController is an interface that
 * allows us to implement an Ads controller
 * for a specific platform. IE. Android would
 * implement it's own AndroidAdsController
 * Desktop would implement it's own.
 * HTTP it's own, and IOS it's own.
 */
public interface MonetizationController {
    /**
     * This is where all the ads should be constructed and
     * setup. We want to do it this way and keep the ads
     * setup code out of the implementing classes constructor.
     */
    public void setupAds();

    /**
     * Called from the render thread ALWAYS.
     * This should check each ads state
     * to see if it should be visible or not
     * then it should set the actual graphic
     * settings on the ads to make them
     * visible or not, as instructed.
     */
    public void updateVisibility();

    /**
     * Lets the RewardAd Asyncronously load itself.
     */
    public void loadRewardAd();

    /**
     * Lets the rewardsedVideoAd show itself.
     */
    public void showRewardAd();

    /**
     * Lets the interstitial ad Asynchronously load itself.
     */
    public void loadInterstitialAd();

    /**
     * Lets the interstitial ad show itself.
     */
    public void showInterstitialAd();

    /**
     * Makes the contained banner ad load itself Asynchronously
     */
    public void loadBannerAd();

    /**
     * Sets the banner ad to showing, does not change
     * any underlying graphics visibility, as that
     * must be always called from main render().
     */
    public void showBannerAd();

    /**
     * Sets the banner ad to hidden, also doesn't change
     * any underlaying graphics settings, because those
     * must be called from render();
     */
    public void hideBannerAd();

    /**
     * Instructs the Ads controller to ad it's
     * banner ad to a specific layout. These
     * layout formats change from platform to platform
     * so we make this generic. The implementing class
     * will have to cast this to the specific layout
     * object they use, then cast it back to T when returned.
     * @param layout
     * @param <T>
     * @return
     */
    public <T> T setupBannerLayout(T layout);

    /**
     * Implemented by android for when a google play billing request returns.
     * The AndroidLauncher routes the request through the ad controller
     * which routes it to the inAppPurchaser GooglePlayIAP
     * @param requestCode
     * @param resultCode
     * @param data
     * @param <T>
     * @return
     */
    public abstract <T> boolean onActivityResult(int requestCode, int resultCode, T data);

    /**
     * Directs the iap to initiate a purchase for the specified item.
     */
    public void buyItem(String sku);

    /**
     * Pauses any systems that have the ability to pause
     */
    public void pause();

    /**
     * Resumes any systems that have been paused.
     */
    public void resume();

    /**
     * Destroys any Systems with the ability to be destroyed.
     * Called at app close.
     */
    public void destroy();

    public boolean isBannerAdLoaded();

    public boolean isBannerAdShowing();

    public boolean isInterstitialAdLoaded();

    public boolean isRewardAdLoaded();
}