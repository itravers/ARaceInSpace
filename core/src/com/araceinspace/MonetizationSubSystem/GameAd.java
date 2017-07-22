package com.araceinspace.MonetizationSubSystem;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * Created by Isaac Assegai on 9/22/16.
 * A GameAd is the base ad type. Any object
 * extending game ad should be able to implement
 * the following abstract methods. GameAd provides
 * some State functionality so we can know
 * if an ad has been loaded, or is showing.
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
     * Setup a GameAd so it's ready to use.
     */
    public abstract void setup();

    /**
     * Will be used to Asynchronously Load an ad.
     */
    public abstract void loadAd();

    /**
     * When the Ad is loaded, this method will be called.
     */
    public abstract void loadAd_callback();

    /**
     * The method called by the render thread to set visibility.
     * Since the base GameAd doesn't know anything about
     * the actual graphical components of an Ad.
     * The subclass will have to implement these capabilities.
     * Only the bannerAd types will need to inherit this.
     * @param vis Can be interpreted by subclass however they want
     */
    public abstract <T> void setVisibility(T vis);

    /**
     * Shows the current ad, only valid for interstitial type
     * banner type will use setVisilibty.
     */
    public abstract void showAd();

    /**
     * Pause the ad, used to call any underlying pause() methods.
     */
    public abstract void pause();

    /**
     * Resume the ad, after being paused,
     * used to call the underlying resume() methods.
     */
    public abstract void resume();

    /**
     * Destroy the add, used to call underlying destroy() methods.
     */
    public abstract void destroy();

    /**
     * Checks to see if the app is connected to the internet.
     * @return
     */
    public abstract boolean isConnected();


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
        System.out.println("game ads: setShowing: "+show);
        showing = show;
    }


}