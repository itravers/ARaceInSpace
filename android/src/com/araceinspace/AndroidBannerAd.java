package com.araceinspace;

import android.view.View;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.android.AndroidApplication;
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
public class AndroidBannerAd extends GameAd {

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

    }

/* Private Methods */

/* Public Methods */

    @Override
    public void loadAd() {

    }

    @Override
    public void loadAd_callback() {

    }

    @Override
    public void showAd() {

    }

    @Override
    public void hideAd() {

    }
}
