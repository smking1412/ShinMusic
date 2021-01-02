package com.shinmusic.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shinmusic.Model.BaiHat;
import com.shinmusic.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.wcy.lrcview.LrcView;

public class Fragment_Loi_Bai_Hat extends Fragment {
    private LrcView lrcView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loi_bai_hat, container, false);
        lrcView = view.findViewById(R.id.lrc_view);
        lrcView.loadLrcByUrl("https://shingetsu.000webhostapp.com/File%20Lyric/Rolling%20in%20the%20Deep-Adele.lrc");
        return view;
    }

    public void GetLyric(String loibaihat) {
        if (loibaihat != null ){
//            lrcView.loadLrcByUrl(loibaihat);

        }
    }
}
