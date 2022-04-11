package com.example.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicplayer.models.PlaylistModel;
import com.example.musicplayer.models.TrackModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NoName";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TRACK_NAME = "track";
    private static final String TABLE_PLAYLIST_NAME = "playlist";

    private static final String TRACK_ID = "track_id";
    private static final String TRACK_TITLE = "track_title";
    private static final String TRACK_ARTIST = "track_artist";
    private static final String TRACK_ALBUM = "track_album";
    private static final String TRACK_PATH = "track_path";
    private static final String TRACK_PLAYLIST_ID = "track_playlist_id";

    private static final String PLAYLIST_ID = "playlist_id";
    private static final String PLAYLIST_TITLE = "playlist_title";
    private static final String PLAYLIST_AMOUNT = "playlist_amount";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_track_table = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_TRACK_NAME, TRACK_ID, TRACK_TITLE, TRACK_ARTIST, TRACK_ALBUM, TRACK_PATH, TRACK_PLAYLIST_ID);
        String create_playlist_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INT)",
                TABLE_PLAYLIST_NAME, PLAYLIST_ID, PLAYLIST_TITLE, PLAYLIST_AMOUNT);
        db.execSQL(create_track_table);
        db.execSQL(create_playlist_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String drop_user_table = String.format("DROP TABLE IF EXISTS %s", TABLE_TRACK_NAME);
        String drop_customer_table = String.format("DROP TABLE IF EXISTS %s", TABLE_PLAYLIST_NAME);
        db.execSQL(drop_user_table);
        db.execSQL(drop_customer_table);
        onCreate(db);
    }

    public void addTrack(TrackModel trackModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRACK_ID, trackModel.getId());
        values.put(TRACK_ALBUM, trackModel.getAlbum());
        values.put(TRACK_ARTIST, trackModel.getArtist());
        values.put(TRACK_PATH, trackModel.getFilepath());
        values.put(TRACK_TITLE, trackModel.getTitle());

        db.insert(TABLE_TRACK_NAME, null, values);
        db.close();
    }

    public List<TrackModel> getAllTrack() {
        List<TrackModel> trackModelList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TRACK_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            TrackModel trackModel = new TrackModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            trackModelList.add(trackModel);
            cursor.moveToNext();
        }
        return trackModelList;
    }

    public void deleteTrack(String trackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACK_NAME, TRACK_ID + " = ?", new String[] { trackId });
        db.close();
    }

    public void addPlaylist(PlaylistModel playlistModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PLAYLIST_TITLE, playlistModel.getTitle());

        db.insert(TABLE_PLAYLIST_NAME, null, values);
        db.close();
    }

    public void deletePlaylist(String playlistId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST_NAME, PLAYLIST_ID + " = ?", new String[] { playlistId });
        db.close();
    }

    public void updatePlaylist(PlaylistModel playlistModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PLAYLIST_TITLE, playlistModel.getTitle());
        values.put(PLAYLIST_AMOUNT, playlistModel.getAmount());

        db.update(TABLE_PLAYLIST_NAME, values, PLAYLIST_ID + " = ?", new String[] {String.valueOf(playlistModel.getId())});
        db.close();
    }
}
