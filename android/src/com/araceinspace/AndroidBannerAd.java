package com.araceinspace;

import com.araceinspace.MonetizationSubSystem.GameAd;
import com.badlogic.gdx.ApplicationAdapter;
import com.google.android.gms.ads.AdRequest;
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

/* Constructors */

    /**
     * Create a new BannerAd with a specific ID and ApplicationAdapter.
     *
     * @param ID  The ID - Ususally manually obtained from ads service.
     * @param app The App we are displaying the ads in.
     */
    public AndroidBannerAd(String ID, ApplicationAdapter app) {
        super(ID, app);
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
