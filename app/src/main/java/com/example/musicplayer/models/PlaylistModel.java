package com.example.musicplayer.models;

import java.util.List;

public class PlaylistModel {

    int id;
    String title;
    int amount;

    public PlaylistModel() {
    }

    public PlaylistModel(String title, int amount) {
        this.title = title;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
