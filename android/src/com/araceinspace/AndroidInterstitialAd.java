package com.araceinspace;

import com.araceinspace.MonetizationSubSystem.GameAd;

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


/* Constructors */

    /**
     * Create a new GameAd with a specific ID and ApplicationAdapter.
     *
     * @param ID The ID - Ususally manually obtained from ads service.
     */
    public AndroidInterstitialAd(String ID) {
        super(ID);
    }


/* Private Methods */


/* Public Methods */

    @Override
    public void setup() {

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
}
