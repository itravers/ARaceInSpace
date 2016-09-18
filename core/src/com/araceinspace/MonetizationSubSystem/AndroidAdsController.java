package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The AndroidAdsController is created by the AndroidLauncher
 * and passed to the main Game Controller: ARaceInSpace.java
 */
public class AndroidAdsController implements AdsController{
    @Override
    public boolean isWifiConnected() {
        return false;
    }

    @Override
    public void loadBannerAd() {

    }

    @Override
    public boolean isBannerLoaded() {
        return false;
    }

    @Override
    public void hideBannerAd() {

    }

    @Override
    public void loadInterstitialAd() {

    }

    @Override
    public boolean isInterstitialAdLoaded() {
        return false;
    }

    @Override
    public void showInterstitialAd() {

    }
}
