package com.example.musicplayer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicplayer.DatabaseHandler;
import com.example.musicplayer.R;
import com.example.musicplayer.adapters.PlaylistAdapter;
import com.example.musicplayer.models.PlaylistModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PlaylistFragment extends Fragment {

    RecyclerView recyclerViewPlaylist;
    Toolbar toolbar;

//    SharedPreferences sharedPreferencesPlaylist;

    List<PlaylistModel> playlistModelList;
    PlaylistAdapter playlistAdapter;


    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.playlist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        DatabaseHandler db = new DatabaseHandler(getContext());

//        recyclerViewPlaylist = rootView.findViewById(R.id.recyclerView_playlist);
//        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
//        playlistModelList = new ArrayList<>();
//        playlistAdapter = new PlaylistAdapter(getContext(), playlistModelList);
//        recyclerViewPlaylist.setAdapter(playlistAdapter);


        //toolbar
        setHasOptionsMenu(true);
        toolbar = rootView.findViewById(R.id.playlist_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.playlist_menu_add_new_playlist:

                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.fragment_playlist_add_new_playlist_dialog);
                    dialog.show();

                    Button buttonAdd = dialog.findViewById(R.id.buttonAddNewPlaylistDialog);
                    EditText editTextTitle = dialog.findViewById(R.id.editTextAddNewPlaylistDialog);

                    buttonAdd.setOnClickListener(view -> {

                        String playlistTitle = editTextTitle.getText().toString();

                        if (playlistTitle.isEmpty()) {
                            Toast.makeText(getContext(), "Vui lòng nhập tên danh sách phát", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        PlaylistModel playlistModel = new PlaylistModel();
                        playlistModel.setTitle(playlistTitle);
                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();

                    });


            }

            return false;
        });

        return rootView;
    }
}