package com.example.devmobileprojet.dataclass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.devmobileprojet.R;

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {
    private static final String TAG = "Music Adapter";
    // Attributs

    private ArrayList<Music> MusicList;
    private LayoutInflater MusicInflater;

    // methods

    public MusicAdapter(Context c, ArrayList<Music> MusicList){
        this.MusicList=MusicList;
        MusicInflater=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return MusicList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConstraintLayout MusicLayout = (ConstraintLayout)MusicInflater.inflate(
                R.layout.song, parent, false
        );
        TextView MusicView = (TextView)MusicLayout.findViewById(R.id.song_title);
        TextView ArtistView = (TextView)MusicLayout.findViewById(R.id.song_artist);
        Music currentMusic = MusicList.get(position);
        MusicView.setText(currentMusic.getTitle());
        Log.d(TAG, "getView: title of the music added to list -- " + currentMusic.getTitle() );
        MusicView.setText(currentMusic.getArtist());

        MusicLayout.getTag(position);
        return MusicLayout;
    }


}
