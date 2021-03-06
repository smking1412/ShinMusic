package com.shinmusic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.shinmusic.Adapter.ViewPagerPlayListSongsAdapter;
import com.shinmusic.Fragment.Fragment_Dianhac;
import com.shinmusic.Fragment.Fragment_Loi_Bai_Hat;
import com.shinmusic.Fragment.Fragment_Play_List_Baihat;
import com.shinmusic.Model.BaiHat;
import com.shinmusic.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayNhacActivity extends AppCompatActivity {
    Toolbar toolbarplaynhac;
    TextView txttimesong, txttotaltimesong;
    SeekBar seektime;
    ImageButton imgrandom, imgpre, imgplay, imgnext, imgrepeat;
    ViewPager viewPagerplaynhac;
    public static ArrayList<BaiHat> mangbaihat = new ArrayList<>();
    public static ViewPagerPlayListSongsAdapter viewPagerPlayListSongsAdapter;
    Fragment_Dianhac fragment_dianhac;
    Fragment_Play_List_Baihat fragment_play_list_baihat;
    Fragment_Loi_Bai_Hat fragment_loi_bai_hat;
    MediaPlayer mediaPlayer;
    int position = 0;
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetDataFromIntent();
        init();
        eventClick();
    }

    private void eventClick() {
        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgplay.setImageResource(R.drawable.iconplay);
                    if (fragment_dianhac.objectAnimator != null) {
                        fragment_dianhac.objectAnimator.pause();
                    }
                } else {
                    mediaPlayer.start();
                    xhandler.post(runnable);
                    imgplay.setImageResource(R.drawable.iconpause);
                    if (fragment_dianhac.objectAnimator != null) {
                        fragment_dianhac.objectAnimator.resume();
                    }
                }

            }
        });
        imgrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeat == false) {
                    if (checkrandom == true) {
                        imgrepeat.setImageResource(R.drawable.iconrepeated);
                        imgrandom.setImageResource(R.drawable.iconshuffle);
                    }
                    imgrepeat.setImageResource(R.drawable.iconrepeated);
                    repeat = true;
                } else {
                    imgrepeat.setImageResource(R.drawable.iconrepeat);
                    repeat = false;
                }
            }
        });

        imgrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkrandom == false) {
                    if (repeat == true) {
                        repeat = false;
                        imgrandom.setImageResource(R.drawable.iconshuffled);
                        imgrepeat.setImageResource(R.drawable.iconrepeat);
                    }
                    imgrandom.setImageResource(R.drawable.iconshuffled);
                    checkrandom = true;
                } else {
                    imgrandom.setImageResource(R.drawable.iconshuffle);
                    checkrandom = false;
                }
            }
        });

        seektime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                fragment_loi_bai_hat.UpdateTime(seekBar.getProgress());
