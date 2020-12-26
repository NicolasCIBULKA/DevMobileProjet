package com.example.devmobileprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.devmobileprojet.dataclass.Music;
import com.example.devmobileprojet.dataclass.MusicAdapter;
import com.example.devmobileprojet.treatment.PlayingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
    Activity that display the list of music and playlists
    FRONT TODO
 */
public class MusicList extends AppCompatActivity {
    // Attributs

    private ArrayList<Music> musicList;
    private ListView musicView;
    private PlayingService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // musicView = (ListView)findViewById(R.id.);// mettre l'id de la list du front
        musicList = new ArrayList<Music>();

        getMusicList();

        Collections.sort(
                musicList, new Comparator<Music>() {
                    @Override
                    public int compare(Music a, Music b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                }
        );

        MusicAdapter musicAdapter = new MusicAdapter(this, musicList);
        musicView.setAdapter(musicAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, PlayingService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayingService.MusicBinder binder = (PlayingService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(musicList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void songPicked(View view){
        musicSrv.setMusic(Integer.parseInt(view.getTag().toString()));
        // TODO
        musicSrv.playMusic();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                //shuffle
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMusicList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicURI, null,null,null,null);
        if (musicCursor != null && musicCursor.moveToFirst()){
            int columnTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int columnID = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int columnArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                long currentid = musicCursor.getLong(columnID);
                String currentTitle = musicCursor.getString(columnTitle);
                String currentArtist = musicCursor.getString(columnArtist);
                musicList.add(new Music(currentid, currentTitle, currentArtist));
            }while(musicCursor.moveToNext());
        }
    }
    // Functions todo
    /*
    - Give List of Music
    - add to Playlist - go to activity to add playlist
    - create Playlist - go to activity to create playlist
    - play the music - go to PlayPause activity & start service
     */


}