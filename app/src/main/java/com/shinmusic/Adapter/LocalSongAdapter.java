package com.shinmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shinmusic.Activity.PlayLocal2Activity;
import com.shinmusic.Activity.PlayLocalActivity;
import com.shinmusic.Activity.PlayNhacActivity;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.util.ArrayList;

public class LocalSongAdapter extends RecyclerView.Adapter<LocalSongAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LocalSongs> listSongLocal;

    public LocalSongAdapter(Context context, ArrayList<LocalSongs> listSongLocal) {
        this.context = context;
        this.listSongLocal = listSongLocal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalSongs localSong = listSongLocal.get(position);
        holder.songName.setText(localSong.getTittle());
        holder.singerName.setText(localSong.getArtist());
        Bitmap image = getSongBitmap(localSong.getPath());
        if (image != null) {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.imgSong);
        } else {
            Glide.with(context)
                    .load(R.drawable.icon_app)
                    .into(holder.imgSong);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayLocalActivity.class);
//                Intent intent = new Intent(context, PlayLocal2Activity.class);
                intent.putExtra("localsong", localSong);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSongLocal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSong;
        TextView songName;
        TextView singerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.img_local_song);
            songName = itemView.findViewById(R.id.tv_local_song_name);
            singerName = itemView.findViewById(R.id.tv_local_song_singer_name);
        }
    }

    private Bitmap getSongBitmap(String uri) {
        Bitmap imageSong = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            imageSong = BitmapFactory.decodeByteArray(art, 0, art.length);
        }
        retriever.release();
        return imageSong;
    }
}
