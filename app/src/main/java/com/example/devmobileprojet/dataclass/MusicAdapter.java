package com.example.devmobileprojet.dataclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.devmobileprojet.R;

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {
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
        LinearLayout MusicLayout = (LinearLayout)MusicInflater.inflate(
                R.layout.song, parent, false
        );
        //TextView MusicView = (TextView)MusicLayout.findViewById(); // TODO
        //TextView ArtistView = (TextView)MusicLayout.findViewById(); // TODO
        Music currentMusic = MusicList.get(position);
        //MusicView.setText(currentMusic.getTitle());
        //MusicView.setText(currentMusic.getArtist());

        MusicLayout.getTag(position);
        return MusicLayout;
    }
}
