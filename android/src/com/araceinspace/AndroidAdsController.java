package com.araceinspace;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.araceinspace.MonetizationSubSystem.AdsController;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


/**
 * Created by Isaac Assegai on 9/17/16.
 * The AndroidAdsController is created by the AndroidLauncher
 * and passed to the main Game Controller: ARaceInSpace.java
 */
public class AndroidAdsController implements AdsController {

/* Field Variables */
    /**
     * Admob generated Banner ID.
     */
    private static final String BANNED_AD_ID = "ca-app-pub-5553172650479270/1591123946";

    AndroidApplication app;
    protected AdView bannerAd;


    public AndroidAdsController(AndroidApplication app){
        this.app = app;
    }

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

    public void setupAds() {
        bannerAd = new AdView(app);
        bannerAd.setVisibility(View.INVISIBLE);
        bannerAd.setBackgroundColor(0xff000000); // black
        bannerAd.setAdUnitId(BANNED_AD_ID);
        bannerAd.setAdSize(AdSize.SMART_BANNER);
    }

    /**
     * Adds our banner add to a specific layout.
     * Add it to be the device width, but only
     * be as high as the banner content.
     *
     * @param layout
     * @return
     */
    public RelativeLayout setupBannerLayout(RelativeLayout layout){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(bannerAd, params);
        return layout;
    }
}
