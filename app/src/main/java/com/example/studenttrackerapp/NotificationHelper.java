package com.example.studenttrackerapp;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.content.Context;
import android.content.ContextWrapper;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


class NotificationHelper extends ContextWrapper {
    private NotificationManager notifManager;
    public static final String CHANNEL_ONE_ID = "com.example.studenttrackerapp.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";


    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }


    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(notificationChannel);

    }


    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getNotification(String title, String body) {
        HomePage homePage = new HomePage();
        return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_stat_st)
//                .addAction(R.drawable.ic_add_circle, "CheckIn", prevPendingIntent)
                .setAutoCancel(true);
    }



    public void notify(Notification.Builder notification) {
        getManager().notify(101, notification.build());
    }


    private NotificationManager getManager() {
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }
}