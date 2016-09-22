package com.araceinspace.MonetizationSubSystem;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * Created by Isaac Assegai on 9/22/16.
 * The BannerAd extends the base GameAd
 * and provides all the functionality needed to load
 * and show itself, provided a given
 * libgdx Application Adapter.
 */
public class BannerAd extends GameAd{

/* Field Variables */

/* Constructors */

    /**
     * Create a new BannerAd with a specific ID and ApplicationAdapter.
     *
     * @param ID  The ID - Ususally manually obtained from ads service.
     * @param app The App we are displaying the ads in.
     */
    public BannerAd(String ID, ApplicationAdapter app) {
        super(ID, app);
    }

/* Private Methods */

/* Public Methods */
}
