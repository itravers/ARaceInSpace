package com.araceinspace;

import com.araceinspace.EventSubSystem.EventDispatcher;
import com.araceinspace.Managers.RenderManager;
import com.araceinspace.MonetizationSubSystem.MonetizationController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class ARaceInSpace extends ApplicationAdapter {
	/* Static Variables */
	public static String version = "0.0.4";
	static int frameNum = 0;

	/* Field Variables & Objects */
	ToastInterface toastInterface;
	public boolean showToast = false;
	public boolean toastSet = false;

	public MonetizationController monetizationController;
	public static EventDispatcher eventDispatcher;
	public GameWorld gameWorld;



	public ARaceInSpace(MonetizationController monetizationController, ToastInterface toastInterface){
		this.monetizationController = monetizationController;
		this.toastInterface = toastInterface;





		//monetizationController.loadBannerAd();
		//monetizationController.loadInterstitialAd();
	}
	
	@Override
	public void create () {
		this.eventDispatcher = new EventDispatcher();
		this.gameWorld = new GameWorld(this);
		//monetizationController.loadBannerAd();
		/** Now have our ads controller setupAds(). */
		//monetizationController.setupAds();
		//monetizationController.loadBannerAd();
	}

	@Override
	public void render () {
		//if(monetizationController.isBannerAdLoaded())monetizationController.showBannerAd();

		//First Calculate Elapsed Time
		gameWorld.update();
		//if(monetizationController.isBannerAdLoaded())monetizationController.showBannerAd();

		//monetizationController.updateVisibility();//used for banner ads to know whether to show
	}

	public void toast(final String t){
		toastInterface.toast(t);
	}

	@Override
	public void dispose () {

	}
}
