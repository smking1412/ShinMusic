package com.shinmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.util.ArrayList;

public class LocalAlbumAdapter extends RecyclerView.Adapter<LocalAlbumAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<LocalSongs> listAlbumLocal;

    public LocalAlbumAdapter(Context context, ArrayList<LocalSongs> listAlbumLocal) {
        this.mContext = context;
        this.listAlbumLocal = listAlbumLocal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dong_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listAlbumLocal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAlbum;
        TextView albumName;
        TextView singerName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.img_album);
            albumName = itemView.findViewById(R.id.tv_album_name);
            albumName = itemView.findViewById(R.id.tv_singer_album_name);
        }
    }
}
