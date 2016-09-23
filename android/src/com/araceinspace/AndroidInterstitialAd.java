package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Isaac Assegai on 9/23/16.
 * The AndroidInterstitialAd extends the base GameAd
 * and provides all the functionality needed to load
 * and show an interstitial ad from the Admob network.
 * provided a given Libgdx Application Adapter, which
 * will be an AndroidApplicationAdapter in this case.
 */
public class AndroidInterstitialAd extends GameAd{

/* Field Variables */

    /**
     * The interstitial ad that will be loaded from google.
     */
    private InterstitialAd interstitialAd;

    /**
     * The LibGdx ApplicationAdapter we are currently using
     * this will be a AndroidApplication if we are on android
     * I believe it will be a LWJGL container on desktop.
     * It is used to access the apps views, to allow the
     * ads to be shown.
     */
    private AndroidApplication app;

    /**
     * me is just an alternate reference to this, for
     * use when code in a contained Runnable wants
     * to refer to this.
     */
    private AndroidInterstitialAd me;


/* Constructors */

    /**
     * Create a new GameAd with a specific ID and ApplicationAdapter.
     *
     * @param ID The ID - Ususally manually obtained from ads service.
     * @param app The App we are displaying the ads in.
     */
    public AndroidInterstitialAd(String ID, final AndroidApplication app) {
        super(ID);
        this.app = app;
        me = this;
    }


/* Private Methods */


/* Public Methods */

    @Override
    public void setup() {
        Gdx.app.log("Game Ads", "AndroidInterstitialAd.setup() called");
        interstitialAd = new InterstitialAd(app);
        interstitialAd.setAdUnitId(getID());
        interstitialAd.setAdListener(new InterstitialListener(me));
    }

    @Override
    public void loadAd() {
        Gdx.app.log("Game Ads", "AndroidInterstitialAd.loadAd() called");
        if(isConnected() && app != null){
            //this needs to be run on the aps UI thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setLoaded(false);
                    AdRequest.Builder builder = new AdRequest.Builder();
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                    AdRequest ad = builder.build();
                    interstitialAd.loadAd(ad);
                }
            });
        }else{
            Gdx.app.log("Game Ads", "AndroidInterstitialAd.loadAd() called, but won't work because isConnected == false || app == null");
        }
    }

    @Override
    public void loadAd_callback() {
        Gdx.app.log("Game Ads", "AndroidInterstitialAd.loadAd_callback() called");
        setLoaded(true);
    }

    @Override
    public void showAd(){
        Gdx.app.log("Game Ads", "AndroidInterstitialAd.showAd() called");
        if(isLoaded()){
            //we want to do this on the UI thread... i think...
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    interstitialAd.show();
                    setShowing(true);
                }
            });
        }
    }

    @Override
    public <T> void setVisibility(T vis) {
      //do nothing interstitial ads visibility is not set the same way as banner ads.
    }

    /**
     * Check to see if the system's wifi is connected.
     * @return True if wifi is connected.
     */
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    /**
     * Called when the ad is closed.
     * We set showing to false.
     */
    public void closeAd_callback(){
        setShowing(false);
    }

    public void pause(){
        //interstitialAds don't have a pause function
    }

    public void resume(){
        //interstitialAds don't have a resume function
    }

    public void destroy(){
        //interstitialAds don't have a destroy function
        
    }

/* Private Classes */

    public class InterstitialListener extends AdListener{

        /**
         * This is the Add this Listener is Listening To.
         */
        AndroidInterstitialAd ad;

        public InterstitialListener(AndroidInterstitialAd ad){
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
            if(i == AdRequest.ERROR_CODE_INTERNAL_ERROR)Gdx.app.log("Game Ads", " AndroidInterstitialAd.onAdFailedToLoad: Error ERROR_CODE_INTERNAL_ERROR");
            if(i == AdRequest.ERROR_CODE_INVALID_REQUEST)Gdx.app.log("Game Ads", " AndroidInterstitialAd.onAdFailedToLoad: Error ERROR_CODE_INVALID_REQUEST");
            if(i == AdRequest.ERROR_CODE_NETWORK_ERROR)Gdx.app.log("Game Ads", " AndroidInterstitialAd.onAdFailedToLoad: Error ERROR_CODE_NETWORK_ERROR");
            if(i == AdRequest.ERROR_CODE_NO_FILL)Gdx.app.log("Game Ads", " AndroidInterstitialAd.onAdFailedToLoad: Error ERROR_CODE_NO_FILL");
        }

        /**
         * When a banner ad is loaded, this is called.
         * We route it back to the main classes loadAd_callback() method.
         */
        @Override
        public void onAdLoaded() {
            Gdx.app.log("Game Ads", "AndroidInterstitialAd.onAdLoaded() thread:" + Thread.currentThread().getName());
            ad.loadAd_callback();
        }

        /**
         * Called when Ad gets closed, we route back to
         * the main classes closeAd_callback() method.
         */
        @Override
        public void onAdClosed(){
            ad.closeAd_callback();
        }

    }
}
