package com.araceinspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.araceinspace.TestSubSystem.AndroidsAdsController_Test;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AndroidLauncher extends AndroidApplication implements ToastInterface{

	AndroidAdsController adsController;
	AndroidLauncher me;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		me = this;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new ARaceInSpace(), config);

		/*Initialize and AndroidAdsController, we do it here, because the
		 * AdView in the AndroidAdsController requires access to this */
		adsController = new AndroidAdsController(this);

		/*Create a View and pass it an instance of the core game
		 *initialized with our ads controller.*/
		//View gameView = initializeForView(new ARaceInSpace(adsController), config);
		View gameView = initializeForView(new AndroidsAdsController_Test(adsController, this), config);

		adsController.setupAds();


		/* Setup the layouts we are going to use for our views.
		   We want the gameView layout to match th parent layout. The main android app config.
		 */
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		/* We letAndroidAdsController add itself to the layout.
		 */
		layout = adsController.setupBannerLayout(layout);

		//Now we set the content view for the android app.
		setContentView(layout);




	}

	@Override
	public void onResume() {
		//mAd.resume(this);
		adsController.resume();
		super.onResume();
	}

	@Override
	public void onPause() {
		//mAd.pause(this);
		adsController.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		//mAd.destroy(this);
		adsController.destroy();
		super.onDestroy();
	}

	/**
	 * When google in app billing returns it calls this method on
	 * the actvitiy. This method check if the ad controller will handle it
	 * otherwise sends it to the super class to handle.
	 * @param requestCode
	 * @param resultCode
	 * @param data
     */
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		//give the ads controller a chance to process the activity result first.
		if(!adsController.onActivityResult(requestCode, resultCode, data)){
			//if that doesn't work, we just do it the old fashioned way.
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void toast(final String t) {
		handler.post(new Runnable()
		{

			@Override
			public void run() {
				//System.out.println("toatsing in launcher run");
				Toast.makeText(me, t, Toast.LENGTH_SHORT).show();
//Toast.
			}

		});

	}

}
