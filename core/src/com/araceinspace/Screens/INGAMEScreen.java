package com.araceinspace.Screens;

import com.araceinspace.ARaceInSpace;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.util.ArrayList;

/**
 * Created by The screen to be rendered when the GAME_STATE is INGAME on 7/16/17.
 */
public class INGAMEScreen extends Screen{

    /* Static Variables */

    /* Field Variables & Objects */
    private OrthCamera backgroundCamera;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private SpriteBatch backgroundBatch;
    private BitmapFont font;

    /* Constructors */

    public INGAMEScreen(RenderManager p) {
        super(p);
    }

    @Override
    public void setup() {
        font = new BitmapFont();
        backgroundCamera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch = new SpriteBatch();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        debugRenderer = new Box2DDebugRenderer();
        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);
    }

    /* Private Methods */

    private void renderVersion(SpriteBatch batch){
        font.setColor(Color.WHITE);
        font.draw(batch, "Version: " +((ARaceInSpace)parent.parent.parent).version, parent.parent.levelManager.getPlayer().getX(),parent.parent.levelManager.getPlayer().getY());
    }

    /* Public Methods */

    @Override
    public void render(float timeElapsed) {
        Player p = parent.parent.levelManager.getPlayer();
        ArrayList<Planet> planets = parent.parent.levelManager.getPlanets();
        camera.zoom = parent.getCameraZoom();
        backgroundCamera.zoom = parent.getCameraZoom();
        camera.position.set(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 0);
        camera.setToAngle(p.getPhysics().getBody().getAngle());
        camera.update();

        backgroundCamera.position.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2, 0);
        backgroundCamera.setToAngle(p.getPhysics().getBody().getAngle());
        backgroundCamera.update();
        backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
        batch.setProjectionMatrix(camera.combined);

        //clear screen
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        parent.renderBackground(backgroundCamera, timeElapsed, backgroundBatch);

        batch.begin();
        parent.renderPlayer(p, timeElapsed, batch);
        parent.renderPlanets(planets, timeElapsed, batch);
        if(parent.parent.devMode){
            renderVersion(batch);
        }
        batch.end();
        //stage.act(timeElapsed);
        // stage.draw();

        debugMatrix = batch.getProjectionMatrix().cpy().scale(parent.PIXELS_TO_METERS, parent.PIXELS_TO_METERS, 0);
        if(parent.parent.devMode){
            debugRenderer.render(parent.parent.levelManager.getWorld(), debugMatrix);
        }

    }

    @Override
    public void dispose() {

    }

    public void setCameraZoom(float zoom){
        super.setCameraZoom(zoom);
        backgroundCamera.zoom = zoom;
    }
}
