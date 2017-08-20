package com.araceinspace.Screens;

import com.araceinspace.EventSubSystem.Event;
import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.EventSubSystem.EventSender;
import com.araceinspace.GameObjectSubSystem.Planet;
import com.araceinspace.GameObjectSubSystem.Player;
import com.araceinspace.InputSubSystem.GameInput;
import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.FontGenerator;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by The screen to be rendered when the GAME_STATE is TITLE on 7/16/17.
 */
public class LOADINGScreen extends Screen{

    /* Static Variables */

    /* Field Variables & Objects */
    private Skin skin;
    private Table mainTable;
    public Slider progressBar;

    /* Constructors */

    public LOADINGScreen(RenderManager p) {
        super(p);
    }

    /* Private Methods */

    private void setupSkin(){
        //skin = parent.parent.resourceManager.getSkin();
        /**
         * Doesn't use resourceManger to load
         */
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
    }

    /* Public Methods */
    @Override
    public void setup() {
        setupSkin();
        stage = new Stage(viewport, batch);
        mainTable = new Table();
        mainTable.setWidth(viewport.getScreenWidth());

        mainTable.align(Align.center|Align.center);
        mainTable.setPosition(0, viewport.getScreenHeight()/2);
        mainTable.setDebug(false);
        BitmapFont font = FontGenerator.createFont(new FreeTypeFontGenerator(Gdx.files.internal("Font_Destroy.ttf")), 60);
        Label loadingLabel = new Label("LOADING", skin);
        loadingLabel.getStyle().font = font;



        progressBar = new Slider(0, 1, .01f, false, skin, "progress-bar");


        mainTable.add(loadingLabel);
        mainTable.row();
        mainTable.add(progressBar).width(viewport.getScreenWidth()/2);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float elapsedTime) {
        Gdx.gl.glClearColor(.447f, .2784f, .3843f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();;
        batch.dispose();
    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return null;
    }


}