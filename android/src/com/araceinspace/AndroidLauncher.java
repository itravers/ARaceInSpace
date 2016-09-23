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
	private RewardedVideoAd mAd;

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

		mAd = MobileAds.getRewardedVideoAdInstance(this);
		mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
			@Override
			public void onRewarded(RewardItem reward) {
				Gdx.app.log("Game Ads", "onReward(): currentcy: " + reward.getType() + " amount: " +
						reward.getAmount());
			}

			@Override
			public void onRewardedVideoAdLeftApplication() {
				Gdx.app.log("Game Ads", "onRewardedVideoAdLeftApplication: ");
			}

			@Override
			public void onRewardedVideoAdClosed() {
				//Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
				Gdx.app.log("Game Ads", "onRewardedVideoAdLeftApplication: ");
			}

			@Override
			public void onRewardedVideoAdFailedToLoad(int errorCode) {
				//Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
				Gdx.app.log("Game Ads", "onRewardedVideoAdLeftApplication: ");
			}

			@Override
			public void onRewardedVideoAdLoaded() {
				//Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
				Gdx.app.log("Game Ads", "onRewardedVideoAdLeftApplication: ");
			}

			@Override
			public void onRewardedVideoAdOpened() {
				//Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
				Gdx.app.log("Game Ads", "onRewardedVideoAdLeftApplication: ");
			}

			@Override
			public void onRewardedVideoStarted() {
				//Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
				Gdx.app.log("Game Ads", "onRewardedVideoAdLeftApplication: ");
			}

		});

		loadRewardedVideoAd();

	}

	private void loadRewardedVideoAd() {
		mAd.loadAd("ca-app-pub-5553172650479270/6900797543", new AdRequest.Builder().build());
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
