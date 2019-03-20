package com.example.may.myproject.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.example.may.myproject.R;

public class NotificationHelper extends ContextWrapper {

    private static final String OYF_CHANNEL_ID = "com.example.may.myproject";
    private static final String OYF_CHANEL_NAME = "OYF";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        createchannel();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createchannel() {

        NotificationChannel oyfchannel = new NotificationChannel(OYF_CHANNEL_ID,
                OYF_CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        oyfchannel.enableLights(false);
        oyfchannel.enableVibration(true);
        oyfchannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(oyfchannel);

    }

    public NotificationManager getManager() {
        if(manager == null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public android.app.Notification.Builder getOYFChannelNotification(String title, String body, PendingIntent contentIntent, Uri soundUri){
        return new android.app.Notification.Builder(getApplicationContext(),OYF_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
}
