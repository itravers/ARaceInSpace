package com.araceinspace;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.araceinspace.TestSubSystem.AndroidsAdsController_Test;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AndroidLauncher extends AndroidApplication {

	AndroidAdsController adsController;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new ARaceInSpace(), config);

		/*Initialize and AndroidAdsController, we do it here, because the
		 * AdView in the AndroidAdsController requires access to this */
		adsController = new AndroidAdsController(this);

		/*Create a View and pass it an instance of the core game
		 *initialized with our ads controller.*/
		//View gameView = initializeForView(new ARaceInSpace(adsController), config);
		View gameView = initializeForView(new AndroidsAdsController_Test(adsController), config);

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

}
