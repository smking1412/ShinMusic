package com.shinmusic.Utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {
    public static final String CHANNEL_ID_1 = "channel1";
    public static final String CHANNEL_ID_2 = "channel2";
    public static final String ACTION_PREVIOUS = "act_prev";
    public static final String ACTION_NEXT = "act_next";
    public static final String ACTION_PLAY = "act_play";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_1,"Channel(1)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Chanel1 Desc...");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID_2,"Channel(2)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Chanel2 Desc...");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);

        }
    }
}
