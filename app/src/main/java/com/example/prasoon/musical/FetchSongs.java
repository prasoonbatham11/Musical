package com.example.prasoon.musical;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by prasoon on 16/7/17.
 */

public class FetchSongs {
    static public ArrayList<Song> fetchSongs(Context context)
    {
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(musicUri, null, null, null, null);

        ArrayList<Song> songs = new ArrayList<>();

        if(cursor!=null && cursor.moveToFirst()) {
            do {
                Song newSong = new Song();
                newSong.setSongId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.Media._ID
                ))));
                newSong.setAlbumId(Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(
                                MediaStore.Audio.Media.ALBUM_ID
                        ))
                ));
                newSong.setArtistId(Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(
                                MediaStore.Audio.Media.ARTIST_ID
                        ))
                ));
                newSong.setAlbumName(cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.Media.ALBUM
                )));
                newSong.setArtistName(cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.Media.ARTIST
                )));
                newSong.setSongName(cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.Media.TITLE
                )));
                double dms = Double.parseDouble(cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.Media.DURATION
                )));
                dms = (dms / 1000.0) / 60.0;
                dms = new BigDecimal(Double.toString(dms)).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                newSong.setDuration(dms);

                newSong.setDatapath(cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.Media.DATA
                )));

                songs.add(newSong);
            }while (cursor.moveToNext());
        }
        return songs;
    }
}
