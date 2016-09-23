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

    /**
     * This is where all the ads should be constructed and
     * setup. We want to do it this way and keep the ads
     * setup code out of the implementing classes constructor.
     */
    public void setupAds();

   // public boolean isInterstitialAdShowing();
   // public float getStateTime();
   // public void setStateTime(float time);
    public void updateVisibility();
}
