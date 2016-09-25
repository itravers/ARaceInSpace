package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.jirbo.adcolony.*;

//import com.google.android.gms.ads.reward.RewardedVideoAd;

/**
 * Created by Isaac Assegai on 9/23/16.
 * Encapsulate all the functionality needed for a reward ad.
 * A Reward ad is given to us from the Admob network, however
 * they don't serve the ad itself so we use mediation networks.
 */
public class AndroidRewardAd2 extends GameAd implements AdColonyV4VCListener{

/* Field Variables */

    public static final String APP_ID = "app97267127423b4837aa";
    public static final String ZONE_ID = "vz6ebda927dc7f49f4bb";

    private AdColonyV4VCAd rewardedVideoAd;

    private AndroidApplication app;

    private AndroidRewardAd2 me;

    /**
     * Create a new GameAd with a specific ID and ApplicationAdapter.
     *
     * @param ID The ID - Ususally manually obtained from ads service.
     */
    public AndroidRewardAd2(String ID, AndroidApplication app) {
        super(ID);
        this.app = app;
        me = this;
    }

/* Private Methods */



/* Public Methods */

    @Override
    public void setup() {
        Gdx.app.log("GameAds", "AndroidRewardAd.setup() called");
        // Initialize the Mobile Ads SDK.
       // MobileAds.initialize(app, APP_ID);
       // rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(app);

       // rewardedVideoAd.setRewardedVideoAdListener(new RewardAdListener(this));
        AdColony.configure(app, "version:0.0.1a,store:google", APP_ID, ZONE_ID);
        rewardedVideoAd = new AdColonyV4VCAd(ZONE_ID);
        AdColony.addV4VCListener(this);

    }

    @Override
    public void loadAd() {
        Gdx.app.log("GameAds", "AndroidRewardAd.loadAd() called");
        if(isConnected() && app != null){
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rewardedVideoAd = new AdColonyV4VCAd(ZONE_ID);
                    setLoaded(true);
                }
            });
            /*
            //this should be run on the apps UI Thread, i think...
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setLoaded(false);
                    //build and load ad here.
                   // AdRequest.Builder builder = new AdRequest.Builder();
                  //  AdColony.
                    Bundle extras = new Bundle();
                    extras.putBoolean("_noRefresh", true);
                    extras.putString("ZONE_ID", "vz6ebda927dc7f49f4bb");
                    //com.jirbo.adcolony.

                   // AdRequest adRequest = new AdRequest.Builder()
                   //         .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                   //         .addNetworkExtrasBundle(MediationRewardedVideoAdAdapter.class, extras)
                   //         .build();

                   // rewardedVideoAd.loadAd(getID(), adRequest);
                }
            });
            */
        }else{
            Gdx.app.log("GameAds", "AndroidRewardAd.loadAd() called, but isConnected() == false || app == null");
        }

    }

    @Override
    public void loadAd_callback() {
        Gdx.app.log("Game Ads", "AndroidRewardAd.loadAd_callback() called");
        setLoaded(true);
    }

    public void closeAd_callback(){setShowing(false);}

    @Override
    public <T> void setVisibility(T vis) {
        //we do nothing here, reward ads don't use this, only banner ads display with this.
    }

    @Override
    public void showAd() {
        Gdx.app.log("GameAds", "AndroidRewardAd.showAd() called");
        if(isLoaded()){
            //we want to do this on the ui thread, hopefully.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setShowing(true);
                    rewardedVideoAd.show();
                    //rewardedVideoAd.show();
                }
            });
        }else{
            Gdx.app.log("GameAds", "AndroidRewardAd.showAd() called, but ad is not loaded.");
        }
    }

    @Override
    public void pause() {
        AdColony.pause();
        //rewardedVideoAd.pause(app);
    }

    @Override
    public void resume() {
        AdColony.resume(app);
        //rewardedVideoAd.resume(app);
    }

    @Override
    public void destroy() {
        //do nothing?
        //AdColony.
        //rewardedVideoAd.destroy(app);
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

    @Override
    public void onAdColonyV4VCReward(AdColonyV4VCReward adColonyV4VCReward) {
        Gdx.app.log("GameAds", "AndroidRewardAd2.onAdColonyV4VCReward() called");
        Gdx.app.log("GameAds", "success: " + adColonyV4VCReward.success());
        Gdx.app.log("GameAds", "name: " + adColonyV4VCReward.name());
        Gdx.app.log("GameAds", "amount: " + adColonyV4VCReward.amount());

    }


/* Private Classes. */


}
