package com.araceinspace;

import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ARaceInSpace extends ApplicationAdapter {
	MonetizationController monetizationController;
	public static EventDispatcher eventDispatcher;
	SpriteBatch batch;
	Texture img;

	public ARaceInSpace(MonetizationController monetizationController){
		this.monetizationController = monetizationController;
		this.eventDispatcher = new EventDispatcher();

		/** Now have our ads controller setupAds(). */
		monetizationController.setupAds();

		//monetizationController.loadBannerAd();
		//monetizationController.loadInterstitialAd();
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		//monetizationController.loadBannerAd();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

		//if(monetizationController.isBannerLoaded() && !monetizationController.isBannerAdShowing()){
		//	monetizationController.showBannerAd();
		//}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
