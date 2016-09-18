package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.araceinspace.MonetizationSubSystem.AdsController;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdListener;
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
     * This is set to true while a banner ad is currently showing.
     */
    private boolean bannerAdShowing = false;


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

    /**
     * Check to see if the system's wifi is connected.
     * @return True if wifi is connected.
     */
    @Override
    public boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return (ni != null && ni.isConnected());
    }

    /**
     * Loads a banner ad from google
     * only if wifi is on, if wifi is not on, it won't.
     */
    @Override
    public void loadBannerAd() {
        if(isWifiConnected()) {
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
    }

    /**
     * Tells us if a banner has been loaded.
     * @return
     */
    @Override
    public boolean isBannerLoaded() {
        return bannerAdLoaded;
    }

    /**
     * Shows a Pre-loaded banner ad.
     * If no banner ad is preloaded it will not show anything.
     */
    @Override
    public void showBannerAd() {
        if(isBannerLoaded()){
            //needs to be run on the aps UI thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerAd.loadAd(rawBannerAd);
                    bannerAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            bannerAd.setBackgroundColor(0xff000000); // black
                            bannerAd.setVisibility(View.VISIBLE);
                            bannerAdShowing = true;
                        }
                    });
                }
            });
        }else{
            System.out.println("ads ShowBannerAd() called when isBannerLoaded() false, so ignored");
            System.out.println("wifi status: " + isWifiConnected());
        }



    }

    @Override
    public void hideBannerAd() {
        //needs to be run on the aps UI thread.
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.INVISIBLE);
                bannerAdShowing = false;
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

    public boolean getBannerAdShowing(){
        return bannerAdShowing;
    }
}
