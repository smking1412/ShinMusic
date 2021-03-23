package com.shinmusic.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shinmusic.Activity.PlayNhacActivity;
import com.shinmusic.R;

import me.wcy.lrcview.LrcView;

public class Fragment_Loi_Bai_Hat extends Fragment {
    private LrcView lrcView;
    private String lyric ;
    private long timeSong = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loi_bai_hat, container, false);
        lrcView = view.findViewById(R.id.lrc_view);
        if (lyric != null) {
            lrcView.loadLrcByUrl(lyric);
        }
        lrcView.updateTime(timeSong);
        lrcView.setDraggable(true, new LrcView.OnPlayClickListener() {
            @Override
            public boolean onPlayClick(LrcView view, long time) {
                return true;
            }
        });
        return view;
    }

    public void UpdateTime(long time){
        timeSong = time;
        lrcView.updateTime(time);
        }

    public void GetLyric(String loibaihat) {
        if (loibaihat != null) {
            if (PlayNhacActivity.mangbaihat.size()==1){
                lyric = loibaihat;
            } else {
                lyric = loibaihat;
                lrcView.loadLrcByUrl(lyric);
            }
        } else {
            lyric = null;
            lrcView.loadLrc(lyric);
            lrcView.setLabel("Không thể tìm thấy lời bài hát (@ _ @)");
        }
    }

    public void GetFirstLyric(String loibaihat) {
        if (loibaihat != null) {
            lyric = loibaihat;
        }
    }
}
