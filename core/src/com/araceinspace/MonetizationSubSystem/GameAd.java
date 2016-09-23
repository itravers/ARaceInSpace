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



/* Constructors */

    /**
     * Create a new GameAd with a specific ID and ApplicationAdapter.
     * @param ID The ID - Ususally manually obtained from ads service.
     */
    public GameAd(String ID){
        this.ID = ID;
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
    //public abstract void showAd();

    /**
     * When a caller wants to hide an add, she'll use this.
     */
    //public abstract void hideAd();



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
     * Sets the Ad to the to a loaded, or unloaded state.
     * @param loaded True for loaded, False for Unloaded.
     */
    public void setLoaded(boolean loaded){
        this.loaded = loaded;
    }

    /**
     * Lets the caller know if the Ad is currently showing.
     * @return True if currently showing.
     */
    public boolean isShowing(){
        return showing;
    }

    /**
     * Lets the caller set the current showing state
     * @param show True if set to showing, False if not.
     */
    public void setShowing(boolean show){
        showing = show;
    }


}
