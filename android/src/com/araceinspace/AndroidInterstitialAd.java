package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
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
    }

    @Override
    public void loadAd() {

    }

    @Override
    public void loadAd_callback() {

    }

    @Override
    public <T> void setVisibility(T vis) {

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
}
