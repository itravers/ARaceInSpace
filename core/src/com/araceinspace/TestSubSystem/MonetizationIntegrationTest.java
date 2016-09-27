package com.araceinspace.TestSubSystem;

import com.araceinspace.InputSubSystem.GameInputListener;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Isaac Assegai on 9/26/16.
 * Tests the Google Play Monetization system in coorelation with the libgdx system.
 * We will show a test page that allows the user to load and view ads,
 * and allows the user to view a video ad for a credit.
 * We also give the user the ability to buy a credit via in-app purchasing.
 */
public class MonetizationIntegrationTest extends ApplicationAdapter{

    private Viewport viewport;
    private Camera camera;


    ToastInterface toastInterface;
    public boolean showToast = false;
    public boolean toastSet = false;
    public MonetizationController monetizationController;
    SpriteBatch batch;
    Texture img;
    int xCoords = 0;

    int credits = 0; //the num of in-game test credits
    TextButton loadBannerAdButton;
    TextButton showBannerAdButton;
    TextButton loadInterstitialAdButton;
    TextButton showInterstitialAdButton;
    TextButton loadRewardAdButton;
    TextButton showRewardAdButton;
    TextButton buy10CreditsButton;
    TextButton buy20CreditsButton;
    Label creditAmountLabel;


    /**
     * Used to contain all the gui items.
     */
    Stage guiStage;



    public MonetizationIntegrationTest(MonetizationController monetizationController, ToastInterface toastInterface){
        this.monetizationController = monetizationController;
        this.toastInterface = toastInterface;
    }

    @Override
    public void create () {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        img = new Texture("isaac.png");
        //img.
        //Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(this)));
        setupGUI(viewport, batch);
    }

