package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The DummyController implements the MonetizationController
 * interface for all classes that don't actually have a
 * monetization controller. This is a space keeper until
 * we can implement a monetization controller for each
 * type of launcher.
 */
public class DummyController implements MonetizationController {

    @Override
    public void showBannerAd() {
        //do nothing
    }

    @Override
    public void hideBannerAd() {
        //do nothing
    }

    @Override
    public <T> T setupBannerLayout(T layout) {
        return null;
    }

    @Override
    public void pause() {
        //do nothing
    }

    @Override
    public void resume() {
        //do nothing
    }

    @Override
    public void destroy() {
        //do nothing
    }

    @Override
    public boolean isBannerAdLoaded() {
        return false;
    }

    @Override
    public boolean isBannerAdShowing() {
        return false;
    }

    @Override
    public boolean isInterstitialAdLoaded() {
        return false;
    }

    @Override
    public boolean isRewardAdLoaded() {
        return false;
    }

    @Override
    public void setupAds() {
        //do nothing
    }

    @Override
    public void loadRewardAd() {
        //do nothing
    }

    @Override
    public void showRewardAd() {
        //do nothing
    }

    @Override
    public void loadInterstitialAd() {
        //do nothing
    }

    @Override
    public void showInterstitialAd() {
        //do nothing
    }

    @Override
    public void loadBannerAd() {
        //do nothing
    }

    @Override
    public void updateVisibility() {
        //do nothing
    }

    @Override
    public <T> boolean onActivityResult(int requestCode, int resultCode, T data) {
        return false;
    }

    @Override
    public void buyItem(String sku) {
        //do nothing
    }
}
