package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * Used by Launchers that have not yet implemented
 * an AdsController
 */
public class DummyController implements AdsController{
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
    public boolean isBannerAdShowing() {
        return false;
    }

    @Override
    public void showBannerAd() {

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

    @Override
    public void setupAds() {

    }
}
