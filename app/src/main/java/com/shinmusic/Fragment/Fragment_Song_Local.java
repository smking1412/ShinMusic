package com.shinmusic.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shinmusic.Activity.PlayLocalActivity;
import com.shinmusic.Activity.PlayNhacActivity;
import com.shinmusic.Adapter.LocalSongAdapter;
import com.shinmusic.R;

import static com.shinmusic.Fragment.Fragment_Ca_Nhan.localSongsList;


public class Fragment_Song_Local extends Fragment {
    View view;
    private Button btnPlayAll;
    private RecyclerView recyclerView;
    private LocalSongAdapter localSongAdapter;

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
                Intent intent = new Intent(getContext(), PlayLocalActivity.class);
                intent.putExtra("listlocalsongs",localSongsList);
                startActivity(intent);
            }
        });
    }

    private void init() {
        btnPlayAll = view.findViewById(R.id.btn_play_local_song);
        recyclerView = view.findViewById(R.id.recycler_song_local);
        recyclerView.setHasFixedSize(true);
        if (!(localSongsList.size() < 1)){
            localSongAdapter = new LocalSongAdapter(getActivity(), localSongsList);
            recyclerView.setAdapter(localSongAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        }
    }
}