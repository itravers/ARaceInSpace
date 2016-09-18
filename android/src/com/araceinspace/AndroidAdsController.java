package com.araceinspace;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.araceinspace.MonetizationSubSystem.AdsController;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdRequest;
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

    /**
     * Used by showBannerAdd() to decide if banner has been loaded
     * When loadBannerAd() is called this is set to false until new
     * add has been loaded.
     */
    private boolean bannerAdLoaded = false;

    /**
     * This is a reference to the main android app.
     * needed here to add bannerAd to the main apps view.
     */
    AndroidApplication app;

    /**
     * This is the raw banner ad that we get from google and add to the Adview.bannerAd
     */
    private AdRequest rawBannerAd;

    /**
     * This is the view of the bannerAd that shows on the screen.
     */
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
        //needs to be run on the aps UI thread.
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAdLoaded = false;
                AdRequest.Builder builder = new AdRequest.Builder();
                builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                rawBannerAd = builder.build();
                bannerAdLoaded = true;
            }
        });
    }

    @Override
    public boolean isBannerLoaded() {
        return bannerAdLoaded;
    }

    @Override
    public void showBannerAd() {
        if(isBannerLoaded()){
            //needs to be run on the aps UI thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerAd.loadAd(rawBannerAd);
                    bannerAd.setVisibility(View.VISIBLE);
                }
            });
        }else{
            System.out.println("ads ShowBannerAd() called when isBannerLoaded() false, so ignored");
        }



    }

    @Override
    public void hideBannerAd() {
        //needs to be run on the aps UI thread.
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.INVISIBLE);
            }
        });
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
