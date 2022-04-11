package com.example.musicplayer.models;

import android.database.Cursor;

import java.io.Serializable;

public class TrackModel implements Serializable {

    private String title;
    private String album;
    private String artist;
    private String filepath;
    private String id;

    public TrackModel(Cursor cursor) {
        title = cursor.getString(0);
        artist = cursor.getString(1);
        album = cursor.getString(2);
        filepath = cursor.getString(3);
        id = cursor.getString(4);
    }

    public TrackModel(String id, String title, String artist, String album, String filepath) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.filepath = filepath;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
