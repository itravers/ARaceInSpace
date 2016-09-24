package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by Isaac Assegai on 9/23/16.
 * Encapsulate all the functionality needed for a reward ad.
 * A Reward ad is given to us from the Admob network, however
 * they don't serve the ad itself so we use mediation networks.
 */
public class AndroidRewardAd extends GameAd {

/* Field Variables */

    private RewardedVideoAd rewardedVideoAd;

    private AndroidApplication app;

    private AndroidRewardAd me;

    /**
     * Create a new GameAd with a specific ID and ApplicationAdapter.
     *
     * @param ID The ID - Ususally manually obtained from ads service.
     */
    public AndroidRewardAd(String ID, AndroidApplication app) {
        super(ID);
        this.app = app;
        me = this;
    }

/* Private Methods */



/* Public Methods */

    @Override
    public void setup() {
        Gdx.app.log("GameAds", "AndroidRewardAd.setup() called");
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(app);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardAdListener(this));

    }

    @Override
    public void loadAd() {
        Gdx.app.log("GameAds", "AndroidRewardAd.loadAd() called");
        if(isConnected() && app != null){
            //this should be run on the apps UI Thread, i think...
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setLoaded(false);
                    //build and load ad here.
                    AdRequest.Builder builder = new AdRequest.Builder();
                    AdRequest ad = builder.build();
                    rewardedVideoAd.loadAd(getID(), ad);
                }
            });
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
                }
            });
        }
    }

    @Override
    public void pause() {
        rewardedVideoAd.pause(app);
    }

    @Override
    public void resume() {
        rewardedVideoAd.resume(app);
    }

    @Override
    public void destroy() {
        rewardedVideoAd.destroy(app);
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


/* Private Classes. */

    private class RewardAdListener implements RewardedVideoAdListener {

        AndroidRewardAd ad;

        public RewardAdListener(AndroidRewardAd ad){
            super();
            this.ad = ad;
        }

        @Override
        public void onRewarded(RewardItem reward) {
            Gdx.app.log("GameAds", "onReward(): currentcy: " + reward.getType() + " amount: " +
                    reward.getAmount());
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            Gdx.app.log("GameAds", "onRewardedVideoAdLeftApplication: ");
        }

        @Override
        public void onRewardedVideoAdClosed() {
            //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            Gdx.app.log("GameAds", "onRewardedVideoAdClosed: ");
            closeAd_callback();
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            //Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            Gdx.app.log("GameAds", "onRewardedVideoAdFailedToLoad: ");
        }

        @Override
        public void onRewardedVideoAdLoaded() {
            //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            Gdx.app.log("GameAds", "onRewardedVideoAdLoaded: ");
            ad.loadAd_callback();
        }

        @Override
        public void onRewardedVideoAdOpened() {
            //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            Gdx.app.log("GameAds", "Override: ");
        }

        @Override
        public void onRewardedVideoStarted() {
            //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            Gdx.app.log("GameAds", "onRewardedVideoStarted: ");
        }
    }
}
