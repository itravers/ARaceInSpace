package com.araceinspace.AndroidMonetizationSubSystem;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.araceinspace.AndroidLauncher;
import com.araceinspace.AndroidMonetizationSubSystem.util.IabHelper;
import com.araceinspace.AndroidMonetizationSubSystem.util.IabResult;
import com.araceinspace.AndroidMonetizationSubSystem.util.Inventory;
import com.araceinspace.AndroidMonetizationSubSystem.util.Purchase;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Isaac Assegai on 9/26/16.
 * This class allows the caller to purchase
 * pre-setup items from the google play store.
 * The caller can consume cosumable items,
 * and store unconsumable and subscriptions.
 */
public class PlayPurchaseManager {

/* Static Variables */

    /**
     * There are several different types of purchase that need to be
     * handled in slightly different ways. For instance, a CONSUMABLE and
     * NON_CONSUMABLE are treated by the play store as the same thing, but
     * we treat it differently because we call consume on a CONSUMABLE as
     * soon as we obtain it, whereas, we should never call consume on a
     * non-consumable, or it will disappear from the play inventory
     * when it should not.
     */
    public enum PURCHASE_TYPE {CONSUMABLE, NON_CONSUMABLE, SUBSCRIPTION};

    /**
     * Key issued by google play that they use to make sure the app is legit.
     * This should be kinda obscured in the apk somehow.
     * we'll do that in the class constructor.
     */
    static String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/toWPHc37g+x3HMdK70ikTbt/7ylEC5MI+BWnoqj/Wr2Dry68xU016RbWtvJ2eGXtEl3AXnGYnwhmrt6Xmmb1BcK9o02nTZzimR7EY7EXxvOpCFBjDC2biADYWQS2NE/LNPH2brc7tadwO+Tx/FyU2FRBpC58fUveNXQcGXtY8mxp7ocesDQEiTEYc4HiLAetifTsEEtytJAc6MJ349BSLJBJH0zIwxn7pFrWPjsgXt4y2+szOPo+0E/UaNAbjWgiaj35JgsLKCJYiKdSgic7cJn4q8j1QqD5dzNTaVXrZYZiYit6ctuHFRmC+e6cqRGvbP4C1eBJVewuW62XyyvwIDAQAB";


/* Field Variables */

    /**
     * A reference to the main libgdx android launcher app.
     * used to run things on the ui thread,
     */
    private AndroidLauncher app;

    /**
     * Used for all the actual contact with google play.
     */
    IabHelper iabHelper;

    /**
     * The Object that implements all the google play callback
     * we rolled this ourselves.
     */
    PurchaseListener purchaseListener;

    /**
     * This is the list of all in-app purchasable items available to the game.
     * When a new item is setup in google play it will need to be manually
     * set up in the defaultItems list as well. We will do this in the
     * setupDefaultItems() method call.
     */
    private HashMap<String, PurchasableItem> defaultItems;

    /**
     * Locally Stores Purchasable items that have not been consumed.
     * Whenever the app starts the local inventory should be updated
     * with all NON_CONSUMABLES and Subscriptions stored in the
     * remote inventory. Any consumables in the remoteInventory
     * should be immediately consumed before they are added
     * to this localInventory
     */
    private HashMap<String, PurchasableItem> localInventory;

    /**
     * When purchase item is called, this will be set to the item being purchased.
     * That way when purchase_callback is called, we can check the developer
     * payload returned from google play to the developer payload stored
     * in this puchasable item. After the check, it is again set to null.
     */
    private PurchasableItem itemUnderPurchase = null;


/* Constructors */

    /**
     * Constructs a new PlayPurchaseManager using the given app.
     * @param app
     */
    public PlayPurchaseManager(AndroidLauncher app){
        this.app = app;
    }


/* Private Methods */

    /**
     * Sets the IabHelper, which is the object that
     * actually mediates all communications with google play.
     */
    private void setupIabHelper(){
        iabHelper = new IabHelper(app, base64EncodedPublicKey);
        purchaseListener = new PurchaseListener();
        iabHelper.startSetup(purchaseListener);
    }

    /**
     * Sets up the default items list. The items in this
     * list must exactly match the items set up in the
     * google play dev console.
     */
    private void setupDefaultItems(){
        Gdx.app.log("PlayPurchaseManager", "setupDefaultItems() called");
        //initiate the hashmap we store our default items in.
        defaultItems = new HashMap<String, PurchasableItem>();

        //Create ALL the PurchasableItems that will be available for the app.
        PurchasableItem item1 = new PurchasableItem("test_product_0001", PURCHASE_TYPE.CONSUMABLE, "test_product_0001_developer_payload");
        PurchasableItem item2 = new PurchasableItem("test_product_0002", PURCHASE_TYPE.CONSUMABLE, "test_product_0002_developer_payload");

        //Add all the items created to the defaultItems map using their skus as keys.
        defaultItems.put(item1.getSku(), item1);
        defaultItems.put(item2.getSku(), item2);
    }

