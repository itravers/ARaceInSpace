package com.araceinspace.MonetizationSubSystem;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * Created by Isaac Assegai on 9/22/16.
 * A GameAd is the base ad type.
 */
public abstract class GameAd {
    private String ID;
    private boolean loaded;
    private boolean showing;
    private ApplicationAdapter app;

    public abstract String getID();
    public abstract boolean isLoaded();
    public abstract boolean isShowing();
}
