package com.shinmusic.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.shinmusic.Activity.PlayLocalActivity;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.Utils.PlaySongListener;

import java.util.ArrayList;

import static com.shinmusic.Activity.PlayLocalActivity.localSongsArrayList;

public class MusicPlayingService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<LocalSongs> listLocalSong = new ArrayList<>();
    private Uri uri;
    private int position = -1;
    PlaySongListener mPlaySongListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int servicePosition = intent.getIntExtra("servicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");
        if (servicePosition != -1) {
            playMedia(servicePosition);
        }
        if (actionName != null) {
            switch (actionName) {
                case "playPause":
                    Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
                    if (mPlaySongListener != null){
                        mPlaySongListener.playPauseClicked();
                    }
                    break;
                case "next":
                    Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
                    if (mPlaySongListener != null){
                        mPlaySongListener.nextBtnClicked();
                    }
                    break;
                case "previous":
                    Toast.makeText(this, "previous", Toast.LENGTH_SHORT).show();
                    if (mPlaySongListener != null){
                        mPlaySongListener.prevBtnClicked();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMedia(int pos) {
        listLocalSong = localSongsArrayList;
        position = pos;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (listLocalSong != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    public void createMediaPlayer(int pos) {
        uri = Uri.parse(listLocalSong.get(pos).getPath());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
    }

    public void OnCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlaySongListener != null) {
            mPlaySongListener.nextBtnClicked();
        }
        createMediaPlayer(position);
        mediaPlayer.start();
        OnCompleted();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("pmtan", "onBind: ");
        return mBinder;
    }

    public class MyBinder extends Binder {
        public MusicPlayingService getService() {
            return MusicPlayingService.this;
        }
    }

    public void start() {
        mediaPlayer.start();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void setCallBack(PlaySongListener playSongListener){
        this.mPlaySongListener = playSongListener;
    }
}
