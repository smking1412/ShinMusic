package com.shinmusic.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shinmusic.Activity.DanhsachTatcaAlbumActivity;
import com.shinmusic.Activity.DanhsachbaihatActivity;
import com.shinmusic.Activity.DanhsachtheloaitheochudeActivity;
import com.shinmusic.Adapter.AlbumAdapter;
import com.shinmusic.Model.Album;
import com.shinmusic.R;
import com.shinmusic.Service.APIService;
import com.shinmusic.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Album extends Fragment {
    View view;
    RecyclerView recyclerViewAlbum;
    TextView txtXemThemAlbum;
    AlbumAdapter albumAdapter;
    ImageView imgalbum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album,container,false);
        recyclerViewAlbum = view.findViewById(R.id.recyclerviewalbum);
        txtXemThemAlbum = view.findViewById(R.id.textviewxemthemalbum);
        txtXemThemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DanhsachTatcaAlbumActivity.class);
                startActivity(intent);
            }
        });

        GetData();
        return  view;
    }

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Album>> callback = dataservice.GetAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> albumArrayList = (ArrayList<Album>) response.body();

                albumAdapter = new AlbumAdapter(getActivity(),albumArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
                recyclerViewAlbum.setLayoutManager(linearLayoutManager);
                recyclerViewAlbum.setAdapter(albumAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

    }
}
