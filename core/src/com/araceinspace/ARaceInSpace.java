package com.araceinspace;

import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ARaceInSpace extends ApplicationAdapter {
	/* Static Variables */
	static int frameNum = 0;

	/* Field Variables & Objects */
	MonetizationController monetizationController;
	public static EventDispatcher eventDispatcher;
	public static RenderManager renderManager;


	public ARaceInSpace(MonetizationController monetizationController){
		this.monetizationController = monetizationController;
		this.eventDispatcher = new EventDispatcher();
		this.renderManager = new RenderManager();

		/** Now have our ads controller setupAds(). */
		monetizationController.setupAds();

		//monetizationController.loadBannerAd();
		//monetizationController.loadInterstitialAd();
	}
	
	@Override
	public void create () {

		//monetizationController.loadBannerAd();
	}

	@Override
	public void render () {
		renderManager.render();
	}
	
	@Override
	public void dispose () {
		renderManager.dispose();
	}
}
