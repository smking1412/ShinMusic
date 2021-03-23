package com.shinmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shinmusic.Activity.DanhsachbaihatActivity;
import com.shinmusic.Model.Album;
import com.shinmusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllAlbumAdapter extends RecyclerView.Adapter<AllAlbumAdapter.ViewHolder>{
    Context context;
    ArrayList<Album> mangalbum;

    public AllAlbumAdapter(Context context, ArrayList<Album> mangalbum) {
        this.context = context;
        this.mangalbum = mangalbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = mangalbum.get(position);
        Picasso.with(context).load(album.getImageAlbum()).into(holder.imgalbum);
        holder.txttenalbum.setText(album.getTenAlbum());
        holder.txttencasyalbum.setText(album.getTenCaSy());
    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgalbum;
        TextView txttenalbum,txttencasyalbum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgalbum = itemView.findViewById(R.id.imageviewallalbum);
            txttenalbum = itemView.findViewById(R.id.tv_album_name);
            txttencasyalbum = itemView.findViewById(R.id.textviewcasyalbum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("album",mangalbum.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
