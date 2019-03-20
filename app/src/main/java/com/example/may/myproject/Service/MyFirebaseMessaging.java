package com.example.may.myproject.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
//import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.example.may.myproject.Common.Common;
import com.example.may.myproject.Helper.NotificationHelper;
import com.example.may.myproject.MainActivity;
import com.example.may.myproject.OrderStatus;
import com.example.may.myproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static android.support.v7.app.NotificationCompat.*;

public class MyFirebaseMessaging extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("MSG","remote="+remoteMessage);
      // Toast.makeText(MyFirebaseMessaging.this,"myfirebasemessing 0",Toast.LENGTH_LONG).show();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           // Toast.makeText(MyFirebaseMessaging.this,"myfirebasemessing 1",Toast.LENGTH_LONG).show();
            sendNotificationAPI26(remoteMessage);
        }
        else {
           // Toast.makeText(MyFirebaseMessaging.this,"myfirebasemessing 2",Toast.LENGTH_LONG).show();
            sendNotification(remoteMessage);

        }
    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {
        Toast.makeText(MyFirebaseMessaging.this,"myFirebasemessaging 3",Toast.LENGTH_LONG).show();
    RemoteMessage.Notification notification = remoteMessage.getNotification();
    String title  = notification.getTitle();
    String content = notification.getBody();

    Intent intent = new Intent(this, OrderStatus.class);
    intent.putExtra(Common.PHONE_TEXT, Common.currentUser.getPhone());
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this ,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper helper = new NotificationHelper(this);
        Notification.Builder builder = helper.getOYFChannelNotification(title,content,pendingIntent,defaultSoundUri);
        helper.getManager().notify(new Random().nextInt(),builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {
//        Toast.makeText(MyFirebaseMessaging.this,"myFirebasemessaging 4",Toast.LENGTH_LONG).show();
        Log.i("MSG","Notification send");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this ,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Notification.Builder builder = new Notification.Builder(this,new NotificationManager.)

           android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_round)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0,builder.build());
    }
}
