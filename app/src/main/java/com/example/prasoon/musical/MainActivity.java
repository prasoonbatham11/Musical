package com.example.prasoon.musical;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static MediaPlayer mediaPlayer;
    static ArrayList<Song> songs;
    LinearLayout linearLayout;
    ImageButton playpause;
    TextView songname;
    int current = 0;

    static boolean isRep = false, isShuff = false;

    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer.isPlaying()) {
            playpause.setImageResource(R.drawable.pause);
        }
        else playpause.setImageResource(R.drawable.play);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        playpause = (ImageButton) findViewById(R.id.playpause);
        songname = (TextView) findViewById(R.id.songname);
        mediaPlayer = new MediaPlayer();
        listView = (ListView) findViewById(R.id.listview);
        songs = FetchSongs.fetchSongs(this);
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getSongName().compareTo(o2.getSongName());
            }
        });

        SongAdapter songAdapter = new SongAdapter(this, songs);
        listView.setAdapter(songAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current = position;
                if(mediaPlayer!=null)
                playSong(current);
                playpause.setImageResource(R.drawable.pause);
                /*Intent i = new Intent(MainActivity.this, Player.class);
                i.putExtra("index", position+"");
                startActivity(i);*/
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Player.class);
                i.putExtra("index", current+"");
                startActivity(i);
            }
        });

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.pause();
                        playpause.setImageResource(R.drawable.play);
                    }
                }
                else {
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.start();
                        playpause.setImageResource(R.drawable.pause);
                    }
                }
            }
        });



    }
    public void playSong(int songIndex)
    {
        try {
            songname.setText(songs.get(songIndex).getSongName());
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.get(songIndex).getDatapath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e) {

        }
    }
}
