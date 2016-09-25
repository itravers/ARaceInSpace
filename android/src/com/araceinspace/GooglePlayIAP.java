package com.araceinspace;

//import com.araceinspace.MonetizationSubSystem.util.IabHelper;
import android.content.Intent;
import android.util.Log;

import com.araceinspace.util.IabHelper;
import com.araceinspace.util.IabResult;
import com.araceinspace.util.Inventory;
import com.araceinspace.util.Purchase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;

/**
 * Created by Isaac Assegai on 9/24/16.
 */
public class GooglePlayIAP {
    private AndroidLauncher app;

    private AndroidRewardAd me;

    public String testsku = "test_product_0001";
    //public String testsku = "android.test.purchased";

    IabHelper mHelper;
    //String base64EncodedPublicKey = "android.test.purchased";
    String base64EncodedPublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/toWPHc37g+x3HMdK70ikTbt/7ylEC5MI+BWnoqj/Wr2Dry68xU016RbWtvJ2eGXtEl3AXnGYnwhmrt6Xmmb1BcK9o02nTZzimR7EY7EXxvOpCFBjDC2biADYWQS2NE/LNPH2brc7tadwO+Tx/FyU2FRBpC58fUveNXQcGXtY8mxp7ocesDQEiTEYc4HiLAetifTsEEtytJAc6MJ349BSLJBJH0zIwxn7pFrWPjsgXt4y2+szOPo+0E/UaNAbjWgiaj35JgsLKCJYiKdSgic7cJn4q8j1QqD5dzNTaVXrZYZiYit6ctuHFRmC+e6cqRGvbP4C1eBJVewuW62XyyvwIDAQAB";

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(testsku)) {
                consumeItem(result, purchase);
               // buyButton.setEnabled(false);
            }

        }
    };

    public void consumeItem(IabResult result,
                            Purchase purchase){
        Gdx.app.log("GameAds","consuming item: " +
                result + " : " + purchase);
        mHelper.queryInventoryAsync(mGotInventoryListener);
    }

    public GooglePlayIAP(AndroidLauncher app){
        this.app = app;
        mHelper = new IabHelper(app, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                              // Log.d("GameAds", "In-app Billing setup failed: " +
                                              //         result);
                                               Gdx.app.log("GameAds", "In-app Billing setup failed: " +
                                               result);
                                           } else {
                                               Log.d("GameAds", "In-app Billing is set up OK");
                                               Gdx.app.log("GameAds","In-app Billing is set up OK: " +
                                                       result);
                                               mHelper.enableDebugLogging(true, "GameAds");
                                               mHelper.queryInventoryAsync(mGotInventoryListener);

                                           }
                                       }
                                   });
       // IabHelper.
    }

    public void consumeOwnedItems(){
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

    }

    // Listener that's called when we finish querying the items and
    // subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            Log.d("GameAds", "Query inventory finished.");
            if (result.isFailure()) {
                Log.d("GameAds", "Failed to query inventory: " + result + " : " + inventory);
                return;
            }

            Log.d("GameAds", "Query inventory was successful.");

                /*
                 * Check for items we own. Notice that for each purchase, we check
                 * the developer payload to see if it's correct! See
                 * verifyDeveloperPayload().
                 */

            // // Check for gas delivery -- if we own gas, we should fill up the
            // tank immediately
            Purchase purchase = inventory.getPurchase(testsku);
            if (purchase != null && verifyDeveloperPayload(purchase)) {
                Log.d("GameAds", "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(testsku),
                        mConsumeFinishedListener);
                return;
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        Log.d("GameAds", "we have consumed the purchase: " + purchase + " : " + result);
                    } else {
                        // handle error
                        Log.d("GameAds", "we have NOT NOT NOT consumed the purchase: " + purchase + " : " + result);
                    }
                }
            };

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
       return mHelper.handleActivityResult(requestCode, resultCode, data);
    }


    public void destroy() {
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    public void buyItem(){
        mHelper.launchPurchaseFlow(app, testsku, 10001, mPurchaseFinishedListener, "mypurchasetoken");

    }

}