    /**
     * A synchronously Queries the remote inventory for any stored items.
     * The callback onQueryInventoryFinished is called with the inventory result
     * in THAT method, we will loop through the result, consume consumables
     * and store any non consumables in the inventory.
     */
    private void setupLocalInventory(){
        Gdx.app.log("PlayPurchaseManager", "setupLocalInventory() called");
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iabHelper.queryInventoryAsync(purchaseListener);
            }
        });
    }

    /**
     * Callback that gets called when a purchase was sucessful.
     * We check and make sure the developer payload returned
     * from google play is the same developer payload that
     * we wanted to purchase.
     * @param p The purchase from google play, their format, not ours.
     */
    public void purchaseItem_callback(Purchase p){
        //Check to make sure itemUnderPurchase is set correctly, this should never return i believe.
        if(itemUnderPurchase == null){
            Gdx.app.error("PlayPurchaseManager", "purchaseItem_callback() called, but itemUnderPurchase == null, SHOULDNT HAPPEN");
        }else{
            /* We want to check the developer payload to make sure it matches what we sent
               to be purchased. */
            if(!itemUnderPurchase.getDeveloperPayload().equals(p.getDeveloperPayload())){
                //Developer Payload doesn't match, possible xss attack
                Gdx.app.error("PlayPurchaseManager", "purchaseItem_callback() error, Developer payloads don't match : " +
                        p.getDeveloperPayload() + " vs " + itemUnderPurchase.getDeveloperPayload());
            }else{
                Gdx.app.log("PlayPurchaseManager", "purchaseItem_callback() called successfully");
                /* If the Item bought is consumable, we want to consume it immediately
                   else we want to store it locally. */
                if(itemUnderPurchase.type == PURCHASE_TYPE.CONSUMABLE){
                    iabHelper.consumeAsync(p, purchaseListener);
                }else{
                    //Create a PurchasableItem from the returned purchase, and store it in our local inventory.
                    PurchasableItem item = new PurchasableItem(p.getSku(), itemUnderPurchase.getType(), itemUnderPurchase.getDeveloperPayload());
                    localInventory.put(item.getSku(), item);
                }
                //then we want to set itemUnderPurchase back to null, it should only be non-null when a purchase is happening.
                itemUnderPurchase = null;
            }
        }
    }


/* Public Methods */

    /**
     * Sets up the PlayPurchaseManager to be usable.
     */
    public void setup(){
        Gdx.app.log("PlayPurchaseManager", "setup() called");
        setupIabHelper(); //setup the IabHelper, that actually talks to google play
        setupDefaultItems(); //setup the default items map
        setupLocalInventory(); //setup the localInventory map
    }

    /**
     * Start to initiate a purchase from google play.
     * The item being purchased must be set up in google
     * play and in the defaultItems map.
     * @param item The item we want to purchase. Our format, not theirs.
     */
    public void purchaseItem(PurchasableItem item){
        itemUnderPurchase = item;
        iabHelper.launchPurchaseFlow(app, item.getSku(), 10001, purchaseListener, item.getDeveloperPayload());
    }

    /**
     * Locally Consumes a PurchasableItem. This method must only be called from
     * the call back consume method. When the play store shows an item as consumed
     * then, and only then will we consume it locally.
     * Consuming Locally means that the item will be applied towards the player.
     * @param item
     */
    public void consumeItemLocally(PurchasableItem item){
        Gdx.app.log("PlayPurchaseManager", "consumeItemLocally() called : " + item.toString());
        //we'll end up sending this via the event dispatcher, but for now we'll show a toast.
        Intent intent = new Intent("ShowToast");
        intent.putExtra("message", "Consumed " + item.getSku());
        LocalBroadcastManager.getInstance(app).sendBroadcast(intent);
    }


