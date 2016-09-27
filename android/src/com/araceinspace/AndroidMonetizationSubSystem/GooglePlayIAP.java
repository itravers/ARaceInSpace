package com.araceinspace.AndroidMonetizationSubSystem;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.araceinspace.AndroidLauncher;
import com.araceinspace.AndroidMonetizationSubSystem.util.IabHelper;
import com.araceinspace.AndroidMonetizationSubSystem.util.IabResult;
import com.araceinspace.AndroidMonetizationSubSystem.util.Inventory;
import com.araceinspace.AndroidMonetizationSubSystem.util.Purchase;
import com.araceinspace.isaac.game.BuildConfig;
import com.badlogic.gdx.Gdx;

/**
 * Created by Isaac Assegai on 9/24/16.
 * The GooglePlayIAP contains all the data and methods
 * needed to contact the google play store in order to
 * purchase, keep track of, and consume in app items.
 * A different IAP manager will be made for each different launcher.
 */
public class GooglePlayIAP {

/* Field Variables */

    /**
     * A reference to the main libgdx android launcher app.
     * used to run things on the ui thread,
     */
    private AndroidLauncher app;

    /**
     * This info will be where we store all our item info.
     */
    //public String testsku = "test_product_0001";
    public String testsku;

    /**
     * Used to contact google play for pretty much everything.
     */
    IabHelper iabHelper;

    /**
     * Set up to listen to any response for the google play service
     * that is initiated by the IabHelper.
     */
    MyIAPListener myIAPListener;

    /**
     * Key issued by google play that they use to make sure the app is legit.
     * This should be kinda obscured in the apk somehow.
     * we'll do that in the class constructor.
     */
    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/toWPHc37g+x3HMdK70ikTbt/7ylEC5MI+BWnoqj/Wr2Dry68xU016RbWtvJ2eGXtEl3AXnGYnwhmrt6Xmmb1BcK9o02nTZzimR7EY7EXxvOpCFBjDC2biADYWQS2NE/LNPH2brc7tadwO+Tx/FyU2FRBpC58fUveNXQcGXtY8mxp7ocesDQEiTEYc4HiLAetifTsEEtytJAc6MJ349BSLJBJH0zIwxn7pFrWPjsgXt4y2+szOPo+0E/UaNAbjWgiaj35JgsLKCJYiKdSgic7cJn4q8j1QqD5dzNTaVXrZYZiYit6ctuHFRmC+e6cqRGvbP4C1eBJVewuW62XyyvwIDAQAB";

/* Constructors */

    /**
     * Construct a new GooglePlayIAP
     * Assigned the app, we wait for setup to initiate anything else.
     * @param app
     */
    public GooglePlayIAP(AndroidLauncher app){
        this.app = app;

        if(BuildConfig.DEBUG){
            Gdx.app.log("GameAds", "iap launched in debug mode.");
            testsku = "android.test.purchased";
        }else{
            Gdx.app.log("GameAds", "iap launched in release mode.");
            testsku = "test_product_0001";
        }
    }

/* Private Methods */

/* Public Methods */

    /**
     * Setups up the GooglePlayIAP to be used.
     */
    public void setup(){
        iabHelper = new IabHelper(app, base64EncodedPublicKey);
        myIAPListener = new MyIAPListener();
        iabHelper.startSetup(myIAPListener);//register the listener to listen for a setupFinished
    }

    /**
     * Check google play for all owned items.
     * This will cause a queryInventory to happen.
     * The listener will them check and make sure all
     * returned items that are consumable are properly
     * consumed.
     */
    public void consumeOwnedItems(){
        Gdx.app.log("GameAds", "consumeOwnedItems() called");
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iabHelper.queryInventoryAsync(myIAPListener);
            }
        });

    }

    /**
     * Here we will check the incoming developer payload in the purchase
     * to make sure that it matches the payload we thing we are talking about.
     * @param purchase
     * @return
     */
    public boolean verifyDeveloperPayload(Purchase purchase){
        return true;
    }

    /**
     * A purchase process returns and calls onActivityResult on the Main AndroidLauncher
     * which gets routed to here. If mhelper.handleActivityResult returns true, then
     * we processed it correctly, if it returns false, then we'll let
     * the calling AndroidLauncher process it.
     * mHelper.handleActivity will call the mPurchaseFinishedLitener if needed.
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
       return iabHelper.handleActivityResult(requestCode, resultCode, data);
    }

    /**
     * destroys the IabHelper
     */
    public void destroy() {
        if (iabHelper != null) iabHelper.dispose();
        iabHelper = null;
    }

    /**
     * use the IabHelper to buy an item.
     */
    public void buyItem(){
        iabHelper.launchPurchaseFlow(app, testsku, 10001, myIAPListener, "mypurchasetoken");

    }

    /**
     * This is where we initiate the actual consumtion of the item in the app,
     * this is called after we have already consumed the item in google play, and only
     * called if that google play consumption worked.
     * @param result The result of consumthing the item in google play.
     * @param purchase The purchase we consumed, will have item data.
     */
    public void consumeItem(IabResult result, Purchase purchase){
        //this is where we wire up the actual item consumtion itself.
        //we will join this to the other parts of the app as needed through the event dispatch system.
        Gdx.app.log("GameAds", "We CONSUME THE ITEM HERE: " + purchase);
        //This is how we route the message, create an intent, and broadcast it. We've setup the launcher to listen for this.
        Intent intent = new Intent("ShowToast");
        intent.putExtra("message", "Consumed " + purchase.getSku());
        LocalBroadcastManager.getInstance(app).sendBroadcast(intent);

    }

