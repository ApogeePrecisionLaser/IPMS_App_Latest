package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
      String message= remoteMessage.getNotification().getBody();
      String title=remoteMessage.getNotification().getTitle();
      sendNotification(title,message);
    }

    protected void sendNotification(String title,String message) {

        int mNotificationId = 001;

        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setOngoing(true);


        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification(StopScript.class in this case)

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, QuaratineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(contentIntent);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        // Builds the notification and issues it.
        mNotificationManager.notify(mNotificationId, mBuilder.build());


    }
}
