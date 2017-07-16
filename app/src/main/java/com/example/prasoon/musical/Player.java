package com.example.prasoon.musical;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Player extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    MediaPlayer mediaPlayer;
    TextView songname;
    ImageView albumart;
    ImageButton shuffle, repeat, prev, next, play, seekfwd, seekbwd;
    SeekBar seekBar;
    String index;
    int idx;
    ArrayList<Song> songs;
    Utility utility;
    boolean isRepeat, isShuffle;
    static final int SEEKFWD = 5000, SEEKBWD = 5000;

    Handler handler = new Handler();

    ArrayList<Integer> album = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        init();

        albumart.setImageResource(returnRandArt());

        seekBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);

        if(mediaPlayer.isPlaying()) {
            songname.setText(songs.get(idx).getSongName());
            updateSeekBar();
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.pause();
                        play.setImageResource(R.drawable.play);
                    }
                }
                else {
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.start();
                        play.setImageResource(R.drawable.pause);
                    }
                }
            }
        });

        seekfwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = mediaPlayer.getCurrentPosition();
                int topos = Math.min(current+SEEKFWD, mediaPlayer.getDuration());
                mediaPlayer.seekTo(topos);
            }
        });

        seekbwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = mediaPlayer.getCurrentPosition();
                int topos = Math.max(current-SEEKBWD, 0);
                mediaPlayer.seekTo(topos);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isShuff) {
                    Random random = new Random();
                    idx = random.nextInt((songs.size()-1)-0+1);
                    playSong(idx);
                }
                else {
                    idx = (idx + 1) % songs.size();
                    playSong(idx);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isShuff) {
                    Random random = new Random();
                    idx = random.nextInt((songs.size()-1)-0+1);
                    playSong(idx);
                }
                else {
                    idx = (idx - 1 + songs.size()) % songs.size();
                    playSong(idx);
                }
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isRep) {
                    MainActivity.isRep = false;
                    repeat.setImageResource(R.drawable.repeat);
                }
                else {
                    MainActivity.isRep = true;
                    MainActivity.isShuff = false;
                    repeat.setImageResource(R.drawable.repeat_pressed);
                    shuffle.setImageResource(R.drawable.shuffle);
                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isShuff) {
                    MainActivity.isShuff = false;
                    shuffle.setImageResource(R.drawable.shuffle);
                }
                else {
                    MainActivity.isShuff = true;
                    MainActivity.isRep = false;
                    shuffle.setImageResource(R.drawable.shuffle_pressed);
                    repeat.setImageResource(R.drawable.repeat);
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

            seekBar.setProgress(0);
            seekBar.setMax(100);
            updateSeekBar();
        }
        catch (Exception e) {

        }
    }

    public void updateSeekBar()
    {
        handler.postDelayed(updateTimeTask, 100);
    }

    public Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            long total = mediaPlayer.getDuration();
            long current = mediaPlayer.getCurrentPosition();

            int progress = (int) (utility.getProgressPercentage(current, total));

            seekBar.setProgress(progress);

            handler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateTimeTask);
        int total = mediaPlayer.getDuration();
        int current = utility.progressToTimer(seekBar.getProgress(),
                total);

        mediaPlayer.seekTo(current);
        updateSeekBar();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(MainActivity.isRep) {
            playSong(idx);
        }
        else if(MainActivity.isShuff) {
            Random random = new Random();
            idx = random.nextInt((songs.size()-1)-0+1);
            playSong(idx);
        }
        else {
            idx = (idx+1)%songs.size();
            playSong(idx);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void init() {
        songname = (TextView) findViewById(R.id.songname);
        isRepeat = MainActivity.isRep;
        isShuffle = MainActivity.isShuff;
        utility = new Utility();
        index = getIntent().getExtras().getString("index");
        songs = MainActivity.songs;
        idx = Integer.parseInt(index);
        mediaPlayer = MainActivity.mediaPlayer;
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        repeat = (ImageButton) findViewById(R.id.repeat);
        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);
        play = (ImageButton) findViewById(R.id.play);
        seekfwd = (ImageButton) findViewById(R.id.seekfwd);
        seekbwd = (ImageButton) findViewById(R.id.seekbwd);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        albumart = (ImageView) findViewById(R.id.songimage);

        if(MainActivity.isShuff) {
            shuffle.setImageResource(R.drawable.shuffle_pressed);
        }
        else {
            shuffle.setImageResource(R.drawable.shuffle);
        }

        if(MainActivity.isRep) {
            repeat.setImageResource(R.drawable.repeat_pressed);
        }
        else {
            repeat.setImageResource(R.drawable.repeat);
        }
        if(mediaPlayer.isPlaying()) {
            play.setImageResource(R.drawable.pause);
        }
        else play.setImageResource(R.drawable.play);

        int i;
        for(i=0;i<=8;i++)
        {
            album.add(this.getResources()
                    .getIdentifier(
                            "a"+i, "drawable", this.getPackageName()
                    ));
        }
    }

    public int returnRandArt()
    {
        int n = album.size();
        Random r = new Random();
        return album.get(r.nextInt(n));
    }

}