/* Private Classes */

    /**
     * Used to aggregate all the IabHelpers finished listeners that we will use.
     */
    private class MyIAPListener implements IabHelper.OnConsumeFinishedListener, IabHelper.QueryInventoryFinishedListener, IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnIabSetupFinishedListener{

    /* Constructors */

        /**
         * Constructor
         */
        public MyIAPListener(){
            //don't need to do anything yet.
        }

    /* Private Methods */


    /* Public Methods */

        /**
         * This means the item will now show as consumed in google play.
         * We can wire this up to other sections of the app where
         * we actually benefit the user for consuming the item.
         * @param purchase The purchase that was (or was to be) consumed.
         * @param result The result of the consumption operation.
         */
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Log.d("GameAds", "onConsumeFinished() called: " + purchase + " : " + result);
                consumeItem(result, purchase);//we actually consume the item.
            } else {
                // handle error
                Log.d("GameAds", "onConsumeFinished() called, but there was an error: " + IabHelper.getResponseDesc(result.getResponse()));
            }
        }

        /**
         * Listener that's called when we finish querying
         * the items andsubscriptions we own
         * @param result The result of the operation.
         * @param inventory The inventory.
         */
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                Log.d("GameAds", "onQueryInventoryFinished() called, but there was an error: " + IabHelper.getResponseDesc(result.getResponse()));
                return;//there was an error, don't do the normal stuff
            }

            Log.d("GameAds", "onQueryInventoryFinished() called: " + result + " : " + inventory);
            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */
//we need to check for all owned items that are consumable and consume them here.
            //check for items that should be consumed right away, and consume them here
            //we don't wire them game to consume them yet, we do that in the callback method onConsumeFinished()

            Purchase purchase = inventory.getPurchase(testsku);
            if (purchase != null && verifyDeveloperPayload(purchase)) {
                Log.d("GameAds", "We have item. Consuming it.");
                iabHelper.consumeAsync(inventory.getPurchase(testsku),
                        /*mConsumeFinishedListener*/this);
                return;
            }
        }

        /**
         * A requested purchase is finished, we can is if it went through
         * or failed, and we can respond accordingly.
         * @param result The result of the purchase.
         * @param purchase
         */
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                    if (result.isFailure()) {
                        Gdx.app.log("GameAd", "purchase failed for some reason: " + IabHelper.getResponseDesc(result.getResponse()));

                        //we already own the item we are trying to buy, google play won't allow you to own more than one of each type of item
                        //so we will consume it.
                        if(result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED){
                            Gdx.app.log("GameAd", "we already own item, so we are going to try to consume it via play store.");
                            //iabHelper.consumeAsync(purchase, this);
                            consumeOwnedItems();
                        }
                    }else if (purchase.getSku().equals(testsku)) { //purchase was a success, we decide if we consume or not here
                        Gdx.app.log("GameAd", "purchase success: " + result + " ::: " + purchase + IabHelper.getResponseDesc(result.getResponse()));
                        //the test item was found, we want to consume it.

                        //i need to consume this here in google play, not just in the app itsef...!!!!!
                        iabHelper.consumeAsync(purchase, this);
                       // consumeItem(result, purchase);
                    }
        }

        @Override
        public void onIabSetupFinished(IabResult result) {
            if(!result.isSuccess()) {
                Gdx.app.log("GameAds", "In-app Billing setup failed: " + IabHelper.getResponseDesc(result.getResponse()));
            }else{
                Log.d("GameAds", "In-app Billing is set up OK");
                Gdx.app.log("GameAds","In-app Billing is set up OK: " + result);
                iabHelper.enableDebugLogging(true, "GameAds-iabHelper");
                iabHelper.queryInventoryAsync(/*mGotInventoryListener*/this);//will get inventory info from play
            }
        }
    }

}
