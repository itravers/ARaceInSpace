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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    TextButton hideBannerAdButton;
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

        Label iapAdLabel = new Label("In-App purchases- ", skin);
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
        loadBannerAdButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               System.out.println("loadBannerAdButton clicked");
                monetizationController.loadBannerAd();
            };
        });

        showBannerAdButton = new TextButton("ShowAD", skin, "default");
        showBannerAdButton.setWidth(Gdx.graphics.getWidth()/6);
        showBannerAdButton.setHeight(buttonHeight);
        showBannerAdButton.setPosition((loadBannerAdButton.getX() + loadBannerAdButton.getWidth()+spacer), bannerAdLabel.getY());
        showBannerAdButton.setTouchable(Touchable.disabled);
        showBannerAdButton.setVisible(false);
        showBannerAdButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("showBannerAdButton clicked");
                monetizationController.showBannerAd();
            };
        });

        hideBannerAdButton = new TextButton("HideAD", skin, "default");
        hideBannerAdButton.setWidth(Gdx.graphics.getWidth()/6);
        hideBannerAdButton.setHeight(buttonHeight);
        hideBannerAdButton.setPosition((showBannerAdButton.getX() + showBannerAdButton.getWidth()+spacer), showBannerAdButton.getY());
        hideBannerAdButton.setTouchable(Touchable.disabled);
        hideBannerAdButton.setVisible(false);
        //showBannerAdButton.setDisabled(true);

        hideBannerAdButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("hideBannerAdButton clicked");
                monetizationController.hideBannerAd();
            };
        });



        loadInterstitialAdButton = new TextButton("LoadAD", skin, "default");
        loadInterstitialAdButton.setWidth(Gdx.graphics.getWidth()/7);
        loadInterstitialAdButton.setHeight(buttonHeight);
        loadInterstitialAdButton.setPosition((interstitialAdLabel.getX() + interstitialAdLabel.getWidth()), interstitialAdLabel.getY());
        loadInterstitialAdButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("loadInterstitialAdButton clicked");
            };
        });

        showInterstitialAdButton = new TextButton("ShowAD", skin, "default");
        showInterstitialAdButton.setWidth(Gdx.graphics.getWidth()/6);
        showInterstitialAdButton.setHeight(buttonHeight);
        showInterstitialAdButton.setPosition((loadInterstitialAdButton.getX() + loadInterstitialAdButton.getWidth()+spacer), interstitialAdLabel.getY());
        showInterstitialAdButton.setTouchable(Touchable.disabled);
        showInterstitialAdButton.setVisible(false);
        showInterstitialAdButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("showInterstitialAdButton clicked");
            };
        });



        loadRewardAdButton = new TextButton("LoadAD", skin, "default");
        loadRewardAdButton.setWidth(Gdx.graphics.getWidth()/7);
        loadRewardAdButton.setHeight(buttonHeight);
        loadRewardAdButton.setPosition((rewardAdLabel.getX() + rewardAdLabel.getWidth()), rewardAdLabel.getY());
        loadRewardAdButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("loadRewardAdButton clicked");
            };
        });

        showRewardAdButton = new TextButton("ShowAD", skin, "default");
        showRewardAdButton.setWidth(Gdx.graphics.getWidth()/6);
        showRewardAdButton.setHeight(buttonHeight);
        showRewardAdButton.setPosition((loadRewardAdButton.getX() + loadRewardAdButton.getWidth()+spacer), rewardAdLabel.getY());
        showRewardAdButton.setTouchable(Touchable.disabled);
        showRewardAdButton.setVisible(false);
        showRewardAdButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("showRewardAdButton clicked");
            };
        });


        buy10CreditsButton = new TextButton("Buy 10 Credits", skin, "default");
        buy10CreditsButton.setWidth(Gdx.graphics.getWidth()/3);
        buy10CreditsButton.setHeight(buttonHeight);
        buy10CreditsButton.setPosition((iapAdLabel.getX() + iapAdLabel.getWidth()), iapAdLabel.getY());
        buy10CreditsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("buy10CreditsButton clicked");
            };
        });

        buy20CreditsButton = new TextButton("Buy 20 Credits", skin, "default");
        buy20CreditsButton.setWidth(Gdx.graphics.getWidth()/3);
        buy20CreditsButton.setHeight(buttonHeight);
        buy20CreditsButton.setPosition((buy10CreditsButton.getX() + buy10CreditsButton.getWidth()+spacer), iapAdLabel.getY());
        buy20CreditsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("buy20CreditsButton clicked");
            };
        });



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
        guiStage.addActor(hideBannerAdButton);
        guiStage.addActor(loadInterstitialAdButton);
        guiStage.addActor(showInterstitialAdButton);
        guiStage.addActor(loadRewardAdButton);
        guiStage.addActor(showRewardAdButton);
        guiStage.addActor(buy10CreditsButton);
        guiStage.addActor(buy20CreditsButton);
    }

    @Override
    public void render () {

        //update credits on screen
        updateGUI();



        xCoords++;
        if(xCoords >= Gdx.graphics.getWidth()){
            xCoords = 0;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(img, xCoords, 150);

        batch.end();

        guiStage.draw();

        monetizationController.updateVisibility();//used for banner ads to know whether to show


    }

    private void updateGUI(){
        //update changing labels
        creditAmountLabel.setText(String.valueOf(credits));

        //update button enabled
        updateButtonsEnabled();

    }

    private void updateButtonsEnabled(){
        //check banner show, should only be enabled if loaded, and not showing
        if(monetizationController.isBannerAdLoaded() && !monetizationController.isBannerAdShowing()){
            //showBannerAdButton.setDisabled(false);
            showBannerAdButton.setTouchable(Touchable.enabled);
            showBannerAdButton.setVisible(true);
        }else{
            //showBannerAdButton.setDisabled(true);
            showBannerAdButton.setTouchable(Touchable.disabled);
            showBannerAdButton.setVisible(false);
        }

        if(monetizationController.isBannerAdLoaded() && monetizationController.isBannerAdShowing()){
            //check hideBannerAdButton
            hideBannerAdButton.setTouchable(Touchable.enabled);
            hideBannerAdButton.setVisible(true);
        }else{
            //check hideBannerAdButton
            hideBannerAdButton.setTouchable(Touchable.disabled);
            hideBannerAdButton.setVisible(false);
        }

        //check the showInterstitialAdButton
        if(monetizationController.isInterstitialAdLoaded()){
            showInterstitialAdButton.setTouchable(Touchable.enabled);
            showInterstitialAdButton.setVisible(true);
        }else{
            showInterstitialAdButton.setTouchable(Touchable.disabled);
            showInterstitialAdButton.setVisible(false);
        }

        //checks the showRewardAdButton
        if(monetizationController.isRewardAdLoaded()){
            showRewardAdButton.setTouchable(Touchable.enabled);
            showRewardAdButton.setVisible(true);
        }else{
            showRewardAdButton.setTouchable(Touchable.disabled);
            showRewardAdButton.setVisible(false);
        }






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