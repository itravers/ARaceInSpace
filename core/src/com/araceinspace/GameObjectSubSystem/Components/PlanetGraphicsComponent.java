package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 7/14/17.
 */
public class PlanetGraphicsComponent extends TwoDGraphicsComponent {
    /* Static Variables */

    /* Field Variables & Objects */
    Planet parent;

    /* Constructors */

    public PlanetGraphicsComponent(Planet p, Vector2 loc, TextureAtlas.AtlasRegion region, Animation animations) {
        //super(null, loc, region, animations);
        super(region, animations);
        setupRendering(animations);
        parent = p;
        this.setX(loc.x);
        this.setY(loc.y);
    }

    /* Private Methods */

    /* Public Methods */

    public void render(float elapsedTime, SpriteBatch batch){
        //draw gravity well
        float m = parent.getPhysics().gravityRadius;
        float w = m, h = m;
        float x = parent.getX() +(getWidth() / 2) - w / 2;
        float y = parent.getY() + (getHeight() / 2 ) - h / 2;
        Color oldColor = batch.getColor();
        batch.setColor(1f, .6f, .25f, .25f);
        batch.draw(parent.parent.parent.resourceManager.getGravityWellAtlas().getRegions().first(), x, y, w, h);
        batch.setColor(oldColor);
        //super.render(elapsedTime, batch);
        TextureRegion frame = currentAnimation.getKeyFrame(elapsedTime, true);
        //originial
        batch.draw(frame,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());

       // batch.draw(frame, 0, 0, 0, 0, 15, 15, 1, 1, 0);

        //batch.draw(frame, getX(), getY(), getWidth(), getHeight());


       // batch.draw(parent.parent.parent.resourceManager.getPlanetAtlas().getRegions().first(), x, y);
        //batch.draw(frame,x, y);
    }

    public void setupRendering(Animation currentAnimation){
        this.currentAnimation = currentAnimation;
    }
}
