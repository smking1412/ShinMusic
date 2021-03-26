package com.shinmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shinmusic.Model.LocalSongs;
import com.shinmusic.R;
import com.shinmusic.Utils.PlaySongListener;

import java.util.ArrayList;

import static com.shinmusic.Fragment.Fragment_Ca_Nhan.localSongsList;

public class PlayLocalAdapter extends RecyclerView.Adapter<PlayLocalAdapter.ViewHolder> {
    Context context;
    private ArrayList<LocalSongs> listLocalSongs = new ArrayList<>();
//    private PlaySongListener playSongListener;

    public PlayLocalAdapter(Context context, ArrayList<LocalSongs> listLocalSongs) {
        this.context = context;
        this.listLocalSongs = listLocalSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_playbaihat, parent, false);
        listLocalSongs = localSongsList;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalSongs localSong = listLocalSongs.get(position);
        holder.txtindex.setText(position + 1 + "");
        holder.txttenbaihat.setText(localSong.getTittle());
        holder.txtcasy.setText(localSong.getArtist());
        if (listLocalSongs.size() > 1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //playSongListener.onClickItemSong(context,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listLocalSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtindex, txttenbaihat, txtcasy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtindex = itemView.findViewById(R.id.textviewplaynhacindex);
            txttenbaihat = itemView.findViewById(R.id.textviewplaynhactenbaihat);
            txtcasy = itemView.findViewById(R.id.textviewplaynhactencasy);

        }
    }
}
