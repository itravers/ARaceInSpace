package com.araceinspace.AndroidMonetizationSubSystem;


import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.araceinspace.AndroidLauncher;
import com.badlogic.gdx.Gdx;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Isaac Assegai on 9/24/16.
 * Receives and processes notification
 * messages coming from google firebase.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService{

    AndroidLauncher app;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        //Gdx.app.
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("MessagingService", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("MessagingService", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("MessagingService", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //((AndroidLauncher)this.getApplicationContext()).toast("toast test: "+remoteMessage.getNotification().getBody());
           // this.getApplicationInfo().
           // app.toast(remoteMessage.getNotification().getBody());
            Intent intent = new Intent("ShowToast");
            intent.putExtra("remoteMessage", remoteMessage);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            Log.d("MessageService", "Sent Intent in broadcast: " + intent);
            //sendBroadcast(intent);

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
