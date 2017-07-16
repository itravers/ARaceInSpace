package com.araceinspace.Screens;

import com.araceinspace.GameObjectSubSystem.SpriteTemplate;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Isaac Assegai on 7/16/17.
 * Abstract class that each individual screen is based off of.
 */
public abstract class Screen {
    protected RenderManager parent;
    protected Viewport viewport;
    protected Camera camera;
    protected SpriteBatch batch;
    protected Stage stage;

    public Screen(RenderManager parent){
        this.parent = parent;
        camera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        setup();
    }

    public abstract void setup();
    public abstract void render(float elapsedTime);
    public abstract void dispose();
}
