package com.araceinspace;

import com.araceinspace.MonetizationSubSystem.AdsController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ARaceInSpace extends ApplicationAdapter {
	AdsController adsController;
	SpriteBatch batch;
	Texture img;

	public ARaceInSpace(AdsController adsController){
		this.adsController = adsController;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		adsController.loadBannerAd();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

		if(adsController.isBannerLoaded() && !adsController.getBannerAdShowing()){
			adsController.showBannerAd();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
