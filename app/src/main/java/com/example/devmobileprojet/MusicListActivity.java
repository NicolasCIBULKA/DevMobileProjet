package com.example.devmobileprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;

import com.example.devmobileprojet.dataclass.Music;
import com.example.devmobileprojet.dataclass.MusicAdapter;
import com.example.devmobileprojet.treatment.PlayingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MusicListActivity extends AppCompatActivity  {

    private static final int REQUEST_CODE = 1;
    private static final String TAG = "Main Activity";
    public static final String PREFS = "MUSICPREFS";

    // Attributs
    static SharedPreferences prefs ;
    static SharedPreferences.Editor editor;
    public static ArrayList<Music> musicList;
    private ArrayList<String> musicnamelist;
    private ListView musicView;
    private PlayingService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    boolean sensorpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        prefs = getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        editor = prefs.edit();
        if(!prefs.contains("SensorPref")){
            editor.putBoolean("SensorPref", false);
        }
        sensorpref = prefs.getBoolean("SensorPref", false);
        musicView = (ListView)findViewById(R.id.song_list);// mettre l'id de la list du front
        musicList = new ArrayList<Music>();
        musicnamelist = new ArrayList<String>();
        permission();
        //getMusicList();
        Collections.sort(
                musicList, new Comparator<Music>() {
                    @Override
                    public int compare(Music a, Music b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                }
        );
        Log.d(TAG, "onCreate: Music List Size =" + musicList.size());
        //MusicAdapter adapter = new MusicAdapter(this, musicList);
        //musicView.setAdapter(adapter);
        musicView = findViewById(R.id.song_list);
        musicView.setAdapter(new ArrayAdapter<>(MusicListActivity.this, android.R.layout.simple_list_item_1, musicnamelist));
    }


    private void permission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MusicListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        else{
            getMusicList();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MusicListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
            else{
                getMusicList();

            }
        }
    }



    //start and bind the service when the activity starts
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
        unbindService(musicConnection);
        musicBound = false;
        */

    }


    // Activated if a music is picked in the list
    public void songPicked(View view){
        //musicSrv.setMusic(Integer.parseInt(view.getTag().toString()));
        int musicid = Integer.parseInt(view.getTag().toString());
        Music music = musicList.get(musicid);
        Log.d(TAG, "songPicked: id of the music : " + musicid);
        Log.d(TAG, "songPicked: "+ music.getTitle() + " - " + music.getPosition());
        Intent i = new Intent(this, Musicplayer.class);
        i.putExtra("position", musicid);
        startActivity(i);

    }



    public void getMusicList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicURI, null,null,null,null);
        if (musicCursor != null ){
            int columnTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int columnID = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int columnArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int duration = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int columnPosition = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while(musicCursor.moveToNext()){
                long currentid = musicCursor.getLong(columnID);
                String currentDuration = musicCursor.getString(duration);
                String currentTitle = musicCursor.getString(columnTitle);
                String currentArtist = musicCursor.getString(columnArtist);
                String currentPosition = musicCursor.getString(columnPosition);
                musicList.add(new Music(currentid, currentTitle, currentArtist, currentPosition, currentDuration));
                musicnamelist.add(currentTitle);
            }
            Log.d(TAG, "getMusicList: size of MusicList in getMuiscList: " + musicList.size());

            musicCursor.close();
        }

        // Handle onClick event
        musicView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Log.d(TAG, "songPicked: " + position);

                Music music = musicList.get(position);
                Log.d(TAG, "songPicked: id of the music : " + position);
                Log.d(TAG, "songPicked: "+ music.getTitle() + " - " + music.getPosition());
                Intent i = new Intent(MusicListActivity.this, Musicplayer.class);
                i.putExtra("position", position);
                startActivity(i);



            }
        });

    }

    public void gotoSettings(View view){
        Intent i = new Intent(MusicListActivity.this, Settings.class);

        startActivity(i);
    }
    // Functions todo
    /*
    - Give List of Music
    - add to Playlist - go to activity to add playlist
    - create Playlist - go to activity to create playlist
    - play the music - go to PlayPause activity & start service
     */


}