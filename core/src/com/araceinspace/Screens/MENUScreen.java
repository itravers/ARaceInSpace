package com.araceinspace.Screens;

import com.araceinspace.Managers.GameStateManager;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.misc.OrthCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Isaac Assegai on 7/16/17.
 * This screen will contain everything needed to implement the Level Select Functionality
 */
public class MENUScreen extends Screen {
    /* Static Variables */

    /* Field Variables & Objects */
    int spacer;
    private Skin skin;
    Table table;
    Table headerTable;
    Table bodyTable;
    Table menuTable;
    Table levelSectionTable;

    private Label titleLabel;

    private ClickListener coinButtonListener;
    private  ClickListener backButtonListener;
    private ClickListener rewardAdButtonListener;
    private ClickListener muteMusicButtonListener;
    private ChangeListener musicVolumeSliderListener;
    private ClickListener changeNameButtonListener;

    private ImageButton backButton;
    private ImageButton coinButton;
    private ImageButton rewardButton;
    private ImageTextButton restartButton;
    private ImageTextButton exitButton;
    private ImageTextButton musicMuteButton;
    private ImageTextButton sfxMuteButton;
    private ImageTextButton resolutionApplyButton;
    private ImageTextButton creditsButton;
    private ImageTextButton changeNameButton;
    private SelectBox selectBox;

    private Slider musicVolumeSlider;
    private Slider sfxVolumeSlider;




    ScrollPane scrollPane;

    /* Constructors */

    public MENUScreen(RenderManager parent) {
        super(parent);
    }

    /* Private Methods */

    private void setupSkin(){
        //TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("aris_uiskin.atlas"));
        //skin = new Skin(Gdx.files.internal("aris_uiskin.json"), atlas);
        skin = parent.parent.resourceManager.getSkin();
    }

