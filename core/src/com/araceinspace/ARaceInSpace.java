package com.araceinspace;

import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class ARaceInSpace extends ApplicationAdapter {
	/* Static Variables */
	static int frameNum = 0;

	/* Field Variables & Objects */
	MonetizationController monetizationController;
	public static EventDispatcher eventDispatcher;
	public GameWorld gameWorld;



	public ARaceInSpace(MonetizationController monetizationController){
		this.monetizationController = monetizationController;



		/** Now have our ads controller setupAds(). */
		monetizationController.setupAds();

		//monetizationController.loadBannerAd();
		//monetizationController.loadInterstitialAd();
	}
	
	@Override
	public void create () {
		this.eventDispatcher = new EventDispatcher();
		this.gameWorld = new GameWorld(this);
		//monetizationController.loadBannerAd();
	}

	@Override
	public void render () {
		//First Calculate Elapsed Time
		gameWorld.update();
	}
	
	@Override
	public void dispose () {

	}
}
