package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The AdsController is an interface that
 * allows us to implement an Ads controller
 * for a specific platform. IE. Android would
 * implement it's own AndroidAdsController
 * Desktop would implement it's own.
 * HTTP it's own, and IOS it's own.
 */
public interface AdsController {
   // public void loadBannerAd();
    //public boolean isBannerLoaded();
    //public boolean isBannerAdShowing();

    /**
     * This is where all the ads should be constructed and
     * setup. We want to do it this way and keep the ads
     * setup code out of the implementing classes constructor.
     */
    public void setupAds();

    public void loadInterstitialAd();

    public void showInterstitialAd();

    /**
     * Loads a new banner ad in the background.
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

   // public void loadInterstitialAd();
   // public boolean isInterstitialAdLoaded();
   // public void showInterstitialAd();



   // public boolean isInterstitialAdShowing();
   // public float getStateTime();
   // public void setStateTime(float time);

    /**
     * Called from the render thread ALWAYS.
     * This should check each ads state
     * to see if it should be visible or not
     * then it should set the actual graphic
     * settings on the ads to make them
     * visible or not, as instructed.
     */
    public void updateVisibility();
}
