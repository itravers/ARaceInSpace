package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.misc.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Each 2DGraphicsComponent will have a reference to the AssetManagers 2D animations.
 * Using LibGdx 2DGraphicsComponents Like Players and Ghosts should be able
 * to reuse the same animations.
 * So we don’t have multiple duplicate animations being created simultaneously
 * for different GameObjects.

 */
public class TwoDGraphicsComponent extends Sprite implements GraphicsComponent {

    /* Field Variables & Methods */
    TextureAtlas atlas;
    Animation animations;
    Animation currentAnimation;

    /*Constructor*/
    public TwoDGraphicsComponent(TextureAtlas atlas, Animation animations){
        super(atlas.getRegions().first(), 0, 0, 50, 50);
        this.atlas = atlas;
        this.animations = animations;
    }

    @Override
    public void update(float timeElapsed) {
        //TODO write graphics update code
    }

}
