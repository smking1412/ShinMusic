package com.shinmusic.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.shinmusic.Adapter.ViewPagerLocalAdapter;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.util.ArrayList;

public class Fragment_Ca_Nhan extends Fragment {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 500;
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerLocalAdapter viewPagerAdapter;
    public static ArrayList<LocalSongs> localSongsList = new ArrayList<>();
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ca_nhan, container, false);
        requestPermission();
        initViewPager();
        return view;

    }

    private void initViewPager() {
        viewPager = view.findViewById(R.id.viewpager_local);
        tabLayout = view.findViewById(R.id.tablayout_local);
        viewPagerAdapter = new ViewPagerLocalAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Fragment_Local_Song(), "Bài hát");
        viewPagerAdapter.addFragments(new Fragment_Local_Album(), "Album");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            Toast.makeText(getActivity(), "Quyền đã được phép!", Toast.LENGTH_SHORT).show();
            localSongsList = getAllLocalSong(context);
        }
    }

    private ArrayList<LocalSongs> getAllLocalSong(Context context) {
        ArrayList<LocalSongs> localSongsArrayList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] prjection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,//path
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
        };
        Cursor cursor = this.getContext().getContentResolver().query(uri, prjection,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String path = cursor.getString(2);
                String duration = cursor.getString(3);
                String artist = cursor.getString(4);
                if (duration != null) {
                    if (Integer.parseInt(duration) > 50000) {
                        LocalSongs localSong = new LocalSongs(path, title, artist, album, duration,null);
                        localSongsArrayList.add(localSong);
//                        Log.d("PMT", "getAllLocalSong: " + localSong.getTittle());
                    }
                }
            }
            cursor.close();
        }
        return localSongsArrayList;
    }

}
