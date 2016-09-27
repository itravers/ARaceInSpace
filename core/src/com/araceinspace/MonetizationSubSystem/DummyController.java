package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * Used by Launchers that have not yet implemented
 * an MonetizationController
 */
public class DummyController implements MonetizationController {

    //@Override
    public void showBannerAd() {

    }

    //@Override
    public void hideBannerAd() {

    }

    @Override
    public <T> T setupBannerLayout(T layout) {
        return null;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

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

    }

    @Override
    public void loadRewardAd() {

    }

    @Override
    public void showRewardAd() {

    }

    @Override
    public void loadInterstitialAd() {

    }

    @Override
    public void showInterstitialAd() {

    }

    @Override
    public void loadBannerAd() {

    }

    @Override
    public void updateVisibility() {

    }

    @Override
    public <T> boolean onActivityResult(int requestCode, int resultCode, T data) {
        return false;
    }

    @Override
    public void buyItem(String sku) {

    }
}
