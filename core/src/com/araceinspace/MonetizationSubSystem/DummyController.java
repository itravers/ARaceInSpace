package com.araceinspace.MonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/17/16.
 * Used by Launchers that have not yet implemented
 * an AdsController
 */
public class DummyController implements AdsController{

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
    public void setupAds() {

    }

    @Override
    public void updateVisibility() {

    }
}
