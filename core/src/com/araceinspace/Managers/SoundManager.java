package com.araceinspace.Managers;

import com.araceinspace.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Isaac Assegai on 7/19/17.
 * controls every aspect of sound in the game
 */
public class SoundManager {
    /* Static Variables */

    /* Field Variables & Objects */
    GameWorld parent;
    public Sound beethovens7th;

    /* Constructors */
    public SoundManager(GameWorld p){
        System.out.println("Constructing SoundManager");
        parent = p;
        setupSounds();
        playSong(beethovens7th);
    }

    /* Private Methods */

    private void setupSounds(){
        beethovens7th = Gdx.audio.newSound(Gdx.files.internal("data/beethoven7th.mp3"));
    }

    /* Public Methods */
    public void playSong(Sound s){
        s.play();
    }
}
