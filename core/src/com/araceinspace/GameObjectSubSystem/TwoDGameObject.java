package com.araceinspace.GameObjectSubSystem;

import com.araceinspace.GameObjectSubSystem.Components.TwoDGraphicsComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

/**
 * Created by Isaac Assegai on 7/10/17.
 * Any objects that will be rendered as 2d models will descend from this abstract class.
 * A TwoDGameObject’s graphics component will be a 2dGraphicsComponent,
 * all other components are the same.
 */
public abstract class TwoDGameObject extends GameObject implements AnimationController.AnimationListener{


    /**
     * Constructor
     */
    public TwoDGameObject(){
        super();
    }
}
