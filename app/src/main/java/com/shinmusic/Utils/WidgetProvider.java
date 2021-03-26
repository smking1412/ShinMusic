package com.shinmusic.Utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.content.ContextCompat;

import com.shinmusic.Activity.MainActivity;
import com.shinmusic.R;
import com.shinmusic.Service.MusicPlayingService;

public class WidgetProvider extends AppWidgetProvider {


    private static final String TAG = "Music Widget";
    public static final String ACTION_PLAY_PAUSE = "com.smartpocket.musicwidget.play_pause";
    public static final String ACTION_STOP = "act_play";
    public static final String ACTION_NEXT = "act_next";
    public static final String ACTION_PREVIOUS = "act_prev";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        associateIntents(context);

    }


    public static RemoteViews getRemoteViews(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_main);
        // For Play/Pause button
        PendingIntent pendingIntentStart = getPendingIntent(context, ACTION_PLAY_PAUSE);
        remoteViews.setOnClickPendingIntent(R.id.button_play_pause, pendingIntentStart);

        // For Stop button
        PendingIntent pendingIntentStop = getPendingIntent(context, ACTION_STOP);
        remoteViews.setOnClickPendingIntent(R.id.button_stop, pendingIntentStop);

        // For Previous button
        PendingIntent pendingIntentPrevious = getPendingIntent(context, ACTION_PREVIOUS);
        remoteViews.setOnClickPendingIntent(R.id.button_prev, pendingIntentPrevious);

        // For Next button
        PendingIntent pendingIntentNext = getPendingIntent(context, ACTION_NEXT);
        remoteViews.setOnClickPendingIntent(R.id.button_next, pendingIntentNext);

        // For Song List activity
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendIntentSongList = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.layout_header, pendIntentSongList);


        return remoteViews;
    }


    private void associateIntents(Context context) {

        try {
            RemoteViews remoteViews = getRemoteViews(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(thisWidget, remoteViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context,WidgetProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Intent oService = new Intent(context, MusicPlayingService.class);
        context.stopService(oService);
        Log.d(TAG, "Deleting widget");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "Widget received action: " + action);

        if ((action.equals(ACTION_PLAY_PAUSE)
                || action.equals(ACTION_NEXT)
                || action.equals(ACTION_STOP)
                || action.equals(ACTION_PREVIOUS)
              )) {
            Intent serviceIntent = new Intent(context, MusicPlayingService.class);
            serviceIntent.setAction(action);
            ContextCompat.startForegroundService(context, serviceIntent);
        } else {
            super.onReceive(context, intent);
        }
    }
}