    private void setupButtons(){
        coinButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                parent.parent.gameStateManager.setCurrentState(GameStateManager.GAME_STATE.STORE);
            }

        };

        backButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                GameStateManager.GAME_STATE stateBefore = parent.parent.gameStateManager.getCurrentState();
                GameStateManager.GAME_STATE stateAfter = parent.parent.gameStateManager.popState();



                parent.parent.gameStateManager.setCurrentState(stateAfter);
            }

        };

        rewardAdButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // resize(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
                //parent.parent.gameStateManager.setCurrentState(parent.parent.gameStateManager.popState());
                monetizationController.loadRewardAd();
                monetizationController.showRewardAd();
            }

        };

        muteMusicButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(parent.parent.soundManager.isMusicMuted()){
                    parent.parent.soundManager.setMusicVolume(.5f);
                    musicMuteButton.getLabel().setText("Mute");
                    musicVolumeSlider.setValue(.5f*100);
                }else{
                    parent.parent.soundManager.setMusicVolume(0);
                    musicMuteButton.getLabel().setText("Unmute");
                    musicVolumeSlider.setValue(0);
                }
            }
        };

        changeNameButtonListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
               parent.nameDialog.show(stage);
            }

        };

        backButton = new ImageButton(skin, "backButton");
        backButton.addListener(backButtonListener);

        coinButton = new ImageButton(skin, "coinButton");
        coinButton.addListener(coinButtonListener);

        rewardButton = new ImageButton(skin, "rewardButton");
        rewardButton.addListener(rewardAdButtonListener);

        restartButton = new ImageTextButton("Restart", skin);
        restartButton.addListener(rewardAdButtonListener);
      //  restartButton.setWidth(restartButton.getWidth()+20);

        exitButton = new ImageTextButton("Exit", skin);
        exitButton.addListener(rewardAdButtonListener);
       // exitButton.setWidth(restartButton.getWidth());
        musicMuteButton = new ImageTextButton("Mute", skin);
        musicMuteButton.addListener(muteMusicButtonListener);

        sfxMuteButton = new ImageTextButton("Mute", skin);
        sfxMuteButton.addListener(rewardAdButtonListener);

        resolutionApplyButton = new ImageTextButton("Apply", skin);
        resolutionApplyButton.addListener(rewardAdButtonListener);

        creditsButton = new ImageTextButton("View Credits", skin);
        creditsButton.addListener(rewardAdButtonListener);

        selectBox = new SelectBox(skin);
        selectBox.setItems("   1200X1080", "   720X480");
        selectBox.setSelectedIndex(0);

    }

    private void setupLabels(){
        titleLabel = new Label("Menu", skin, "Store_Title");
        String coins = Integer.toString(parent.parent.getCoins());
        coinLabel = new Label(coins, skin, "coinLabel");
        coinLabel.setAlignment(Align.right);
    }

    private void setupSliders(){

        musicVolumeSlider = new Slider(0, 100, 1, false, skin);
        sfxVolumeSlider = new Slider(0, 100, 1, false, skin);

        musicVolumeSliderListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

              // System.out.println( ((Slider)actor).getValue());
                float volume = ((Slider)actor).getValue()/100;
                parent.parent.soundManager.setMusicVolume(volume);
                if(volume == 0){
                    musicMuteButton.getLabel().setText("Unmute");
                }else{
                    musicMuteButton.getLabel().setText("Mute");
                }
            }
        };
        musicVolumeSlider.addListener(musicVolumeSliderListener);

        float musicVolume = parent.parent.soundManager.getMusicVolume()*100;
        musicVolumeSlider.setValue(musicVolume);
    }

    private void setupTables(){
        float width = viewport.getScreenWidth();
        float height = viewport.getScreenHeight();

        table = new Table();
        table.setDebug(parent.parent.devMode);
        table.setWidth(width);
        table.align(Align.center|Align.top);
        table.setPosition(0, height);

        headerTable = new Table();
        headerTable.setDebug(parent.parent.devMode);
        headerTable.align(Align.center|Align.top);

        headerTable.add(backButton).padLeft(spacer).padTop(0).size(width/8, height/10);
        headerTable.add(rewardButton).size(width/8, height/12).padLeft(spacer/1).padTop(spacer/4).align(Align.left).spaceLeft(0);
        headerTable.add(titleLabel).expandX().align(Align.center).size(width/3, height/12);

        headerTable.add(coinLabel).size(width/6, height/12).align(Align.right);
        headerTable.add(coinButton).size(width/8, height/10).padTop(0).padRight(spacer);
        headerTable.row();

        bodyTable = new Table();
        bodyTable.setDebug(parent.parent.devMode);
        bodyTable.align(Align.center);

        menuTable = new Table();
        menuTable.setDebug(parent.parent.devMode);
        menuTable.align(Align.top|Align.center);

        Tree tree = new Tree(skin, "default");

        Tree.Node levelSectionNode = new Tree.Node(new   Label("    Level Section", skin, "tree_header"));
        levelSectionTable = new Table();
        levelSectionTable.setDebug(parent.parent.devMode);
        levelSectionTable.align(Align.left|Align.top);
        levelSectionTable.add(restartButton).width(width/3.5f).fill().expandX().spaceRight(width/20);
        levelSectionTable.add(exitButton).width(width/3.5f).fill().expandX();
        Tree.Node levelSectionChild = new Tree.Node(levelSectionTable);
        levelSectionNode.add(levelSectionChild);

        Tree.Node optionsSection = new Tree.Node(new Label("   Options Section", skin, "tree_header"));
        Table optionsSectionTable = new Table();
        optionsSectionTable.setDebug(parent.parent.devMode);
        optionsSectionTable.align(Align.left|Align.top);
        changeNameButton = new ImageTextButton("Change Name", skin);
        changeNameButton.addListener(changeNameButtonListener);
        optionsSectionTable.add(changeNameButton).width(width/2).fill().expandX();
        Tree.Node optionSectionChild = new Tree.Node(optionsSectionTable);
        optionsSection.add(optionSectionChild);

        Tree.Node soundSelectionNode = new Tree.Node(new Label("    Sound Section", skin, "tree_header"));
        Table soundSectionTable = new Table();
        soundSectionTable.setDebug(parent.parent.devMode);
        soundSectionTable.align(Align.left|Align.top);
        soundSectionTable.add(new Label("Music Volume", skin, "extra_small"));
        soundSectionTable.row();
        soundSectionTable.add(musicVolumeSlider).width(width/2.8f).fill().expandX().spaceRight(width/20);
        soundSectionTable.add(musicMuteButton).width(width/4.2f).fill().expandX();
        soundSectionTable.row();
        soundSectionTable.add(new Label("  SFX Volume", skin, "extra_small"));
        soundSectionTable.row();
        soundSectionTable.add(sfxVolumeSlider).width(width/2.8f).fill().expandX().spaceRight(width/20);
        soundSectionTable.add(sfxMuteButton).width(width/4.2f).fill().expandX();
        Tree.Node soundSectionChild = new Tree.Node(soundSectionTable);
        soundSelectionNode.add(soundSectionChild);

        Tree.Node videoSectionNode = new Tree.Node(new Label("    Video Section", skin, "tree_header"));
        Table videoSectionTable = new Table();
        videoSectionTable.setDebug(parent.parent.devMode);
        videoSectionTable.align(Align.left|Align.top);
        videoSectionTable.add(new Label("Resolution", skin, "extra_small"));
        videoSectionTable.row();
        videoSectionTable.add(selectBox).width(width/2f).fill().expandX().spaceRight(width/20);
        videoSectionTable.add( resolutionApplyButton);
        Tree.Node videoSectionChild = new Tree.Node(videoSectionTable);
        videoSectionNode.add(videoSectionChild);

        Tree.Node creditsSectionNode = new Tree.Node(new Label("    Credits Section", skin, "tree_header"));
        Table creditsSectionTable = new Table();
        creditsSectionTable.setDebug(parent.parent.devMode);
        creditsSectionTable.align(Align.left|Align.top);
        creditsSectionTable.add(creditsButton).width(width/2).fill().expandX();
        Tree.Node creditsSectionChild = new Tree.Node(creditsSectionTable);
        creditsSectionNode.add(creditsSectionChild);


        Tree.Node moo2 = new Tree.Node(new TextButton("moo2", skin));
        Tree.Node moo3 = new Tree.Node(new TextButton("moo3", skin));
        Tree.Node moo4 = new Tree.Node(new TextButton("moo4", skin));
        tree.add(levelSectionNode);
        tree.add(optionsSection);
        tree.add(soundSelectionNode);
        tree.add(videoSectionNode);
        tree.add(creditsSectionNode);
        //tree.add(moo4);

        tree.expandAll();


        menuTable.add(tree).fill().expandX();



        scrollPane = new ScrollPane(menuTable, skin, "default");
        bodyTable.add(scrollPane).width(width*.95f).height(height*.755f).padLeft(0).align(Align.top|Align.center);//set the scroll pane size

        table.add(headerTable).fill().expandX();
        table.row();
        table.add(bodyTable).fill().expandX();

        stage.addActor(table);

    }

    private void showdebug(){
        boolean debug = parent.parent.devMode;
        table.setDebug(debug);
        bodyTable.setDebug(debug);
        headerTable.setDebug(debug);
        menuTable.setDebug(debug);
        levelSectionTable.setDebug(debug);
    }

    /* Public Methods */

    /**
     * Automatically gets called from extended class.
     */
    @Override
    public void setup() {
        stage = new Stage(viewport, batch);
        setupSkin();
        setupButtons();
        setupLabels();
        setupSliders();
        setupTables();

        parent.setupNameDialog(skin, stage, this);

        //stage.addActor(mainTable);
        parent.parent.inputManager.addInputProcessor(stage);
    }

    /**
     * Gets called from render manager
     * @param elapsedTime
     */
    @Override
    public void render(float elapsedTime) {

        if(monetizationController.isBannerAdLoaded())monetizationController.showBannerAd();
        showdebug();
        Gdx.gl.glClearColor(.447f, .2784f, .3843f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
      //  skin.dispose();
        batch.dispose();

    }

    @Override
    public OrthCamera getBackgroundCamera() {
        return null;
    }

}
