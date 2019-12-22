package com.example.theultimatemusicentertainmentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.theultimatemusicentertainmentapp.Adapters.PlayListAdapter;
import com.example.theultimatemusicentertainmentapp.Adapters.SongAdapter;
import com.example.theultimatemusicentertainmentapp.DBHelper.DatabaseAccess;
import com.example.theultimatemusicentertainmentapp.Model.Audio;
import com.example.theultimatemusicentertainmentapp.Model.PlayList;
import com.example.theultimatemusicentertainmentapp.Model.SongsPlayList;

import java.util.ArrayList;

public class ViewPlayList extends AppCompatActivity implements  PlayListAdapter.OnSongClickListener{


    RecyclerView rcvSong;
    PlayListAdapter pAdapter;
    ArrayList<Audio> audioList;
    ArrayList<SongsPlayList> arrCurrentPlayListAudio;
    ArrayList<Audio> finalPlaylistAudio;
    String CurrentUserEmail = "";
    ArrayList<PlayList> arrAllCurrentPlayList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_play_list);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        audioList =  (ArrayList) bundle.getParcelableArrayList("arrAudioList");
        CurrentUserEmail = i.getStringExtra("CurrentUserName");
        display();
    }

    void display()
    {
        loadAudio();
        rcvSong = (RecyclerView) findViewById(R.id.rcvPlayList);
        rcvSong.setHasFixedSize(true);
        rcvSong.setLayoutManager(new LinearLayoutManager(this));
        pAdapter = new PlayListAdapter(this,arrAllCurrentPlayList,this);
        rcvSong.setAdapter(pAdapter);
    }

    private void loadAudio()
    {
        arrAllCurrentPlayList = getAllCurrentPlayListOfUser(CurrentUserEmail);
    }

    @Override
    public void onSongClick(int position) {

        Toast.makeText(ViewPlayList.this,"POSITION" + position,Toast.LENGTH_SHORT).show();

        ArrayList<Audio> aList;

        Integer CurrentPlayListID = arrAllCurrentPlayList.get(position).getPlayListId();

        arrCurrentPlayListAudio = getAllSongFromCurrentPlayList(CurrentPlayListID.toString());


        //get songs from array which is used to match current user playlist songs and display in list.
        //
        ArrayList<Audio> arr = new ArrayList<Audio>();
        for (int i = 0; i < arrCurrentPlayListAudio.size(); i++)
        {
           SongsPlayList p = arrCurrentPlayListAudio.get(i);

            for (int j = 0; j < audioList.size(); j++)
            {
                //compare id value from main array and current user songs id from any specific playlisyt
                if(p.getSongId().equals(audioList.get(j).getId())){
                    Audio a = audioList.get(j);
                    arr.add(a);
                    //finalPlaylistAudio.add(a);
                }
            }
           Toast.makeText(ViewPlayList.this,"",Toast.LENGTH_SHORT).show();

        }
        finalPlaylistAudio = arr;
        Toast.makeText(ViewPlayList.this,"",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),display_Playlist_songs_list.class)
                .putExtra("songs",finalPlaylistAudio));
    }



    //DB METHODS

    private ArrayList<PlayList> getAllCurrentPlayListOfUser(String useremail)
    {
        ArrayList<PlayList> array = new ArrayList<PlayList>();
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        Cursor crPlaylist = da.getPlayList(useremail);

        while(crPlaylist.moveToNext()) {
            Integer playlistid = crPlaylist.getInt(0);   //0 is the number of id column in your database table
            String playlistname = crPlaylist.getString(1);
            String strUserEmail = crPlaylist.getString(2);
            PlayList p = new PlayList(playlistid,playlistname,strUserEmail);
            array.add(p);

        }
        return   array;
    }



    private  ArrayList<SongsPlayList>  getAllSongFromCurrentPlayList(String playlistid){
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        ArrayList<SongsPlayList> songlist = da.getSongFromPlayListByPlayListId(playlistid);
        da.close();
        return songlist;
    }




}
