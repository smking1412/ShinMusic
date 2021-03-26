package com.shinmusic.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.shinmusic.Model.LocalSongs;

import java.util.ArrayList;

import static com.shinmusic.Activity.PlayLocalActivity.localSongsArrayList;

public class MusicPlayingService extends Service {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<LocalSongs> listLocalSong = new ArrayList<>();
//    private Uri uri;
//    private int position = -1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("pmtan", "onBind: ");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public  class MyBinder extends Binder{
        public MusicPlayingService getService(){
            return MusicPlayingService.this;
        }
    }

}
