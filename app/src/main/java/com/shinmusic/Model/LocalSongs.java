package com.shinmusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocalSongs implements Parcelable {
    private String path;
    private String tittle;
    private String artist;
    private String album;
    private String duration;
    private String lyric;

    public LocalSongs(String path, String tittle, String artist, String album, String duration, String lyric) {
        this.path = path;
        this.tittle = tittle;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.lyric = lyric;
    }

    protected LocalSongs(Parcel in) {
        path = in.readString();
        tittle = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        lyric = in.readString();
    }

    public static final Creator<LocalSongs> CREATOR = new Creator<LocalSongs>() {
        @Override
        public LocalSongs createFromParcel(Parcel in) {
            return new LocalSongs(in);
        }

        @Override
        public LocalSongs[] newArray(int size) {
            return new LocalSongs[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(tittle);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(duration);
        dest.writeString(lyric);
    }
}
