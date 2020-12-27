package com.shinmusic.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shinmusic.Adapter.AllAlbumAdapter;
import com.shinmusic.Model.Album;
import com.shinmusic.R;
import com.shinmusic.Service.APIService;
import com.shinmusic.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachTatcaAlbumActivity extends AppCompatActivity {
    RecyclerView recyclerViewAllAlum;
    Toolbar toolbarAllAlbum;
    AllAlbumAdapter allAlbumAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_tatca_album);
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Album>> callback = dataservice.GetAllAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> mangalbum = (ArrayList<Album>) response.body();
                allAlbumAdapter = new AllAlbumAdapter(DanhsachTatcaAlbumActivity.this,mangalbum);
                recyclerViewAllAlum.setLayoutManager(new GridLayoutManager(DanhsachTatcaAlbumActivity.this,2));//2 cột
                recyclerViewAllAlum.setAdapter(allAlbumAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

    private void init() {
        recyclerViewAllAlum = findViewById(R.id.recyclerviewallalbum);
        toolbarAllAlbum = findViewById(R.id.toolbartatcaalbum);
        setSupportActionBar(toolbarAllAlbum);
        getSupportActionBar().setTitle("Tất cả các Album");
        toolbarAllAlbum.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
