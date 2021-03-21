package com.shinmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shinmusic.Model.BaiHat;
import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;

import java.util.ArrayList;

public class PlayLocalAdapter extends  RecyclerView.Adapter<PlayLocalAdapter.ViewHolder>{
    Context context;
    ArrayList<LocalSongs> localSongsArrayList;

    public PlayLocalAdapter(Context context, ArrayList<LocalSongs> localSongsArrayList) {
        this.context = context;
        this.localSongsArrayList = localSongsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_playbaihat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalSongs localSong = localSongsArrayList.get(position);
        holder.txtindex.setText(position + 1 + "");
        holder.txttenbaihat.setText(localSong.getTittle());
        holder.txtcasy.setText(localSong.getArtist());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return localSongsArrayList.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        TextView txtindex, txttenbaihat, txtcasy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtindex = itemView.findViewById(R.id.textviewplaynhacindex);
            txttenbaihat = itemView.findViewById(R.id.textviewplaynhactenbaihat);
            txtcasy = itemView.findViewById(R.id.textviewplaynhactencasy);

        }
    }
}
