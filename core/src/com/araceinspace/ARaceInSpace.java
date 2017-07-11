package com.araceinspace;

import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.badlogic.gdx.ApplicationAdapter;

public class ARaceInSpace extends ApplicationAdapter {
	/* Static Variables */
	static int frameNum = 0;

	/* Field Variables & Objects */
	MonetizationController monetizationController;
	public static EventDispatcher eventDispatcher;
	public GameWorld gameWorld;
	public static RenderManager renderManager;


	public ARaceInSpace(MonetizationController monetizationController){
		this.monetizationController = monetizationController;
		this.eventDispatcher = new EventDispatcher();
		this.gameWorld = new GameWorld(this);
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
		gameWorld.renderManager.render();
	}
	
	@Override
	public void dispose () {
		renderManager.dispose();
	}
}
