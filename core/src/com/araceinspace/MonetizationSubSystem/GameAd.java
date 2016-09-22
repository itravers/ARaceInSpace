package com.araceinspace.MonetizationSubSystem;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * Created by Isaac Assegai on 9/22/16.
 * A GameAd is the base ad type.
 */
public abstract class GameAd {
/* Field Variables */

    /**
     * This ID is determined by the underlying
     * ads system. Google Admob will give
     * this ID when setting up an Admob Account.
     */
    private String ID;

    /**
     * Allows us to keep track if an ad
     * is currently loaded and ready to be shown.
     */
    private boolean loaded;

    /**
     * Allows us to keep track if an ad
     * is currently showing.
     */
    private boolean showing;

    /**
     * The LibGdx ApplicationAdapter we are currently using
     * this will be a AndroidApplication if we are on android
     * I believe it will be a LWJGL container on desktop.
     * It is used to access the apps views, to allow the
     * ads to be shown.
     */
    private ApplicationAdapter app;

/* Constructors */

    /**
     * Create a new GameAd with a specific ID and ApplicationAdapter.
     * @param ID The ID - Ususally manually obtained from ads service.
     * @param app The App we are displaying the ads in.
     */
    public GameAd(String ID, ApplicationAdapter app){
        this.ID = ID;
        this.app = app;
    }

/* Private Methods */


/* Abstract Methods */

    /**
     * Will be used to Asynchronously Load an ad.
     */
    public abstract void loadAd();

    /**
     * When the Ad is loaded, this method will be called.
     */
    public abstract void loadAd_callback();

    /**
     * When a caller wants to show an add, he'll use this method.
     */
    public abstract void showAd();

    /**
     * When a caller wants to hide an add, she'll use this.
     */
    public abstract void hideAd();


/* Public Methods */

    /**
     * Returns the Ad's Id to the caller
     * @return The ID.
     */
    public String getID(){
        return this.ID;
    }

    /**
     * Lets the caller know if the Ad is loaded and ready to be shown.
     * @return True if loaded.
     */
    public boolean isLoaded(){
        return loaded;
    }

    /**
     * Lets the caller know if the Ad is currently showing.
     * @return True if currently showing.
     */
    public boolean isShowing(){
        return showing;
    }
}
