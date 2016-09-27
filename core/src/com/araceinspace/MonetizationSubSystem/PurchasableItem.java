package com.araceinspace.MonetizationSubSystem;

/**
 * A PurchasableItem represents all the
 * data needed to use a google play
 * iap item.
 */
public class PurchasableItem{

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