/* Private Classes */

    /**
     * A PurchasableItem represents all the
     * data needed to use a google play
     * iap item.
     */
    private class PurchasableItem{

    /* Field Variables */
        /**
         * Purchasable Items are refered to by their sku's.
         */
        private String sku;

        /**
         * The type of this purchase
         * CONSUMABLE, NON_CONSUMABLE, SUBSCRIPTION
         */
        private PURCHASE_TYPE type;

        /**
         * The developerPayload string gets passed to google play
         * in the Purchase class. This payload is sent back
         * in several callbacks, where we verfiy it matches
         * the developerPayload that we sent out. This
         * seems to protect against several layers of xss attacks.
         */
        private String developerPayload;


    /* Constructors */

        /**
         * Constructs a new PurchasableItem with the given sku, type and developerPayload.
         * @param sku - The sku of the item, originally taken from Play Dev Site
         * @param type - We define this type based on the google play type.
         * @param developerPayload - The payload we verify this item was made by us with.
         */
        public PurchasableItem(String sku, PURCHASE_TYPE type, String developerPayload){
            this.sku = sku;
            this.type = type;
            this.developerPayload = developerPayload;
        }


    /* Private Methods */


    /* Public Methods */

        /**
         * Returns the Items SKU to the caller.
         * @return THE SKU
         */
        public String getSku(){
            return sku;
        }

        /**
         * Returns the Type to the caller.
         * @return The PURCHASE_TYPE
         */
        public PURCHASE_TYPE getType(){
            return type;
        }

        /**
         * Returns the Developer Payload
         * to the caller to be checked.
         * @return The Developer Payload String
         */
        public String getDeveloperPayload(){
            return developerPayload;
        }
    }

    /**
     * The PurchaseListener is where we implement all our callback
     * we receive from the google play servers. Whenever we query google
     * play servers, we need to implement the matching listener here
     * to complete the operation.
     */
    private class PurchaseListener implements IabHelper.QueryInventoryFinishedListener,
                                              IabHelper.OnConsumeFinishedListener,
                                              IabHelper.OnIabSetupFinishedListener,
                                              IabHelper.OnIabPurchaseFinishedListener{

    /* Private Methods */
        /**
         * This is where google play returns a call-back to us when we query inventory.
         * We need to loop through all available items in defaultItems and only process
         * those items which have a sku matching a defaultItem.
         * For those we process, we need to check the type. If the type is non_consumable
         * or a subscription, we need to store it in local inventory.
         * If it is a consumable item, we need to immediately consume it.
         * @param result The result of the operation.
         * @param inv The inventory.
         */
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            //First check to see if the call failed.
            if (result.isFailure()) { //The call did fail.
                Gdx.app.error("PlayPurchaseManager.Listener", "queryInventory failed: " + IabHelper.getResponseDesc(result.getResponse()));
            }else{ //The Call didn't fail.
                Gdx.app.log("PlayPurchaseManager.Listener", "queryInventory suceeded, processing: " + inv.toString());
                //Iterate through the defaultItems, see if the inv has any matching items
                Iterator it = defaultItems.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    String defaultSKU = (String)pair.getKey();
                    PURCHASE_TYPE defaultType = ((PurchasableItem)pair.getValue()).getType();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    it.remove(); // avoids a ConcurrentModificationException

                    //Look for an item in inv which has a sku that matches the sku of a default item
                    if(inv.hasPurchase(defaultSKU)){
                    /* Found one, this is a valid in game item. We need to either
                       add it to the localInventory if it is non_consumable, or a
                       subscription. If it is consumable, then we need to consume it right away.*/

                        //Get the matched purchase from the returned remote inventory
                        Purchase purchase = inv.getPurchase(defaultSKU);

                        if(defaultType == PURCHASE_TYPE.CONSUMABLE){
                            //consume this item immediately
                            iabHelper.consumeAsync(purchase, this);
                        }else{//NON CONSUMABLE, and SUBSCRIPTIONS
                            //Create a PurchasableItem from the returned purchase, and store it in our local inventory.
                            PurchasableItem item = new PurchasableItem(purchase.getSku(), defaultType, purchase.getDeveloperPayload());
                            localInventory.put(item.getSku(), item);
                        }
                    }
                }
            }
        }

        /**
         * We have told google play to consume a specific item and it has called us back here.
         * This should only have been ever called on an item already marked as consumable
         * So we can just locally consume the item now.
         * @param purchase The purchase that was (or was to be) consumed.
         * @param result The result of the consumption operation.
         */
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            //First check to see if the call failed.
            if(result.isFailure()){ //The Called Failed.
                Gdx.app.error("PlayPurchaseManager.Listener", "onConsumeFinished failed: " + IabHelper.getResponseDesc(result.getResponse()));
            }else{
                Gdx.app.log("PlayPurchaseManager.Listener", "onConsumeFinished succeeded, item is consumed in play store: " + purchase.toString());
                PurchasableItem item = new PurchasableItem(purchase.getSku(), PURCHASE_TYPE.CONSUMABLE, purchase.getDeveloperPayload());
                consumeItemLocally(item);
            }
        }

        /**
         * Callback to google plays IabHelper.startSetup().
         * After setup is successful we want to query the inventory.
         * @param result The result of the setup process.
         */
        @Override
        public void onIabSetupFinished(IabResult result) {
            //first check to see if the call failed.
            if(result.isFailure()){//The call did fail
                Gdx.app.error("PlayPurchaseManager.Listener", "onIabSetupFinished() failed: " + IabHelper.getResponseDesc(result.getResponse()));
            }else{//the call didn't fail
                setupLocalInventory();
            }
        }

        /**
         * Called from google plays IabHelper.launchPurchaseFlow.
         * We need to check for errors, and if there is none
         * we will invoke the PlayPurchaseMangers purchase callback function.
         * @param result The result of the purchase.
         * @param p The purchase information (null if purchase failed)
         */
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase p) {
            //first check to see if the call failed
            if(result.isFailure()) {//the call did fail
                Gdx.app.error("PlayPurchaseManager.Listener", "onIabPurchaseFinished() failed: " + IabHelper.getResponseDesc(result.getResponse()));
            }else {//The call didn't fail.
                //the purchase was successful, we already wrote a handler for it, lets call it.
                purchaseItem_callback(p);
            }
        }
    }
}