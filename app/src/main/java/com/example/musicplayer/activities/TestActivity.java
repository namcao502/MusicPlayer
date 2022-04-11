package com.example.musicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.models.TrackModel;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        requestRead();
    }
    /**
     * permission code
     */
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 502;

    /**
     * requestPermissions and do something
     *
     */
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            readFile();
        }
    }

    /**
     * do you want to do
     */
    public void readFile() {
        // do something
        ListView audioView = findViewById(R.id.songView);

        ArrayList<String> audioList = new ArrayList<>();

//        String[] proj = {
//                MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.DISPLAY_NAME,
//        };
//        // Can include more data for more details and check it.
//
//        Cursor audioCursor = getContentResolver()
//                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);
//
//        //access
//        if(audioCursor != null){
//            if(audioCursor.moveToFirst()){
//                do{
////                    String fullPath = audioCursor.getString(audioCursor.getColumnIndex("_data"));
//                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
//                    audioList.add(audioCursor.getString(audioIndex));
//                }while(audioCursor.moveToNext());
//            }
//        }
//        audioCursor.close();

        List<TrackModel> trackModelList = new ArrayList<>();
        Cursor cursor = query(TestActivity.this);
        if (cursor != null){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                TrackModel trackModel = new TrackModel(cursor);
                trackModelList.add(trackModel);
                audioList.add(trackModel.getTitle());
            }
        }
        cursor.close();
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(filePath);
//        byte[] coverBytes = retriever.getEmbeddedPicture();
//        Bitmap songCover;
//        if (coverBytes!=null)
//            songCover = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length);
//        else
//            songCover=null;


        //complete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1, audioList);
        audioView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
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
                MediaStore.Audio.Media.DURATION,
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