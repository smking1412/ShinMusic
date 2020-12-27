package com.shinmusic.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shinmusic.R;

import java.io.IOException;
import java.io.InputStream;

import me.wcy.lrcview.LrcView;

public class Fragment_Loi_Bai_Hat extends Fragment {
    private LrcView lrcView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loi_bai_hat,container,false);
        lrcView = view.findViewById(R.id.lrc_view);
        return view;
    }

    public String GetLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }
}
