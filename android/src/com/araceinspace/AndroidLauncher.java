package com.araceinspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.araceinspace.AndroidMonetizationSubSystem.AndroidMonetizationController;
import com.araceinspace.MonetizationSubSystem.DummyController;
import com.araceinspace.MonetizationSubSystem.ToastInterface;
import com.araceinspace.TestSubSystem.MonetizationIntegrationTest;
import com.araceinspace.TestSubSystem.StoreLayoutTest;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Created by Isaac Assegai on 9/17/16.
 * The AndroidLauncher intitializes all the systems
 * needed to get the app running on android.
 */
public class AndroidLauncher extends AndroidApplication implements ToastInterface{

/* Field Variables */

	/**
	 * The MonetizationController that we create and pass to the game.
	 */
	AndroidMonetizationController adsController;
	//DummyController adsController;

	/**
	 * An Alternate reference to this. Used inside Runnables.
	 */
	AndroidLauncher me;

	/**
	 * Used to receive intents
	 */
	LocalBroadcastManager localBroadcastManager;

	/**
	 * Also used to receive intents.
	 */
	BroadcastReceiver broadcastReceiver;

	/**
	 * A reference to the game.
	 */
	//MonetizationIntegrationTest mainGame;
	//StoreLayoutTest mainGame;
	ARaceInSpace mainGame;

/* Protected Methods */

	/**
	 * Called when the OS creates a new game.
	 * @param savedInstanceState Saved data we can use to reset-up the game.
     */
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		System.out.println("onCreate()");
		super.onCreate(savedInstanceState);
		me = this;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		/*Initialize and AndroidMonetizationController, we do it here, because the
		 * AdView in the AndroidMonetizationController requires access to this */
		adsController = new AndroidMonetizationController(this);
		//adsController = new DummyController();

		/*Create a View and pass it an instance of the core game
		 *initialized with our ads controller.*/
		//mainGame = new StoreLayoutTest(adsController, this);
		mainGame = new ARaceInSpace(adsController, this);
		View gameView = initializeForView(mainGame, config);

		//set up the ads controller ads.
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

		//setup the broadcast receiver, so messages can be routed through here
		registerBroadcastReceiver();
	}

/* Private Methods */

	/**
	 * Registers the intents broadcast receiver, and tell what to do with the
	 * intents when they are received.
	 */
	private void registerBroadcastReceiver() {
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				Log.d("MessageService", "AndroidLauncher Received intent: " + intent);
				//RemoteMessage remoteMessage = (RemoteMessage)intent.getExtras().get("remoteMessage");

				if(action.equals("ShowToast")){
					String message = (String)intent.getExtras().get("message");
					toast(message);
				}else if(action.equals("Add1Credit")){
					toast("Adding a credit for watching video");
					mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 1);
				}else if(action.equals("Add10Credits")){
					toast("Adding 10 credits because you purchased them");
					mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 10);
				}else if(action.equals("Add15Credits")){
					toast("Adding 15 credits because you purchased them");
					mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 15);
				}else if(action.equals("Add20Credits")){
					toast("Adding 20 credits because you purchased them");
					mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 20);
				}else if(action.equals("Add30Credits")){
					toast("Adding 30 credits because you purchased them");
					mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 30);
				}else if(action.equals("Add100Credits")){
					toast("Adding 100 credits because you purchased them");
					mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 100);
				}else if(action.equals("Add5000Credits")){
					toast("Adding 5000 credits because you purchased them");
					mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 5000);
				}else if(action.equals("RemoveAds")){
					toast("Removing All Ads");
					//mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 5000);
					//todo put remove ads code here
				}else if(action.equals("UnlockEverything")){
					toast("Unlocking Everything in Game");
					//mainGame.gameWorld.setCoins(mainGame.gameWorld.getCoins() + 5000);
					//todo put unlock everything in game here
				}
			}
		};

		localBroadcastManager = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("ShowToast");
		intentFilter.addAction("Add1Credit");
		intentFilter.addAction("Add10Credits");
		intentFilter.addAction("Add15Credits");
		intentFilter.addAction("Add20Credits");
		intentFilter.addAction("Add30Credits");
		intentFilter.addAction("Add100Credits");
		intentFilter.addAction("Add5000Credits");
		intentFilter.addAction("RemoveAds");
		intentFilter.addAction("UnlockEverything");
		localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
	}


/* Public Methods */

	/**
	 * Called when the OS resumes the game, after being interrupted.
	 */
	@Override
	public void onResume() {
		adsController.resume();
		super.onResume();
	}

	/**
	 * Called by the OS, when it needs to pause the game.
	 */
	@Override
	public void onPause() {
		adsController.pause();
		super.onPause();
	}

	/**
	 * Called by the OS when it wants to destroy the game.
	 */
	@Override
	public void onDestroy() {
		adsController.destroy();
		localBroadcastManager.unregisterReceiver(broadcastReceiver);
		mainGame.dispose();
		super.onDestroy();
	}

	/**
	 * When google in app billing returns it calls this method on
	 * the main. This method check if the ad controller will handle it
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

	/**
	 * When we are told to toast, we'll use androids toast feature.
	 * @param t The string we want to display in the toast.
     */
	public void toast(final String t) {
		handler.post(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(me, t, Toast.LENGTH_SHORT).show();
			}
		});
	}
}