package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The AdsController is an interface implemented
 */
public interface AdsController {
    public boolean isWifiConnected();
    public void loadBannerAd();
    public boolean isBannerLoaded();
    public void showBannerAd();
    public void hideBannerAd();
    public void loadInterstitialAd();
    public boolean isInterstitialAdLoaded();
    public void showInterstitialAd();
    public void setupAds();
}
