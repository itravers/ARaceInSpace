package com.araceinspace.AndroidMonetizationSubSystem;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Isaac Assegai on 9/24/16.
 * Once an app is properly linked to the FireBase console through the AdMob console
 * then the "Notifications" service in FireBase becomes available.
 * A new message can be created in FireBase console which gets
 * sent to all the copies of the game.
 * This class listens for a message being sent, and then routes that
 * message to be shown to the user in a "toast"
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService{

/* Private Methods */

    /**
     * This is the method that is automatically invoked when A message is received through firebase.
     * @param remoteMessage Data about, and the message itself.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MessagingService", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("MessagingService", "Message data payload: " + remoteMessage.getData());
        }

        // If message contains a notification payload, we want to route the message to the main Launcher to be handled.
        if (remoteMessage.getNotification() != null) {
            Log.d("MessagingService", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            //This is how we route the message, create an intent, and broadcast it. We've setup the launcher to listen for this.
            Intent intent = new Intent("ShowToast");
            intent.putExtra("remoteMessage", remoteMessage);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }
}