    /**
     * Creates all the buttons and labels and such we use on this test
     */
    public void setupGUI(Viewport viewPort, SpriteBatch batch){
        guiStage = new Stage(viewPort, batch);
       // guiStage.set
        Gdx.input.setInputProcessor(guiStage);
        //BitmapFont font = new BitmapFont();
        //TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

       // skin.addRegions(buttonAtlas);
        //TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.font = font;
       // textButtonStyle.up = skin.getDrawable("up-button");
        //textButtonStyle.down = skin.getDrawable("down-button");
        //textButtonStyle.checked = skin.getDrawable("checked-button");

        BitmapFont font = skin.getFont("default-font");
        font.getData().setScale(.13f, .66f);

        //skin.getFont("candycolouredclown").;

        int spacer = 10;
        int buttonHeight = Gdx.graphics.getHeight()/16;

        //setup labels
        Label titleLabel = new Label("Monetization System Test", skin);
        titleLabel.setPosition((Gdx.graphics.getWidth()/2)-(titleLabel.getWidth()/2), Gdx.graphics.getHeight()-(titleLabel.getHeight()+spacer)*1);

        //setup labels
        Label warningLabel = new Label("Please do not click on advertisments during this test!", skin);
        warningLabel.setFontScale(.5f);
        warningLabel.setScale(.5f);
        warningLabel.setPosition((Gdx.graphics.getWidth()/2)-(warningLabel.getWidth()/4), Gdx.graphics.getHeight()-(warningLabel.getHeight()+spacer)*2);

        Label bannerAdLabel = new Label("Banner ADs-      ", skin);
        bannerAdLabel.setFontScale(.75f);
        bannerAdLabel.setScale(.75f);
        bannerAdLabel.setPosition(20, Gdx.graphics.getHeight()-(bannerAdLabel.getHeight()+spacer)*3);

        Label interstitialAdLabel = new Label("Interstitial ADs-", skin);
        interstitialAdLabel.setFontScale(.75f);
        interstitialAdLabel.setScale(.75f);
        interstitialAdLabel.setPosition(20, Gdx.graphics.getHeight()-(interstitialAdLabel.getHeight()+spacer)*4);

        Label rewardAdLabel = new Label("Reward ADs-      ", skin);
        rewardAdLabel.setFontScale(.75f);
        rewardAdLabel.setScale(.75f);
        rewardAdLabel.setPosition(20, Gdx.graphics.getHeight()-(rewardAdLabel.getHeight()+spacer)*5);

        Label iapAdLabel = new Label("Reward ADs- ", skin);
        iapAdLabel.setFontScale(.75f);
        iapAdLabel.setScale(.75f);
        iapAdLabel.setPosition(20, Gdx.graphics.getHeight()-(iapAdLabel.getHeight()+spacer)*6);

        Label creditLabel = new Label("Credits: ", skin);
        creditLabel.setFontScale(.5f);
        creditLabel.setScale(.5f);
        creditLabel.setPosition(5, Gdx.graphics.getHeight()-creditLabel.getHeight());

        creditAmountLabel = new Label("0", skin);
        creditAmountLabel.setFontScale(.5f);
        creditAmountLabel.setScale(.5f);
        creditAmountLabel.setPosition((creditLabel.getX()+creditLabel.getWidth()+spacer), Gdx.graphics.getHeight()-creditLabel.getHeight());

        //setup buttons
        loadBannerAdButton = new TextButton("LoadAD", skin, "default");
        loadBannerAdButton.setWidth(Gdx.graphics.getWidth()/7);
        loadBannerAdButton.setHeight(buttonHeight);
        loadBannerAdButton.setPosition((bannerAdLabel.getX() + bannerAdLabel.getWidth()), bannerAdLabel.getY());

        showBannerAdButton = new TextButton("ShowAD", skin, "default");
        showBannerAdButton.setWidth(Gdx.graphics.getWidth()/6);
        showBannerAdButton.setHeight(buttonHeight);
        showBannerAdButton.setPosition((loadBannerAdButton.getX() + loadBannerAdButton.getWidth()+spacer), bannerAdLabel.getY());



        loadInterstitialAdButton = new TextButton("LoadAD", skin, "default");
        loadInterstitialAdButton.setWidth(Gdx.graphics.getWidth()/7);
        loadInterstitialAdButton.setHeight(buttonHeight);
        loadInterstitialAdButton.setPosition((interstitialAdLabel.getX() + interstitialAdLabel.getWidth()), interstitialAdLabel.getY());

        showInterstitialAdButton = new TextButton("ShowAD", skin, "default");
        showInterstitialAdButton.setWidth(Gdx.graphics.getWidth()/6);
        showInterstitialAdButton.setHeight(buttonHeight);
        showInterstitialAdButton.setPosition((loadInterstitialAdButton.getX() + loadInterstitialAdButton.getWidth()+spacer), interstitialAdLabel.getY());



        loadRewardAdButton = new TextButton("LoadAD", skin, "default");
        loadRewardAdButton.setWidth(Gdx.graphics.getWidth()/7);
        loadRewardAdButton.setHeight(buttonHeight);
        loadRewardAdButton.setPosition((rewardAdLabel.getX() + rewardAdLabel.getWidth()), rewardAdLabel.getY());

        showRewardAdButton = new TextButton("ShowAD", skin, "default");
        showRewardAdButton.setWidth(Gdx.graphics.getWidth()/6);
        showRewardAdButton.setHeight(buttonHeight);
        showRewardAdButton.setPosition((loadRewardAdButton.getX() + loadRewardAdButton.getWidth()+spacer), rewardAdLabel.getY());


        buy10CreditsButton = new TextButton("Buy 10 Credits", skin, "default");
        buy10CreditsButton.setWidth(Gdx.graphics.getWidth()/3);
        buy10CreditsButton.setHeight(buttonHeight);
        buy10CreditsButton.setPosition((iapAdLabel.getX() + iapAdLabel.getWidth()), iapAdLabel.getY());

        buy20CreditsButton = new TextButton("Buy 20 Credits", skin, "default");
        buy20CreditsButton.setWidth(Gdx.graphics.getWidth()/3);
        buy20CreditsButton.setHeight(buttonHeight);
        buy20CreditsButton.setPosition((buy10CreditsButton.getX() + buy10CreditsButton.getWidth()+spacer), iapAdLabel.getY());



        guiStage.addActor(titleLabel);
        guiStage.addActor(warningLabel);
        guiStage.addActor(bannerAdLabel);
        guiStage.addActor(interstitialAdLabel);
        guiStage.addActor(rewardAdLabel);
        guiStage.addActor(iapAdLabel);
        guiStage.addActor(creditLabel);
        guiStage.addActor(creditAmountLabel);

        guiStage.addActor(loadBannerAdButton);
        guiStage.addActor(showBannerAdButton);
        guiStage.addActor(loadInterstitialAdButton);
        guiStage.addActor(showInterstitialAdButton);
        guiStage.addActor(loadRewardAdButton);
        guiStage.addActor(showRewardAdButton);
        guiStage.addActor(buy10CreditsButton);
        guiStage.addActor(buy20CreditsButton);
    }

    @Override
    public void render () {

        xCoords++;
        if(xCoords >= Gdx.graphics.getWidth()){
            xCoords = 0;
        }
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(img, xCoords, 0);

        batch.end();

        guiStage.draw();

        monetizationController.updateVisibility();//used for banner ads to know whether to show


    }

    public void toast(final String t){
        toastInterface.toast(t);
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}