package com.example.theultimatemusicentertainmentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.theultimatemusicentertainmentapp.Adapters.SongAdapter;
import com.example.theultimatemusicentertainmentapp.Model.Audio;

import java.util.ArrayList;

public class display_Playlist_songs_list extends AppCompatActivity implements  SongAdapter.OnSongClickListener{



    ArrayList<Audio> finalPlaylistAudio;
    RecyclerView rcvSong;
    SongAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get songs list which is passed oin intent as value
        setContentView(R.layout.activity_display__playlist_songs_list);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        finalPlaylistAudio =  (ArrayList) bundle.getParcelableArrayList("songs");
        rcvSong = (RecyclerView) findViewById(R.id.rcvSongList);
        rcvSong.setHasFixedSize(true);
        rcvSong.setLayoutManager(new LinearLayoutManager(this));
        sAdapter = new SongAdapter(this,finalPlaylistAudio,this);
        rcvSong.setAdapter(sAdapter);
    }


    //onclick event to play song which user select from the list from playlist display screen

    @Override
    public void onSongClick(int position) {
        String songName = finalPlaylistAudio.get(position).getTitle();
        //sending array list "mySongs" and song name
        startActivity(new Intent(getApplicationContext(),Player.class)
                .putExtra("songs",finalPlaylistAudio)
                .putExtra("songname",songName)
                .putExtra("pos",position));
    }

}
