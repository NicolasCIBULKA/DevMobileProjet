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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.devmobileprojet.dataclass.Music;
import com.example.devmobileprojet.dataclass.MusicAdapter;
import com.example.devmobileprojet.treatment.PlayingService;

import java.io.IOException;
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
        setContentView(R.layout.activity_musicplayer);

       // musicView = (ListView)findViewById(R.id.);// mettre l'id de la list du front


        Collections.sort(
                musicList, new Comparator<Music>() {
                    @Override
                    public int compare(Music a, Music b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                }
        );
        musicView = findViewById(R.id.song_list);
        musicView.setAdapter(new ArrayAdapter<>(MusicList.this, android.R.layout.simple_list_item_1, musicList));


    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
       /* switch (item.getItemId()) {
            case R.id.action_shuffle:
                //shuffle
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    // Functions todo
    /*
    - Give List of Music
    - add to Playlist - go to activity to add playlist
    - create Playlist - go to activity to create playlist
    - play the music - go to PlayPause activity & start service
     */


}