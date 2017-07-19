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

    private Sound currentSong;
    long currentSongId;
    float musicVolume;

    boolean musicMuted;

    /* Constructors */
    public SoundManager(GameWorld p){
        System.out.println("Constructing SoundManager");
        parent = p;
        setupSounds();
    }

    /* Private Methods */

    private void setupSounds(){

        beethovens7th = Gdx.audio.newSound(Gdx.files.internal("data/beethoven7th.mp3"));
        playSong(beethovens7th);
        setMusicVolume(.5f);
    }

    /* Public Methods */
    public void playSong(Sound s){
        currentSong = s;
        currentSongId = s.play();
    }

    public void setMusicVolume(float musicVolume){
        if(musicVolume == 0){
            musicMuted = true;
        }else{
            musicMuted = false;
        }
        this.musicVolume = musicVolume;
        currentSong.setVolume(currentSongId, musicVolume);

    }

    public boolean isMusicMuted(){
        return musicMuted;
    }

    public float getMusicVolume(){
        return musicVolume;
    }
}
