package com.shinmusic.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.util.ArrayList;

public class LocalAlbumAdapter extends RecyclerView.Adapter<LocalAlbumAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<LocalSongs> listAlbumLocal;

    public LocalAlbumAdapter(Context mContext, ArrayList<LocalSongs> listAlbumLocal) {
        this.mContext = mContext;
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
        holder.albumName.setText(listAlbumLocal.get(position).getAlbum());
        holder.singerName.setText(listAlbumLocal.get(position).getArtist());
        Bitmap image = getAlbumBitmap(listAlbumLocal.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.imgAlbum);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.icon_app)
                    .into(holder.imgAlbum);
        }
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
            singerName = itemView.findViewById(R.id.tv_singer_album_name);
        }
    }

    private Bitmap getAlbumBitmap(String uri) {
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
