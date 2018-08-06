package com.paddypal.azir.paddypal.notifications;

import android.app.Notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.paddypal.azir.paddypal.R;
import com.paddypal.azir.paddypal.getGuidance.GuidanceActivity;

import static android.media.RingtoneManager.getDefaultUri;


public class MyFirebaseMessagingService extends FirebaseMessagingService{
    int requestID = (int) System.currentTimeMillis();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Message", "From: " + remoteMessage.getFrom());
        Log.d("Message", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Intent notificationIntent = new Intent(getApplicationContext(), GuidanceActivity.class);
        Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,requestID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Notification notification=new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setStyle(new NotificationCompat.BigTextStyle())
                .addAction(R.drawable.ic_launcher_foreground,"View",pendingIntent)
                .addAction(0,"Remind",pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(123,notification);
    }
}
