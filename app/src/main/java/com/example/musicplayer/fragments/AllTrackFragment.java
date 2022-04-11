package com.example.musicplayer.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicplayer.DatabaseHandler;
import com.example.musicplayer.R;
import com.example.musicplayer.activities.TestActivity;
import com.example.musicplayer.adapters.TrackAdapter;
import com.example.musicplayer.models.TrackModel;

import java.util.ArrayList;
import java.util.List;

public class AllTrackFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 502;

    List<TrackModel> trackModelList;
    TrackAdapter trackAdapter;
    RecyclerView recyclerViewTrack;

    LinearLayout layout;

    DatabaseHandler db;

    public AllTrackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_track, container, false);

        recyclerViewTrack = view.findViewById(R.id.recyclerView_all_track);
        layout = view.findViewById(R.id.all_track_layout);
        trackModelList = new ArrayList<>();
        recyclerViewTrack.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new DatabaseHandler(getContext());
        trackModelList = db.getAllTrack();

        //complete
        trackAdapter = new TrackAdapter(getContext(), trackModelList);
        recyclerViewTrack.setAdapter(trackAdapter);

        //load dialog for waiting

//        requestRead();

        return view;
    }
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            readFile();
        }
    }

    public void readFile() {

        Cursor cursor = query(getContext());
        if (cursor != null){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                TrackModel trackModel = new TrackModel(cursor);
                db.addTrack(trackModel);
            }
        }
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile();
            } else {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public Cursor query(Context context) {

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,     // context id/ uri id of the file
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

        // the last parameter sorts the data alphanumerically

        return cursor;
    }
}