package com.example.musicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.models.TrackModel;
import com.example.musicplayer.services.CreateNotification;
import com.example.musicplayer.services.OnClearFromRecentService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView textViewTitle, textViewArtist, textViewAlbum, textViewStart, textViewEnd;
    ImageView imageViewAddToPlaylist, imageViewShuffleAndLoop, imageViewPrevious, imageViewPlay, imageViewNext;
    SeekBar seekBarProgress, seekBarVolume;

    List<TrackModel> trackModelList;

    MediaPlayer mediaPlayer;
    int songPosition = 0;
    String playState = "Loop";

    AudioManager audioManager;

    NotificationManager notificationManager;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionName");

            switch (action){
                case CreateNotification.ACTION_PREVIOUS:
                    Previous();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (mediaPlayer.isPlaying()){
                        Pause();
                    }
                    else {
                        Play();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    Next();
                    break;
            }
        }
    };

    private void CreateChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID, "MusicPlayer", NotificationManager.IMPORTANCE_HIGH);
            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        imageViewPlay.setImageResource(R.drawable.icons8_pause_64);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        ViewBinding();

        GetTrackListFromIntent();

        TouchHandler();

        CreateMediaPlayer();

    }

    private void TouchHandler() {

        imageViewShuffleAndLoop.setOnClickListener(view -> {
            if (playState.equals("Loop")) {
                playState = "Shuffle";
                imageViewShuffleAndLoop.setImageResource(R.drawable.icons8_shuffle_64);
            }
            else {
                if (playState.equals("Shuffle")){
                    playState = "Go";
                    imageViewShuffleAndLoop.setImageResource(R.drawable.icons8_arrow_64);
                }
                else {
                    if (playState.equals("Go")){
                        playState = "Loop";
                        imageViewShuffleAndLoop.setImageResource(R.drawable.icons8_repeat_64);
                    }
                }
            }
        });

        imageViewNext.setOnClickListener(view -> {
            Next();
        });

        imageViewPrevious.setOnClickListener(view -> {
            Previous();
        });

        imageViewPlay.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()){
                Pause();
            }
            else {
                Play();
            }
        });

        seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                UpdateProgress();
            }
        });

        try {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetTrackListFromIntent() {
        Intent intent = getIntent();
        trackModelList = new ArrayList<>();
        trackModelList = (List<TrackModel>) intent.getSerializableExtra("TrackModelList");
        songPosition = intent.getIntExtra("TrackPosition",0);
    }

    private void ViewBinding() {
        textViewTitle = findViewById(R.id.textViewTitlePlayer);
        textViewArtist = findViewById(R.id.textViewArtistPlayer);
        textViewAlbum = findViewById(R.id.textViewAlbumPlayer);
        textViewStart = findViewById(R.id.textViewStart);
        textViewEnd = findViewById(R.id.textViewEnd);

        textViewTitle.setSelected(true);
        textViewArtist.setSelected(true);
        textViewAlbum.setSelected(true);

        imageViewPlay = findViewById(R.id.imageViewPlay);
        imageViewAddToPlaylist = findViewById(R.id.imageViewAddToPlaylist);
        imageViewShuffleAndLoop = findViewById(R.id.imageViewShuffleAndLoop);
        imageViewPrevious = findViewById(R.id.imageViewPrevious);
        imageViewNext = findViewById(R.id.imageViewNext);

        seekBarProgress = findViewById(R.id.seekBarProgress);
        seekBarVolume = findViewById(R.id.seekBarVolume);

    }

    private void UpdateProgress(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentPosition = mediaPlayer.getCurrentPosition();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                textViewStart.setText(simpleDateFormat.format(currentPosition));
                seekBarProgress.setProgress(currentPosition);
                if (currentPosition == mediaPlayer.getDuration()){
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        CreateMediaPlayer();
                        mediaPlayer.start();
                        imageViewPlay.setImageResource(R.drawable.icons8_pause_64);
                    }
                    else {
                        CreateMediaPlayer();
                    }
                    SetTime();
                    UpdateProgress();
                }
                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void CreateMediaPlayer() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CreateChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(trackModelList.get(songPosition).getFilepath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        textViewTitle.setText(trackModelList.get(songPosition).getTitle());
        textViewArtist.setText(trackModelList.get(songPosition).getArtist());
        textViewAlbum.setText(trackModelList.get(songPosition).getAlbum());
        imageViewPlay.setImageResource(R.drawable.icons8_pause_64);
        SetTime();
        UpdateProgress();

        CreateNotification.CreateNotification(MusicPlayerActivity.this, trackModelList.get(songPosition),
                R.drawable.ic_pause_black_24dp, songPosition, trackModelList.size() - 1);

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            if (playState.equals("Loop"))
                mediaPlayer.reset();
            else {
                if (playState.equals("Shuffle")){
                    CreateRandomTrackPosition();
                    CreateMediaPlayer();
                }
                else {
                    if (playState.equals("Go")){
                        Next();
                    }
                }
            }
        });
    }

    private void CreateRandomTrackPosition() {
        int limit = trackModelList.size();
        Random random = new Random();
        int randomNumber = random.nextInt(limit);
        songPosition = randomNumber;
    }

    private void SetTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        textViewEnd.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBarProgress.setMax(mediaPlayer.getDuration());
    }

    private void Play(){
        mediaPlayer.start();
        imageViewPlay.setImageResource(R.drawable.icons8_pause_64);
        SetTime();
        UpdateProgress();
        CreateNotification.CreateNotification(MusicPlayerActivity.this, trackModelList.get(songPosition),
                R.drawable.ic_pause_black_24dp, songPosition, trackModelList.size() - 1);
    }

    private void Pause(){
        mediaPlayer.pause();
        imageViewPlay.setImageResource(R.drawable.icons8_play_64);
        SetTime();
        UpdateProgress();
        CreateNotification.CreateNotification(MusicPlayerActivity.this, trackModelList.get(songPosition),
                R.drawable.ic_play_arrow_black_24dp, songPosition, trackModelList.size() - 1);
    }

    private void Previous() {
        songPosition -= 1;
        int maxLength = trackModelList.size();
        if (songPosition < 0){
            songPosition = maxLength - 1;
        }
        if (playState.equals("Shuffle")){
            CreateRandomTrackPosition();
        }
        else {
            if (playState.equals("Loop")){
                songPosition += 1;
                mediaPlayer.reset();
            }
        }
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            CreateMediaPlayer();
            mediaPlayer.start();
            imageViewPlay.setImageResource(R.drawable.icons8_pause_64);
        }
        else {
            CreateMediaPlayer();
        }
        SetTime();
        UpdateProgress();
        CreateNotification.CreateNotification(MusicPlayerActivity.this, trackModelList.get(songPosition),
                R.drawable.ic_pause_black_24dp, songPosition, trackModelList.size() - 1);
    }

    private void Next() {
        songPosition += 1;
        int maxLength = trackModelList.size();
        if (songPosition > maxLength - 1){
            songPosition = 0;
        }
        if (playState.equals("Shuffle")){
            CreateRandomTrackPosition();
        }
        else {
            if (playState.equals("Loop")){
                songPosition -= 1;
                mediaPlayer.reset();
            }
        }
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            CreateMediaPlayer();
            imageViewPlay.setImageResource(R.drawable.icons8_pause_64);
        }
        else {
            CreateMediaPlayer();
        }
        SetTime();
        UpdateProgress();
        CreateNotification.CreateNotification(MusicPlayerActivity.this, trackModelList.get(songPosition),
                R.drawable.ic_pause_black_24dp, songPosition, trackModelList.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            notificationManager.cancelAll();
//        }
        unregisterReceiver(broadcastReceiver);
    }

}