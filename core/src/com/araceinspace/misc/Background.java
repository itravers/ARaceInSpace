package com.araceinspace.misc;

import com.araceinspace.Managers.LevelManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

/**
 * Represents a single levels background.
 * Created by Isaac Assegai on 8/17/2015.
 */
public class Background {
    public static int NUM_LAYERS = 3;
    private LevelManager parent;
    private TextureRegion[] layers;

    public Background(LevelManager parent, TextureRegion[] layers){
        this.parent = parent;
        this.layers = layers;
    }

    /**
     * Returns the designated background layer.
     * If layerNum is to big, it returns the last available layer.
     * If layerNum is to small, it returns the first available layer.
     * @param layerNum The layer number we are seeking to find.
     * @return The layer itself.
     */
    public TextureRegion getLayer(int layerNum){
        TextureRegion layer;
        //Check if layerNum doesn't fit specified parameters, and fix.
        if(layerNum >= NUM_LAYERS){
            //layerNum is too high, use last available layer.
            layer = layers[NUM_LAYERS-1];
        }else if(layerNum >= 0){
            //layer num is just right, return it.
            layer = layers[layerNum];
        }else{
            //layerNum is too low, return first available layer.
            layer = layers[0];
        }
        return layer;
    }

    /**
     * Renders this background.
     * @param elapsedTime
     * @param batch
     */
    public void render(OrthCamera backgroundCamera, float elapsedTime, SpriteBatch batch){
        Matrix4 temp = batch.getProjectionMatrix();
        batch.setProjectionMatrix(backgroundCamera.calculateParallaxMatrix(.08f, .08f));
        batch.disableBlending();
        batch.begin();
        /*batch.draw(layers[0], -(int) (layers[0].getRegionWidth() / 2),
                -(int) (layers[0].getRegionHeight() / 2));
        */
        float scale = parent.parent.renderManager.getCameraZoom();
        float scaler = 1f;
        float scaler2 = scaler/2f;


        batch.draw(layers[0], -(int) (layers[0].getRegionWidth() / scaler), -(int) (layers[0].getRegionHeight() / scaler),
                +(int) (0), +(int) (0),
                layers[0].getRegionWidth() / scaler2, layers[0].getRegionHeight() / scaler2,
                1f, 1f,
                0);

         batch.end();


      /*  batch.enableBlending();
        batch.setProjectionMatrix(parent.parent.renderManager.getBackgroundCamera().calculateParallaxMatrix(.15f, .15f));
        batch.begin();
       // batch.draw(layers[3],  -(int)(layers[3].getRegionWidth() /4), -(int)(layers[3].getRegionHeight() /4),
       //         layers[3].getRegionWidth()/2, layers[3].getRegionWidth()/2);
        scaler = 10f;
        scaler2 = scaler/2f;
        batch.draw(layers[3], -(int) (layers[3].getRegionWidth() / scaler), -(int) (layers[3].getRegionHeight() / scaler),
                +(int) (0), +(int) (0),
                layers[3].getRegionWidth() / scaler2, layers[3].getRegionHeight() / scaler2,
                1f, 1f,
                0);

        batch.end();




        batch.enableBlending();
        batch.setProjectionMatrix(parent.parent.renderManager.getBackgroundCamera().calculateParallaxMatrix(.225f, .225f));
        batch.begin();
        // batch.draw(layers[2], -(int) (layers[2].getRegionWidth() / 2),
        //         -(int) (layers[2].getRegionHeight() / 2));
        //batch.draw(layers[2],  -(int)(layers[2].getRegionWidth() /3), -(int)(layers[2].getRegionHeight() / 3),
        //        layers[2].getRegionWidth() / 1.5f, layers[3].getRegionWidth() / 1.5f);
        scaler = 20f;
        scaler2 = scaler/2f;

        batch.draw(layers[2], -(int) (layers[2].getRegionWidth() / scaler), -(int) (layers[2].getRegionHeight() / scaler),
                +(int) (0), +(int) (0),
                layers[2].getRegionWidth() / scaler2, layers[2].getRegionHeight() / scaler2,
                1f, 1f,
                0);

        batch.end();


        batch.enableBlending();
        batch.setProjectionMatrix(parent.parent.renderManager.getBackgroundCamera().calculateParallaxMatrix(.4f, .4f));
        batch.begin();
       // TiledDrawable tile = new TiledDrawable(layers[1]);
        //tile.draw(batch, (layers[1].getRegionWidth() / 2f), (layers[1].getRegionHeight() / 2f),
        //        layers[1].getRegionWidth() / 1f, layers[1].getRegionWidth() / 1f);
       // batch.draw(layers[1], -(int) (layers[1].getRegionWidth() / 16f), -(int) (layers[1].getRegionHeight() / 16f),
       //         layers[1].getRegionWidth() / 8f, layers[1].getRegionWidth() / 8f);
        scaler = 24f;
        scaler2 = scaler/2f;
        batch.draw(layers[1], -(int) (layers[1].getRegionWidth() / scaler), -(int) (layers[1].getRegionHeight() / scaler),
                +(int) (0), +(int) (0),
                layers[1].getRegionWidth() / scaler2, layers[1].getRegionHeight() / scaler2,
                1f, 1f,
                 0);


                batch.end();
                */


        batch.setProjectionMatrix(temp);
    }
}
