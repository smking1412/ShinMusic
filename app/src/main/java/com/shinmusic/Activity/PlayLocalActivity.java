package com.shinmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shinmusic.Adapter.ViewPagerPlayListSongsAdapter;
import com.shinmusic.Fragment.Fragment_Dianhac;
import com.shinmusic.Fragment.Fragment_Loi_Bai_Hat;
import com.shinmusic.Fragment.Fragment_Play_Local_List_Song;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.shinmusic.Fragment.Fragment_Ca_Nhan.localSongsList;

public class PlayLocalActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
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
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ArrayList<LocalSongs> listLocalSongs = localSongsList;
    private ViewPagerPlayListSongsAdapter viewPagerPlayListSongsAdapter;
    private Uri uri;
    private Handler handler = new Handler();

    private int position = 0;
    private boolean isRepeat = false;
    private boolean isShuffle = false;

    private Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac2);
        initView();
        getDataFromIntent();
        prepareSong();
        eventClick();
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        prevThreadBtn();
        nextThreadBtn();
        super.onResume();
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

    private void playPauseClicked() {
        if (mediaPlayer.isPlaying()) {
            btnStopAndResume.setImageResource(R.drawable.iconplay);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            if (fragment_dianhac.objectAnimator != null) {
                fragment_dianhac.objectAnimator.pause();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentTime = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentTime);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                        currentTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    }
                    handler.postDelayed(this, 300);
                }
            });
        } else {
            btnStopAndResume.setImageResource(R.drawable.iconpause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            if (fragment_dianhac.objectAnimator != null) {
                fragment_dianhac.objectAnimator.resume();
            }
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

    private void prevBtnClicked() {
        if (isShuffle && !isRepeat){
            position = getRandomPosition(listLocalSongs.size() - 1);
        } else if (!isShuffle && !isRepeat) {
            position = ((position - 1) < 0 ? (listLocalSongs.size() - 1) : (position - 1));
        }
        prepareSong();
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

    private void nextBtnClicked() {
        if (isShuffle && !isRepeat){
            position = getRandomPosition(listLocalSongs.size() - 1);
        } else if (!isShuffle && !isRepeat) {
            position = ((position + 1) % listLocalSongs.size());
        }
        prepareSong();

    }

    private int getRandomPosition(int i) {
        Random randomPosition = new Random();
        return randomPosition.nextInt(i+1);
    }

    private void eventClick() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentTime = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentTime);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    currentTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                }
                handler.postDelayed(this, 300);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });

        btnShuffleSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShuffle == false) {
                    isShuffle = true;
                    btnShuffleSong.setImageResource(R.drawable.iconshuffled);
                    if (isRepeat == true){
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
                    if (isShuffle == true){
                        isShuffle = false;
                        btnShuffleSong.setImageResource(R.drawable.iconshuffle);
                    }
                } else {
                    isRepeat = false;
                    btnPlayRepeat.setImageResource(R.drawable.iconrepeat);
                }
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("localsong")) {
                position = intent.getIntExtra("localsong", -1);
//                Log.d("PMTAN", "GetDataFromIntent: " + localSong.getTittle());
            }

            if (intent.hasExtra("localsongrandom")) {
                position = intent.getIntExtra("localsongrandom", -1);
                isShuffle = true;
                btnShuffleSong.setImageResource(R.drawable.iconrepeated);
//                Log.d("PMTAN", "GetDataFromIntent: " + localSong.getTittle());
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

    private void prepareSong() {
        if (listLocalSongs != null) {
            btnStopAndResume.setImageResource(R.drawable.iconpause);
            uri = Uri.parse(listLocalSongs.get(position).getPath());
        }
//        if (mediaPlayer.isPlaying() && mediaPlayer!= null){
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = new MediaPlayer();
//        }

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });
            mediaPlayer.setDataSource(String.valueOf(uri));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//            mediaPlayer.start();
//        } else {
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//            mediaPlayer.start();
//        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        seekBar.setProgress(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        totalTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        songName.setText(listLocalSongs.get(position).getTittle());
        singerName.setText(listLocalSongs.get(position).getArtist());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPagerPlayListSongsAdapter.getItem(0) != null) {
                    if (listLocalSongs.size() > 0) {
                        if (listLocalSongs.get(position).getLyric() != null) {
                            fragment_loi_bai_hat.GetFirstLyric(listLocalSongs.get(position).getLyric());
                        }
                        if (listLocalSongs.get(position).getPath() != null) {
                            fragment_dianhac.getSongBitmap(listLocalSongs.get(position).getPath());
                        } else if (listLocalSongs.get(position).getPath() == null) {
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
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
    }
}