package com.araceinspace.Screens;

import com.araceinspace.GameObjectSubSystem.SpriteTemplate;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.text.View;

/**
 * Created by Isaac Assegai on 7/16/17.
 * Abstract class that each individual screen is based off of.
 */
public abstract class Screen {
    protected RenderManager parent;
    protected ScreenViewport viewport;
    protected OrthCamera camera;
    protected ClipBatch batch;
    protected Stage stage;
    protected MonetizationController monetizationController;
    Label coinLabel = null;

    public Screen(RenderManager parent){
        this.parent = parent;
        if(parent != null) this.monetizationController = parent.monetizationController;
        camera = new OrthCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        batch = new ClipBatch();
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

    public class ClipBatch extends SpriteBatch {

        public final ShaderProgram clipShader;
        protected Rectangle clip = new Rectangle();
        protected float rotation;

        protected boolean drawing = false;

        public ClipBatch() {
            super();
            ShaderProgram.pedantic = false;
            clipShader = new ShaderProgram(
                    Gdx.files.internal("data/clip.vert"),
                    Gdx.files.internal("data/clip.frag"));
            if (!clipShader.isCompiled())
                throw new GdxRuntimeException(clipShader.getLog());
            if (clipShader.getLog().length()!=0)
                Gdx.app.log("ClipBatch", clipShader.getLog());

            clip.width = Gdx.graphics.getWidth();
            clip.height = Gdx.graphics.getHeight();
        }

        /**
         * Begins a new batch with the clipping shader enabled, using the given bounds and rotation
         * (lower-left origin).
         */
        public void begin(float x, float y, float width, float height, float rotation, ShapeRenderer shapeRenderer) {
            //set new clip region and rotation
            Vector3 wtoC = camera.project(new Vector3(x, y, 0));
            Vector3 wToV = viewport.project(new Vector3(x, y, 0));

            clip.set(wtoC.x, wtoC.y, width, height);
            this.rotation = rotation;

            if(parent.parent.devMode){
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.RED);

                //shapeRenderer.rect(wToV.x, wToV.y, width*(camera.zoom), height*(camera.zoom));//causes shape to change size with zoom
                //System.out.println("rotation: " + camera.getCurrentAngle());
                /**
                 * Draw rect with given rotation, in shape renderer world, rotation = clip rotation - camera rotation
                 */
                shapeRenderer.rect(wToV.x, wToV.y, 0,0, width, height, 1, 1, (float)Math.toDegrees(rotation)-camera.getCurrentAngle());//draws rect with given rotation,
                //shapeRenderer.rect(wToV.x, wToV.y, width, height);
                shapeRenderer.circle(wToV.x,wToV.y, 5);


                shapeRenderer.end();
            }





            //set the shader and use() it
            setShader(clipShader);
            super.begin();

            //update shader uniforms
            clipShader.setUniformf("clip_pos", clip.x, clip.y);
            //clipShader.setUniformf("clip_size", clip.width, clip.height); //causes shader to change size with zoom
            clipShader.setUniformf("clip_size", clip.width*(1/camera.zoom), clip.height*(1/camera.zoom));//causes shader to stay the same with zoom
            clipShader.setUniformf("clip_rotation", rotation);


        }

        /**
         * Starts a new batch with the default SpriteBatch shader.
         */
        public void begin() {
            setShader(null);
            super.begin();
        }

    }
}
