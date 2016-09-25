package com.araceinspace.AndroidMonetizationSubSystem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.jirbo.adcolony.*;

/**
 * Created by Isaac Assegai on 9/23/16.
 * This Encapsulates the data and methods needed to
 * implement Rewarded Videos from AdColony.
 * I was having trouble getting AdMob Mediation to work
 * So I decided to roll a class specifically dedicated
 * to AdColony Reward Video's
 */
public class AdColonyAndroidRewardAd extends GameAd implements AdColonyV4VCListener{

/* Field Variables */

    /**
     * The APP_ID given when App is setup in AdColony
     */
    public static final String APP_ID = "app97267127423b4837aa";

    /**
     * The Zone ID that the app is setup to serve.
     * I believe this can be turned into an Array of strings and still work
     * as multiple Zone_IDs are available for each app in AdColony
     */
    public static final String ZONE_ID = "vz6ebda927dc7f49f4bb";

    /**
     * The Ad that is downloaded and shown, itself.
     */
    private AdColonyV4VCAd rewardedVideoAd;

    /**
     * The main android application, used for data/methods access
     */
    private AndroidApplication app;

    /**
     * Another Reference to this, used inside contained Runnables.
     */
    private AdColonyAndroidRewardAd me;

    /**
     * Create a new GameAd with a specific ID and ApplicationAdapter.
     */
    public AdColonyAndroidRewardAd(AndroidApplication app) {
        super(null);//adColony doesn't use an ad_id.
        this.app = app;
        me = this;
    }

/* Private Methods */


/* Public Methods */

    /**
     * Setup this ad. We need to configure it and set a listener.
     */
    @Override
    public void setup() {
        Gdx.app.log("GameAds", "AdColonyAndroidRewardAd.setup() called");
        AdColony.configure(app, "version:0.0.1a,store:google", APP_ID, ZONE_ID);//sets the app and zone id, also need this weird string for some reason.
        rewardedVideoAd = new AdColonyV4VCAd(ZONE_ID); //Init the reward add.
        AdColony.addV4VCListener(this); //Add this as a listener.
    }

    /**
     * Loads a new ad in the background. Thread.
     */
    @Override
    public void loadAd() {
        Gdx.app.log("GameAds", "AdColonyAndroidRewardAd.loadAd() called");
        if(isConnected() && app != null){ //we don't want to load an add if we are not connected.
            //Need to run this in the ui thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rewardedVideoAd = new AdColonyV4VCAd(ZONE_ID);
                    setLoaded(true);
                }
            });
        }else{
            Gdx.app.log("GameAds", "AdColonyAndroidRewardAd.loadAd() called, but isConnected() == false || app == null");
        }
    }

    /**
     * The listener calls this when the ad is properly loaded.
     */
    @Override
    public void loadAd_callback() {
        Gdx.app.log("Game Ads", "AdColonyAndroidRewardAd.loadAd_callback() called");
        setLoaded(true);
    }

    /**
     * The listener calls this when the ad is closed.
     */
    public void closeAd_callback(){setShowing(false);}

    /**
     * We don't use this here, we only use this for banner ads.
     */
    @Override
    public <T> void setVisibility(T vis) {
        //we do nothing here, reward ads don't use this, only banner ads display with this.
    }

    /**
     * Shows a reardVideo, this will interrupt whatever other activity is happening.
     * if the ad is shown at a certain time we may need to pause.
     */
    @Override
    public void showAd() {
        Gdx.app.log("GameAds", "AdColonyAndroidRewardAd.showAd() called");
        if(isLoaded()){
            //we want to do this on the ui thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setShowing(true);
                    rewardedVideoAd.show();
                }
            });
        }else{
            Gdx.app.log("GameAds", "AdColonyAndroidRewardAd.showAd() called, but ad is not loaded.");
            //if ad isn't loaded and should be shown, we want to tell the user via a toast.
            //This is how we route the message, create an intent, and broadcast it. We've setup the launcher to listen for this.
            Intent intent = new Intent("ShowToast");
            intent.putExtra("message", "Couldn't show Reward Ad, not Loaded Correctly.");
            LocalBroadcastManager.getInstance(app).sendBroadcast(intent);
        }
    }

    /**
     * Pauses the AdColony Video
     */
    @Override
    public void pause() {
        AdColony.pause();
    }

    /**
     * Resumes a paused AdColony video
     */
    @Override
    public void resume() {
        AdColony.resume(app);
    }

    /**
     * AdColony Videos don't have a destroy message, we don't do anything here.
     */
    @Override
    public void destroy() {
        //do nothing
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
     * The method called when an AdColony Reward ad actually rewards us.
     * From here we'll need to make some sort of event and dispatch it.
     * @param adColonyV4VCReward
     */
    @Override
    public void onAdColonyV4VCReward(AdColonyV4VCReward adColonyV4VCReward) {
        Gdx.app.log("GameAds", "AdColonyAndroidRewardAd.onAdColonyV4VCReward() called");
        Gdx.app.log("GameAds", "success: " + adColonyV4VCReward.success());
        Gdx.app.log("GameAds", "name: " + adColonyV4VCReward.name());
        Gdx.app.log("GameAds", "amount: " + adColonyV4VCReward.amount());

    }
}
