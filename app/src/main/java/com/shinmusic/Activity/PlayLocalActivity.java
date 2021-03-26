package com.shinmusic.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import com.shinmusic.Adapter.ViewPagerPlayListSongsAdapter;
import com.shinmusic.Fragment.Fragment_Dianhac;
import com.shinmusic.Fragment.Fragment_Loi_Bai_Hat;
import com.shinmusic.Fragment.Fragment_Play_Local_List_Song;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;
import com.shinmusic.Service.MusicPlayingService;
import com.shinmusic.Utils.NotificationReceiver;
import com.shinmusic.Utils.PlaySongListener;
import com.shinmusic.Utils.ShakeDetector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.shinmusic.Fragment.Fragment_Ca_Nhan.localSongsList;
import static com.shinmusic.Utils.ApplicationClass.ACTION_NEXT;
import static com.shinmusic.Utils.ApplicationClass.ACTION_PLAY;
import static com.shinmusic.Utils.ApplicationClass.ACTION_PREVIOUS;
import static com.shinmusic.Utils.ApplicationClass.CHANNEL_ID_2;

public class PlayLocalActivity extends AppCompatActivity
        implements PlaySongListener, ServiceConnection {
    private ImageView btnReturn;
    private TextView songName;
    private TextView singerName;
    private ImageView btnMore;
    private TextView currentTime;
    private TextView totalTime;
    private SeekBar seekBar;
    private ViewPager viewPagerPlay;
    private ImageView btnShuffleSong;
    private ImageView btnPlayPre;
    private ImageView btnStopAndResume;
    private ImageView btnPlayNext;
    private ImageView btnPlayRepeat;

    private Fragment_Dianhac fragment_dianhac;
    private Fragment_Play_Local_List_Song fragment_play_local_list_song;
    private Fragment_Loi_Bai_Hat fragment_loi_bai_hat;
    //private MediaPlayer mediaPlayer;
    public static ArrayList<LocalSongs> localSongsArrayList = new ArrayList<>();
    private ViewPagerPlayListSongsAdapter viewPagerPlayListSongsAdapter;
    private Uri uri;
    private Handler handler = new Handler();
    private MusicPlayingService musicPlayingService;
    MediaSessionCompat mediaSessionCompat;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private int position = 0;
    public static boolean isRepeat = false;
    public static boolean isShuffle = false;

    private Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac2);
        initView();
        getDataFromIntent();
        setMedia();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(),"Nhạc của Shin");
        eventClick();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicPlayingService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        mSensorManager.registerListener(mShakeDetector,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
        playThreadBtn();
        prevThreadBtn();
        nextThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        mSensorManager.unregisterListener(mShakeDetector);
    }

    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btnStopAndResume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    public void playPauseClicked() {
        if (musicPlayingService.isPlaying()) {
            btnStopAndResume.setImageResource(R.drawable.iconplay);
            musicPlayingService.pause();
            seekBar.setMax(musicPlayingService.getDuration() / 1000);
            if (fragment_dianhac.objectAnimator != null) {
                fragment_dianhac.objectAnimator.pause();
            }
            PlayLocalActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicPlayingService != null) {
                        int mCurrentTime = musicPlayingService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentTime);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                        currentTime.setText(simpleDateFormat.format(musicPlayingService.getCurrentPosition()));
                    }
                    handler.postDelayed(this, 300);
                }
            });
            musicPlayingService.OnCompleted();
        } else {
            btnStopAndResume.setImageResource(R.drawable.iconpause);
            musicPlayingService.start();
            seekBar.setMax(musicPlayingService.getDuration() / 1000);
            if (fragment_dianhac.objectAnimator != null) {
                fragment_dianhac.objectAnimator.resume();
            }
            musicPlayingService.OnCompleted();
        }
    }

    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btnPlayPre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    public void prevBtnClicked() {
        musicPlayingService.stop();
        musicPlayingService.release();
        if (isShuffle && !isRepeat) {
            position = getRandomPosition(localSongsArrayList.size() - 1);
        } else if (!isShuffle && !isRepeat) {
            position = ((position - 1) < 0 ? (localSongsArrayList.size() - 1) : (position - 1));
        }
        uri = Uri.parse(localSongsArrayList.get(position).getPath());
        musicPlayingService.createMediaPlayer(position);
        prepareSong();
        musicPlayingService.start();
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btnPlayNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    public void nextBtnClicked() {
        musicPlayingService.stop();
        musicPlayingService.release();
        if (isShuffle && !isRepeat) {
            position = getRandomPosition(localSongsArrayList.size() - 1);
        } else if (!isShuffle && !isRepeat) {
            position = ((position + 1) % localSongsArrayList.size());
        }
        uri = Uri.parse(localSongsArrayList.get(position).getPath());
        musicPlayingService.createMediaPlayer(position);
        prepareSong();
        musicPlayingService.start();
    }

    private int getRandomPosition(int i) {
        Random randomPosition = new Random();
        return randomPosition.nextInt(i + 1);
    }

    private void eventClick() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicPlayingService != null && fromUser) {
                    musicPlayingService.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayLocalActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicPlayingService != null) {
                    int mCurrentTime = musicPlayingService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentTime);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    currentTime.setText(simpleDateFormat.format(musicPlayingService.getCurrentPosition()));
                }
                handler.postDelayed(this, 300);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                musicPlayingService.stop();
                musicPlayingService.release();
            }
        });

        btnShuffleSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShuffle == false) {
                    isShuffle = true;
                    btnShuffleSong.setImageResource(R.drawable.iconshuffled);
                    if (isRepeat == true) {
                        isRepeat = false;
                        btnPlayRepeat.setImageResource(R.drawable.iconrepeat);
                    }
                } else {
                    isShuffle = false;
                    btnShuffleSong.setImageResource(R.drawable.iconshuffle);
                }
            }
        });

        btnPlayRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepeat == false) {
                    isRepeat = true;
                    btnPlayRepeat.setImageResource(R.drawable.iconrepeated);
                    if (isShuffle == true) {
                        isShuffle = false;
                        btnShuffleSong.setImageResource(R.drawable.iconshuffle);
                    }
                } else {
                    isRepeat = false;
                    btnPlayRepeat.setImageResource(R.drawable.iconrepeat);
                }
            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                nextBtnClicked();
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("localsong")) {
                position = intent.getIntExtra("localsong", -1);
//                Log.d("PMTAN", "GetDataFromIntent: " + localSong.getTittle());
                localSongsArrayList = localSongsList;
            }

            if (intent.hasExtra("localsongrandom")) {
                position = intent.getIntExtra("localsongrandom", -1);
                isShuffle = true;
                btnShuffleSong.setImageResource(R.drawable.iconrepeated);
//                Log.d("PMTAN", "GetDataFromIntent: " + localSong.getTittle());
                localSongsArrayList = localSongsList;
            }
        }
    }

    private void initView() {
        btnReturn = findViewById(R.id.img_return_toolbar_play);
        songName = findViewById(R.id.tv_play_song_name);
        singerName = findViewById(R.id.tv_play_singer_name);
        btnMore = findViewById(R.id.img_play_more);
        currentTime = findViewById(R.id.tv_currenttimesong);
        totalTime = findViewById(R.id.tv_totaltimesong);
        seekBar = findViewById(R.id.sb_song);
        btnShuffleSong = findViewById(R.id.img_shuffle_play);
        btnPlayPre = findViewById(R.id.img_pre_play);
        btnStopAndResume = findViewById(R.id.img_play_stop);
        btnPlayNext = findViewById(R.id.img_next_play);
        btnPlayRepeat = findViewById(R.id.img_repeat_play);
        viewPagerPlay = findViewById(R.id.vp_playnhac);

        fragment_play_local_list_song = new Fragment_Play_Local_List_Song();
        fragment_dianhac = new Fragment_Dianhac();
        fragment_loi_bai_hat = new Fragment_Loi_Bai_Hat();
        viewPagerPlayListSongsAdapter = new ViewPagerPlayListSongsAdapter(getSupportFragmentManager());
        viewPagerPlayListSongsAdapter.addFragment(fragment_play_local_list_song);
        viewPagerPlayListSongsAdapter.addFragment(fragment_dianhac);
        viewPagerPlayListSongsAdapter.addFragment(fragment_loi_bai_hat);
        viewPagerPlay.setAdapter(viewPagerPlayListSongsAdapter);
        viewPagerPlay.setCurrentItem(1);
    }

    private void setMedia() {
        if (localSongsArrayList != null) {
            uri = Uri.parse(localSongsArrayList.get(position).getPath());
        }

        Intent intent = new Intent(this, MusicPlayingService.class);
        intent.putExtra("servicePosition", position);
        startService(intent);
    }

    private void prepareSong() {
        btnStopAndResume.setImageResource(R.drawable.iconpause);
        seekBar.setMax(musicPlayingService.getDuration() / 1000);
        seekBar.setProgress(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        totalTime.setText(simpleDateFormat.format(musicPlayingService.getDuration()));
        songName.setText(localSongsArrayList.get(position).getTittle());
        singerName.setText(localSongsArrayList.get(position).getArtist());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPagerPlayListSongsAdapter.getItem(0) != null) {
                    if (localSongsArrayList.size() > 0) {
                        if (localSongsArrayList.get(position).getLyric() != null) {
                            fragment_loi_bai_hat.GetFirstLyric(localSongsArrayList.get(position).getLyric());
                        }
                        if (localSongsArrayList.get(position).getPath() != null) {
                            fragment_dianhac.getSongBitmap(localSongsArrayList.get(position).getPath());
                        } else if (localSongsArrayList.get(position).getPath() == null) {
                            CircleImageView circleImageView;
                            circleImageView = findViewById(R.id.circleimagedianhac);
                            circleImageView.setBackgroundResource(R.drawable.demo_music);
                        }
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 300);
                    }
                }
            }
        }, 50);
        musicPlayingService.OnCompleted();
        showNotification(R.drawable.iconpause);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicPlayingService.MyBinder myBinder = (MusicPlayingService.MyBinder) service;
        musicPlayingService = myBinder.getService();
        musicPlayingService.setCallBack(this);
        Toast.makeText(musicPlayingService, "connected service" + musicPlayingService, Toast.LENGTH_SHORT).show();
        prepareSong();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicPlayingService = null;
    }

    void showNotification(int playPauseBtn) {
        Intent intent = new Intent(this, PlaySongListener.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent prevIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        byte[] pic = null;
        pic = getBitmap(localSongsArrayList.get(position).getPath());
        Bitmap thumb = null;
        if (pic!= null){
            thumb = BitmapFactory.decodeByteArray(pic,0,pic.length);
        } else {
            thumb = BitmapFactory.decodeResource(getResources(),R.drawable.icon_app);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(thumb)
                .setContentTitle(localSongsArrayList.get(position).getTittle())
                .setContentText(localSongsArrayList.get(position).getArtist())
                .addAction(R.drawable.ic_previous,"Previous",prevPending)
                .addAction(R.drawable.ic_pause,"Pause",pausePending)
                .addAction(R.drawable.ic_next,"Next",nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }

    private byte[] getBitmap(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        return art;
    }
}