package com.araceinspace.Screens;

import com.araceinspace.GameObjectSubSystem.SpriteTemplate;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Isaac Assegai on 7/16/17.
 * Abstract class that each individual screen is based off of.
 */
public abstract class Screen {
    protected RenderManager parent;
    protected ScreenViewport viewport;
    protected OrthCamera camera;
    protected SpriteBatch batch;
    protected Stage stage;
    protected MonetizationController monetizationController;
    Label coinLabel = null;

    public Screen(RenderManager parent){
        this.parent = parent;
        if(parent != null) this.monetizationController = parent.monetizationController;
        camera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        stage = new Stage();

        setup();
    }

    public void setCameraZoom(float zoom){
        camera.zoom = zoom;
    }

    public abstract void setup();
    public abstract void render(float elapsedTime);
    public abstract void dispose();
    public abstract OrthCamera getBackgroundCamera();

    public void updateCoins() {
        if(coinLabel != null) coinLabel.setText(Integer.toString(parent.parent.getCoins()));
    }

    public Stage getStage(){
        return stage;
    }
}
