package com.araceinspace;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
    private AndroidBannerAd me;

/* Constructors */

    /**
     * Create a new BannerAd with a specific ID and ApplicationAdapter.
     *
     * @param ID  The ID - Ususally manually obtained from ads service.
     * @param app The App we are displaying the ads in.
     */
    public AndroidBannerAd(final String ID, final AndroidApplication app) {
        super(ID);
        System.out.println("game ads: AndroidBannerAd constructed on thread:" + Thread.currentThread().getName());

        this.app = app;
        me = this;




    }

/* Private Methods */

/* Public Methods */

    public void setup(){
        System.out.println("game ads: bannerAd constructed on thread:" + Thread.currentThread().getName());
        bannerAd = new AdView(app);
        bannerAd.setVisibility(View.VISIBLE);
        bannerAd.setVisibility(View.INVISIBLE);
        bannerAd.setAdUnitId(getID());
        //bannerAd.setBackgroundColor(0xff000000);
        bannerAd.setAdSize(AdSize.SMART_BANNER);


       bannerAd.setAdListener(new BannerListener(me));


    }

    /**
     * Causes the google api to load the ad into
     * bannerAd. Once loaded the loadAd_callback()
     * method will be called by the bannerAd's listener.
     */
    @Override
    public void loadAd() {
        System.out.println("game ads : loadAd() called from thread:" + Thread.currentThread().getName());
        if(isConnected() && app !=null){
            //needs to be run on the aps UI thread.
            app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setLoaded(false);
                    AdRequest.Builder builder = new AdRequest.Builder();
                    //builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                    //builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                    rawBannerAd = builder.build();
                    System.out.println("game ads : about to load ad on thread:" + Thread.currentThread().getName());
                    bannerAd.loadAd(rawBannerAd);

                    //test
                  //  bannerAd.setVisibility(View.VISIBLE);
                   // setShowing(true);
                }
            });

            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    // do something important here, asynchronously to the rendering thread
                    System.out.println("game ads :inside generaic runnable on thread:" + Thread.currentThread().getName());

                    // post a Runnable to the rendering thread that processes the result
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.

                            setLoaded(false);
                            AdRequest.Builder builder = new AdRequest.Builder();
                            //builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                            //builder.addTestDevice("752B44EB5165C7A81E9423963C07AC77");
                            builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                            rawBannerAd = builder.build();
                            System.out.println("game ads : about to load ad on thread:" + Thread.currentThread().getName());
                            Looper.prepare();
                            //bannerAd.setAdListener(new BannerListener(me));
                            bannerAd.loadAd(rawBannerAd);

                        }
                    });
                }

            }).start();*/


        }else{
            System.out.println("game ads : loadAd() called, but no connection available, or app is null + " + app);
        }

    }

    /**
     * Once the add is loaded, the listener will call back this method.
     * Here we will decide what to do when the ad is loaded.
     * for now we will just setLoaded(true)
     */
    @Override
    public void loadAd_callback() {
        System.out.println("game ads : loadAd_callback() called on thread:"  + Thread.currentThread().getName());
        //test setting visibility true on loadAdCallback, maybe it's a thread issue?
        //bannerAd.setVisibility(View.VISIBLE);
       // setShowing(true);


        setLoaded(true);

    }

    //@Override
   // public void showAd() {
      //  System.out.println("game ads : showAd() called on thread:" + Thread.currentThread().getName());

      //  if(isLoaded()){
           // bannerAd.setVisibility(View.VISIBLE);
            //setShowing(true);
            //Showing the Ad, needs to be run on the UI Thread.
           /* app.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("game ads : showAd() running on thread:" + Thread.currentThread().getName());
                    bannerAd.setVisibility(View.VISIBLE);

                    setShowing(true);
                }
           });/*
            /*  new Thread(new Runnable() {
                @Override
                public void run() {
                    // do something important here, asynchronously to the rendering thread
                    System.out.println("game ads :inside generaic runnable on thread:" + Thread.currentThread().getName());

                    // post a Runnable to the rendering thread that processes the result
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.

                            System.out.println("game ads : showAd() running on thread:" + Thread.currentThread().getName());
                            bannerAd.setVisibility(View.VISIBLE);
                            setShowing(true);

                        }
                    });
                }
            }).start();*/
       // }else{
       //     System.out.println("game ads : showAds() called, but isLoaded() == false");
      //  }
    //}


    public void setVisibility(final int vis){

        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.bringToFront();
                bannerAd.setVisibility(vis);
            }
        });

    }

    //@Override
   // public void hideAd() {
   //     System.out.println("game ads : hideAd() called on thread:" + Thread.currentThread().getName());
   //     bannerAd.setVisibility(View.INVISIBLE);


        //needs to be run on the UI Thread
        /*app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("game ads : hideAd() ran on thread:" + Thread.currentThread().getName());
                bannerAd.setVisibility(View.INVISIBLE);

                setShowing(false);
                //Now we want to prep loading the next banner ad to be shown
               // loadAd();
            }
        });*/
    //}

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
            if(i == AdRequest.ERROR_CODE_INTERNAL_ERROR)System.out.println("Error ERROR_CODE_INTERNAL_ERROR");
            if(i == AdRequest.ERROR_CODE_INVALID_REQUEST)System.out.println("Error ERROR_CODE_INVALID_REQUEST");
            if(i == AdRequest.ERROR_CODE_NETWORK_ERROR)System.out.println("Error ERROR_CODE_NETWORK_ERROR");
            if(i == AdRequest.ERROR_CODE_NO_FILL)System.out.println("Error ERROR_CODE_NO_FILL");
        }

        /**
         * When a banner ad is loaded, this is called.
         */
        @Override
        public void onAdLoaded() {
            System.out.println("game ads :onAdLoaded() thread:" + Thread.currentThread().getName());
           // super.onAdLoaded();

            ad.loadAd_callback();

        }

        
    }
}
