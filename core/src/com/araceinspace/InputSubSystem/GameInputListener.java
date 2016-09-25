package com.araceinspace.InputSubSystem;

import com.araceinspace.TestSubSystem.AndroidsMonetizationController_Test;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Isaac Assegai on 9/22/16.
 */
public class GameInputListener implements GestureDetector.GestureListener{
    //for testing
    AndroidsMonetizationController_Test parent;

    public GameInputListener(AndroidsMonetizationController_Test parent){
        this.parent = parent;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        System.out.println("game ads fling x:y" + velocityX + ":" + velocityY);
        if(velocityX >= 1){
            System.out.println("game ads fling buy item");
            //parent.monetizationController.showBannerAd();
            //parent.monetizationController.showInterstitialAd();
            parent.monetizationController.buyItem();
        }else if(velocityX <= -1){
            System.out.println("game ads show toast");
            //parent.monetizationController.hideBannerAd();
            //parent.monetizationController.loadInterstitialAd();
           // parent.monetizationController.loadRewardAd();
           // parent.monetizationController.consumeOwnedItems();
           // parent.showToast = !parent.showToast;
            parent.toast("test toast here");
            Gdx.app.log("GameAds","fling but do nothing");
        }
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
