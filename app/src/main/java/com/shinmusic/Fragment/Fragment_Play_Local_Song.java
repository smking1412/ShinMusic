package com.shinmusic.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shinmusic.Activity.PlayLocalActivity;
import com.shinmusic.Activity.PlayNhacActivity;
import com.shinmusic.Adapter.PlayLocalAdapter;
import com.shinmusic.Adapter.PlaynhacAdapter;
import com.shinmusic.R;

public class Fragment_Play_Local_Song extends Fragment {
    View view;
    RecyclerView recyclerViewplaynhac;
    PlayLocalAdapter playLocalAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_dscacbaihat,container,false);
        recyclerViewplaynhac = view.findViewById(R.id.recyclerviewplaybaihat);
        if (PlayLocalActivity.listLocalSongs.size() > 0){
            playLocalAdapter = new PlayLocalAdapter(getActivity(), PlayLocalActivity.listLocalSongs);
            recyclerViewplaynhac.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewplaynhac.setAdapter(playLocalAdapter);
        }

        return  view;
    }
}
