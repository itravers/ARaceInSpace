package com.araceinspace.AndroidMonetizationSubSystem;

/**
 * Created by Isaac Assegai on 9/26/16.
 * This class allows the caller to purchase
 * pre-setup items from the google play store.
 * The caller can consume cosumable items,
 * and store unconsumable and subscriptions.
 */
public class PlayPurchaseManager {

/* Static Variables */
    public enum PURCHASE_TYPE {CONSUMABLE, NON_CONSUMABLE, SUBSCRIPTION};

/* Field Variables */



/* Constructors */


/* Private Methods */


/* Public Methods */


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


    /* Private Methods */


    /* Public Methods */
    }

}
