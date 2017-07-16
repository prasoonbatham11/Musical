package com.example.prasoon.musical;

import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by prasoon on 16/7/17.
 */

public class SongAdapter extends ArrayAdapter<Song> {
    public SongAdapter(Context context, List<Song> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View list = convertView;
        if(list==null)
        {
            list = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Song song = getItem(position);
        TextView title = (TextView) list.findViewById(R.id.title);
        TextView artist = (TextView) list.findViewById(R.id.artist);

        title.setText(song.getSongName());
        artist.setText(song.getArtistName());
        return list;
    }
}
