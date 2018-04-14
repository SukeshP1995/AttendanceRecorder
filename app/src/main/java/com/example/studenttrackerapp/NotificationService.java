package com.example.studenttrackerapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationService extends Service {
    NotificationHelper notificationHelper;
    Handler mHandler;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        notificationHelper = new NotificationHelper(this);
        super.onCreate();
        mHandler = new Handler();
        this.mHandler.post(m_Runnable);
        Toast.makeText(this,"Service created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service destroyed", Toast.LENGTH_LONG).show();
    }

    private final Runnable m_Runnable = new Runnable()
    {
        boolean flag = true;

        public void run()

        {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            Date now = calendar.getTime();
            SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            final String currentTime = date.format(now);
            System.out.println(currentTime);
            if (currentTime.compareTo("08:55:00")>0 && currentTime.compareTo("08:56:00")<0){
                if (flag){
                    postNotification();
                    flag = false;
                }
            }
            else if (currentTime.compareTo("09:05:00")>0 && currentTime.compareTo("09:06:00")<0){
                if (flag){
                    postNotification();
                    flag = false;
                }
            }
            else if (currentTime.compareTo("10:55:00")>0 && currentTime.compareTo("10:56:00")<0){
                if (flag){
                    postNotification();
                    flag = false;
                }
            }
            if (currentTime.compareTo("11:05:00")>0 && currentTime.compareTo("11:06:00")<0){
                if (flag){
                    postNotification();
                    flag = false;
                }
            }
            else if (currentTime.compareTo("13:55:00")>0 && currentTime.compareTo("13:56:00")<0){
                if (flag){
                    postNotification();
                    flag = false;
                }
            }
            else if (currentTime.compareTo("14:05:00")>0 && currentTime.compareTo("14:06:00")<0){
                if (flag){
                    postNotification();
                    flag = false;
                }
            }
            else {
                flag = true;
            }
            NotificationService.this.mHandler.postDelayed(m_Runnable, 5000);
        }
    };
    @Override
    public void onStart(Intent intent, int startId) {
        super.onCreate();
        Toast.makeText(this,"Service started", Toast.LENGTH_LONG).show();
    }

    public void postNotification() {
        Notification.Builder notificationBuilder;

        notificationBuilder = notificationHelper.getNotification("Remainder",
                "Its time, please check in");


        if (notificationBuilder != null) {
            notificationHelper.notify(notificationBuilder);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void goToNotificationSettings(String channel) {
        Intent i = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        i.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
        startActivity(i);
    }

}
