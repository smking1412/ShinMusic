package com.shinmusic.Utils;

import android.content.Context;

public interface PlaySongListener {
    void onClickItemSong(Context context,int position);
    void onClickPlayPause();
    void onClickPlayPrev();
    void onClickPlayNext();
}