//                lrcView.updateTime(seekBar.getProgress());
            }
        });

        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mangbaihat.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < mangbaihat.size()) {
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        if (repeat == true) {
                            if (position == 0) {
                                position = mangbaihat.size();
                            }
                            position -= 1;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (mangbaihat.size()) - 1) {
                            position = 0;
                        }
                        new PlayMp3().execute(mangbaihat.get(position).getDuongDan());
                        fragment_dianhac.Playnhac(mangbaihat.get(position).getImageBaiHat());
                        fragment_loi_bai_hat.GetLyric(mangbaihat.get(position).getLoiBaiHat());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                    }
                }
                imgpre.setClickable(false);
                imgnext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgpre.setClickable(true);
                        imgnext.setClickable(true);
                    }
                }, 5000);
            }
        });

        imgpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mangbaihat.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (position < mangbaihat.size()) {
                        imgplay.setImageResource(R.drawable.iconpause);
                        position--;

                        if (position < 0) {
                            position = mangbaihat.size() - 1;
                        }
                        if (repeat == true) {
                            position += 1;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }

                        new PlayMp3().execute(mangbaihat.get(position).getDuongDan());
                        fragment_dianhac.Playnhac(mangbaihat.get(position).getImageBaiHat());
                        fragment_loi_bai_hat.GetLyric(mangbaihat.get(position).getLoiBaiHat());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }
                imgpre.setClickable(false);
                imgnext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgpre.setClickable(true);
                        imgnext.setClickable(true);
                    }
                }, 5000);
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                long time = mediaPlayer.getCurrentPosition();
                seektime.setProgress((int) time);
                fragment_loi_bai_hat.UpdateTime(time);
            }
            xhandler.postDelayed(this, 50);
        }
    };

    private Handler xhandler = new Handler();

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if (intent != null) {
            if (intent.hasExtra("baihat")) {
                BaiHat baiHat = intent.getParcelableExtra("baihat");
                mangbaihat.add(baiHat);
            }

            if (intent.hasExtra("cacbaihat")) {
                ArrayList<BaiHat> baiHatArrayList = intent.getParcelableArrayListExtra("cacbaihat");
                mangbaihat = baiHatArrayList;
            }
        }
    }

    private void init() {
        toolbarplaynhac = findViewById(R.id.toolbarplaynhac);
        txttimesong = findViewById(R.id.textviewtimesong);
        txttotaltimesong = findViewById(R.id.textviewtotaltimesong);
        seektime = findViewById(R.id.seekbarsong);
        toolbarplaynhac = findViewById(R.id.toolbarplaynhac);
        imgrandom = findViewById(R.id.imagebuttonsuffle);
        imgpre = findViewById(R.id.imagebuttonplaybefore);
        imgplay = findViewById(R.id.imagebuttonplay);
        imgnext = findViewById(R.id.imagebuttonplaynext);
        imgrepeat = findViewById(R.id.imagebuttonplayrepeat);
        viewPagerplaynhac = findViewById(R.id.viewpagerplaynhac);

        setSupportActionBar(toolbarplaynhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarplaynhac.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                mediaPlayer.stop();
                mangbaihat.clear();
            }
        });
        toolbarplaynhac.setTitleTextColor(Color.WHITE);

        fragment_dianhac = new Fragment_Dianhac();
        fragment_play_list_baihat = new Fragment_Play_List_Baihat();
        fragment_loi_bai_hat = new Fragment_Loi_Bai_Hat();
        viewPagerPlayListSongsAdapter = new ViewPagerPlayListSongsAdapter(getSupportFragmentManager());
        viewPagerPlayListSongsAdapter.addFragment(fragment_play_list_baihat);
        viewPagerPlayListSongsAdapter.addFragment(fragment_dianhac);
        viewPagerPlayListSongsAdapter.addFragment(fragment_loi_bai_hat);
        viewPagerplaynhac.setAdapter(viewPagerPlayListSongsAdapter);
        fragment_dianhac = (Fragment_Dianhac) viewPagerPlayListSongsAdapter.getItem(1);
        if (mangbaihat.size() > 0) {
            getSupportActionBar().setTitle(mangbaihat.get(0).getTenBaiHat());
            new PlayMp3().execute(mangbaihat.get(0).getDuongDan());
            imgplay.setImageResource(R.drawable.iconpause);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPagerPlayListSongsAdapter.getItem(0) != null) {
                    if (mangbaihat.size() > 0) {
                        if (mangbaihat.get(0).getLoiBaiHat() != null) {
                            fragment_loi_bai_hat.GetFirstLyric(mangbaihat.get(0).getLoiBaiHat());
                        }
                        if (mangbaihat.get(0).getImageBaiHat() != null) {
                            fragment_dianhac.Playnhac(mangbaihat.get(0).getImageBaiHat());
                        } else if (mangbaihat.get(0).getImageBaiHat() == null) {
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
    }


    class PlayMp3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(baihat);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            TimeSong();
            UpdateTime();
        }
    }

    private void TimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txttotaltimesong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seektime.setMax(mediaPlayer.getDuration());
        seektime.setProgress(0);
    }

    private void UpdateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seektime.setProgress(mediaPlayer.getCurrentPosition());
//                    lrcView.updateTime(mediaPlayer.getCurrentPosition());
//                    fragment_loi_bai_hat.UpdateTime(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    txttimesong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    final Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (next == true) {
                                if (mangbaihat.size() > 0) {
                                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                        mediaPlayer = null;
                                    }
                                    if (position < mangbaihat.size()) {
                                        imgplay.setImageResource(R.drawable.iconpause);
                                        position--;

                                        if (position < 0) {
                                            position = mangbaihat.size() - 1;
                                        }
                                        if (repeat == true) {
                                            position += 1;
                                        }
                                        if (checkrandom == true) {
                                            Random random = new Random();
                                            int index = random.nextInt(mangbaihat.size());
                                            if (index == position) {
                                                position = index - 1;
                                            }
                                            position = index;
                                        }

                                        new PlayMp3().execute(mangbaihat.get(position).getDuongDan());
                                        fragment_dianhac.Playnhac(mangbaihat.get(position).getImageBaiHat());
                                        fragment_loi_bai_hat.GetLyric(mangbaihat.get(position).getLoiBaiHat());
                                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                                        UpdateTime();
                                    }
                                }
                                imgpre.setClickable(false);
                                imgnext.setClickable(false);
                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        imgpre.setClickable(true);
                                        imgnext.setClickable(true);
                                    }
                                }, 5000);
                                next = false;
                                handler1.removeCallbacks(this);
                            } else {
                                handler1.postDelayed(this, 1000);
                            }
                        }
                    }, 1000);
                }
            }
        }, 300);
    }

}