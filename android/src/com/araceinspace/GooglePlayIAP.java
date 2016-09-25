package com.araceinspace;

//import com.araceinspace.MonetizationSubSystem.util.IabHelper;
import com.araceinspace.util.IabHelper;
import com.badlogic.gdx.backends.android.AndroidApplication;

/**
 * Created by Isaac Assegai on 9/24/16.
 */
public class GooglePlayIAP {
    private AndroidApplication app;

    private AndroidRewardAd me;

    IabHelper mHelper;
    String base64EncodedPublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/toWPHc37g+x3HMdK70ikTbt/7ylEC5MI+BWnoqj/Wr2Dry68xU016RbWtvJ2eGXtEl3AXnGYnwhmrt6Xmmb1BcK9o02nTZzimR7EY7EXxvOpCFBjDC2biADYWQS2NE/LNPH2brc7tadwO+Tx/FyU2FRBpC58fUveNXQcGXtY8mxp7ocesDQEiTEYc4HiLAetifTsEEtytJAc6MJ349BSLJBJH0zIwxn7pFrWPjsgXt4y2+szOPo+0E/UaNAbjWgiaj35JgsLKCJYiKdSgic7cJn4q8j1QqD5dzNTaVXrZYZiYit6ctuHFRmC+e6cqRGvbP4C1eBJVewuW62XyyvwIDAQAB";

    public GooglePlayIAP(AndroidApplication app){
        this.app = app;
        mHelper = new IabHelper(app.getContext(), base64EncodedPublicKey);
    }


}
