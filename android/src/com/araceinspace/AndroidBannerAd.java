package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by Isaac Assegai on 9/22/16.
 * The BannerAd extends the base GameAd
 * and provides all the functionality needed to load
 * and show itself, provided a given
 * libgdx Application Adapter.
 */
public class AndroidBannerAd extends GameAd{

/* Field Variables */

    /**
     * The view used for a bannerAd, from the google api.
     */
    private AdView bannerAd;

    /**
     * This is the raw banner ad that we get from google and add to the Adview.bannerAd
     */
    private AdRequest rawBannerAd;

    /**
     * The LibGdx ApplicationAdapter we are currently using
     * this will be a AndroidApplication if we are on android
     * I believe it will be a LWJGL container on desktop.
     * It is used to access the apps views, to allow the
     * ads to be shown.
     */
    private AndroidApplication app;

/* Constructors */

    /**
     * Create a new BannerAd with a specific ID and ApplicationAdapter.
     *
     * @param ID  The ID - Ususally manually obtained from ads service.
     * @param app The App we are displaying the ads in.
     */
    public AndroidBannerAd(String ID, AndroidApplication app) {
        super(ID);

        this.app = app;

        bannerAd = new AdView(app);
        bannerAd.setVisibility(View.INVISIBLE);
        bannerAd.setAdUnitId(ID);
        bannerAd.setAdSize(AdSize.SMART_BANNER);
        bannerAd.setAdListener(new BannerListener(this));
    }

/* Private Methods */

/* Public Methods */

    /**
     * Causes the google api to load the ad into
     * bannerAd. Once loaded the loadAd_callback()
     * method will be called by the bannerAd's listener.
     */
    @Override
    public void loadAd() {
        System.out.println("game ads : loadAd() called");
        if(isConnected()){
            //needs to be run on the aps UI thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setLoaded(false);
                    AdRequest.Builder builder = new AdRequest.Builder();
                    //builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                    rawBannerAd = builder.build();
                    bannerAd.loadAd(rawBannerAd);
                }
            });
        }else{
            System.out.println("game ads : loadAd() called, but no connection available");
        }

    }

    /**
     * Once the add is loaded, the listener will call back this method.
     * Here we will decide what to do when the ad is loaded.
     * for now we will just setLoaded(true)
     */
    @Override
    public void loadAd_callback() {
        setLoaded(true);
    }

    @Override
    public void showAd() {
        System.out.println("game ads : showAd() called");
        if(isLoaded()){
            //Showing the Ad, needs to be run on the UI Thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerAd.setVisibility(View.VISIBLE);
                    setShowing(true);
                }
            });
        }else{
            System.out.println("game ads : showAds() called, but isLoaded() == false");
        }
    }

    @Override
    public void hideAd() {
        System.out.println("game ads : hideAd() called");

        //needs to be run on the UI Thread
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.INVISIBLE);
                setShowing(false);

                //Now we want to prep loading the next banner ad to be shown
                loadAd();
            }
        });
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

    /**
     * Check to see if the system's wifi is connected.
     * @return True if wifi is connected.
     */
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return (ni != null && ni.isConnected());
    }

    /**
     * The BannerListener extends the normal AdListener
     * by giving us the ability to refer to the app.
     * We use this to trigger the proper callbacks of the ad.
     */
    private class BannerListener extends AdListener{

        /**
         * This is the Add this Listener is Listening To.
         */
        AndroidBannerAd ad;

        /**
         * Create a Banner Listener
         * @param ad The BannerAd this is being created to listen to.
         */
        public BannerListener(AndroidBannerAd ad){
            super();
            this.ad = ad;
        }

        /**
         * If the banner ad failed to load, this will be called.
         * @param i The Error Code.
         */
        @Override
        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
        }

        /**
         * When a banner ad is loaded, this is called.
         */
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            ad.loadAd_callback();

        }
    }
}
