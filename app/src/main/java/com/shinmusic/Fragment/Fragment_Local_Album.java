package com.shinmusic.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shinmusic.Adapter.LocalAlbumAdapter;
import com.shinmusic.Adapter.LocalSongAdapter;
import com.shinmusic.Adapter.ViewPagerPlayListSongsAdapter;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.util.ArrayList;

import static com.shinmusic.Fragment.Fragment_Ca_Nhan.localSongsList;


public class Fragment_Local_Album extends Fragment {
    private RecyclerView recyclerView;
    private LocalAlbumAdapter localAlbumAdapter;
    private Button btnCreateAlbum;
    private View view;
    private ArrayList<LocalSongs> listLocalSongs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_album_local, container, false);
        init();
        return view;
    }

    private void init() {
        btnCreateAlbum = view.findViewById(R.id.btn_create_local_album);
        recyclerView = view.findViewById(R.id.recycler_album_local);
        recyclerView.setHasFixedSize(true);
        listLocalSongs = localSongsList;
        if (!(listLocalSongs.size() < 1)){
            localAlbumAdapter = new LocalAlbumAdapter(getActivity(),listLocalSongs);
            recyclerView.setAdapter(localAlbumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        }
    }
}