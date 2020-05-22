package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

public class MyBroadcastReceiver extends BroadcastReceiver {
    int notification_id = 001;
    private final static String TAG_ALARM_TRIGGER_BROADCAST = "ALARM_TRIGGER_BROADCAST";
    MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context);
      //  RingtoneUtil.getInstance(context).play();

        String alarmType = intent.getStringExtra(QuaratineActivity.ALARM_TYPE);

        String alarmDescription = intent.getStringExtra(QuaratineActivity.ALARM_DESCRIPTION);
        mp=  MediaPlayer.create(context, R.raw.alert);
        mp.start();
        Log.d(TAG_ALARM_TRIGGER_BROADCAST, alarmDescription);

        Toast.makeText(context, alarmDescription, Toast.LENGTH_LONG).show();
    }

    protected void sendNotification(Context context) {

        int mNotificationId = 001;
        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.card)
                        .setContentTitle("Qartime your self")
                        .setContentText("Please do it upload your picture")
                        .setOngoing(true);

        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification(StopScript.class in this case)

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(contentIntent);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Builds the notification and issues it.
        mNotificationManager.notify(mNotificationId, mBuilder.build());


    }
}
