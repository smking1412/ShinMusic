package com.shinmusic.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shinmusic.Activity.PlayLocalActivity;
import com.shinmusic.Adapter.LocalSongAdapter;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.util.ArrayList;
import java.util.Random;

import static com.shinmusic.Fragment.Fragment_Ca_Nhan.localSongsList;


public class Fragment_Local_Song extends Fragment {
    View view;
    private Button btnPlayAll;
    private RecyclerView recyclerView;
    private LocalSongAdapter localSongAdapter;
    private int position = 0;
    private ArrayList<LocalSongs> listLocalSongs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_song_local, container, false);
        init();
        eventClick();
        return view;
    }

    private void eventClick() {
        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                position = random.nextInt(localSongsList.size());
                Intent intent = new Intent(getContext(), PlayLocalActivity.class);
                intent.putExtra("localsongrandom",position);
                startActivity(intent);
            }
        });
    }

    private void init() {
        btnPlayAll = view.findViewById(R.id.btn_play_local_song);
        recyclerView = view.findViewById(R.id.recycler_song_local);
        recyclerView.setHasFixedSize(true);
        listLocalSongs = localSongsList;
        if (!(listLocalSongs.size() < 1)){
            localSongAdapter = new LocalSongAdapter(getActivity(),listLocalSongs);
            recyclerView.setAdapter(localSongAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        }
    }
}