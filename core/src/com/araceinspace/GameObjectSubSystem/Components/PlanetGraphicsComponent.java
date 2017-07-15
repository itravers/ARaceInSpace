package com.araceinspace.GameObjectSubSystem.Components;

import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.misc.Animation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 7/14/17.
 */
public class PlanetGraphicsComponent extends PlayerGraphicsComponent {
    /* Static Variables */

    /* Field Variables & Objects */
    Planet parent;

    /* Constructors */

    public PlanetGraphicsComponent(Planet p, Vector2 loc, TextureAtlas atlas, Animation animations) {
        super(null, loc, atlas, animations);
        parent = p;
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
        batch.draw(parent.parent.parent.animationManager.getGravityWellAtlas().getRegions().first(), x, y, w, h);
        batch.setColor(oldColor);
        super.render(elapsedTime, batch);
    }
}
