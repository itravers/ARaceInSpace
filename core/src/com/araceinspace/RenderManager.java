package com.araceinspace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by slack on 7/10/17.
 */
public class RenderManager {
    /* Static Variables */
    static public int frameNum;

    /* Field Variables & Objects */
    SpriteBatch batch;
    Texture img;

    /* Constructor */
    public RenderManager(){
        frameNum = 0;
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    /* Methods */
    public void render(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        frameNum ++;
    }

    public void dispose(){
        batch.dispose();
        img.dispose();
    }
}
