package com.example.theultimatemusicentertainmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.theultimatemusicentertainmentapp.Adapters.SongAdapter;
import com.example.theultimatemusicentertainmentapp.DBHelper.DatabaseAccess;
import com.example.theultimatemusicentertainmentapp.Model.Audio;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class DisplaySongList extends AppCompatActivity implements  SongAdapter.OnSongClickListener {


    ArrayList<Audio> audioList;
    RecyclerView rcvSong;
    SongAdapter sAdapter;
    View vwAlertRating;
    RatingBar alertRatingBar;
    View vwAlertCreatPlaylist;
    EditText edtAlertCreatePlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_song_list);
        runtimePermission();
    }


    //check user run time permission-- to access storage
    public void runtimePermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    //Display all song list in recycler view
    void display()
    {
        loadAudio();
        rcvSong = (RecyclerView) findViewById(R.id.rcvSongList);
        rcvSong.setHasFixedSize(true);
        rcvSong.setLayoutManager(new LinearLayoutManager(this));
        sAdapter = new SongAdapter(this,audioList,this);
        rcvSong.setAdapter(sAdapter);
    }


    //get all songs from device and set for recycler view...
    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String ID = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

                // Save to audioList
                audioList.add(new Audio(data, title, album, artist,ID));
            }
        }
        cursor.close();
    }

// Created by Disha
    //option menu delegate method this will be used to set which menu to show
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionamenu,menu);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isAdmin = preferences.getBoolean("isAdmin",false);

        //hide view audience menu is user is not admin
        if(isAdmin){
            MenuItem item = menu.findItem(R.id.mnuViewAudience);
            item.setVisible(true);
        }
        else{
            MenuItem item = menu.findItem(R.id.mnuViewAudience);
            item.setVisible(false);
        }
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed(); return true;
            case R.id.mnuCreatePlaylist:
                showViewAlertDialog();
                return true;
            case R.id.mnuViewPlaylist:
                openViewPlayListScreen();
            return true;
            case R.id.mnuRating:
                showRatingAlertDialog();
                return  true;
            case R.id.mnuViewAudience:
                openViewAudience();
                return true;
            case R.id.mnuLogOut:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return  super.onOptionsItemSelected(item);

        }
        // return super.onOptionsItemSelected(item);
    }



    //Show rating dialog view for giving app ratinng
    public  void showRatingAlertDialog(){
        vwAlertRating = View.inflate(this, R.layout.ratingalertdalog, null);
        //edtAlertCreatePlaylist = (EditText) vwAlertCreatPlaylist.findViewById(R.id.edtPlaylistTitle);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String CurrentUserID = preferences.getString("CurrentUserID", "");
        final String CurrentUserName = preferences.getString("CurrentUserName", "");


        alertRatingBar = (RatingBar) vwAlertRating.findViewById(R.id.alertViewRatingBar);
        alertRatingBar.setNumStars(5);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DisplaySongList.this);
        builder.setTitle("Choose Course");

        builder.setView(vwAlertRating)

                .setCancelable(false)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    //Positive button click event
                    //consider as Cancel button click
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Rate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int numOfStar = alertRatingBar.getNumStars();
                        Float ratingValue = alertRatingBar.getRating();
                        String strQuery = "update user set rating = " + "'"+ratingValue.toString()+"'" + " where email = " + "'"+CurrentUserName+"'";
                        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
                        da.open();
                        int resultFlag = da.insertUserRecordToDb(strQuery);
                        da.close();
                        if(resultFlag == 0){
                            Toast.makeText(DisplaySongList.this,"Value not inserted",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    //this method will be usefull in showing alert dialog for create new playlist .
    // which will allow them to insert new play list
    public  void showViewAlertDialog()
    {
        vwAlertCreatPlaylist = View.inflate(this, R.layout.myalertplaylist, null);
        edtAlertCreatePlaylist = (EditText) vwAlertCreatPlaylist.findViewById(R.id.edtPlaylistTitle);


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DisplaySongList.this);
        builder.setTitle("Enter playlist name");

        builder.setView(vwAlertCreatPlaylist)

                .setCancelable(false)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    //Positive button click event
                    //consider as Cancel button click
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(edtAlertCreatePlaylist.getText().length() > 0)
                        {
                            insertNewPlaylistToDb(edtAlertCreatePlaylist.getText().toString());
                        }

                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
}


    //method will navigate to view all playlist screen for current user
    private void openViewPlayListScreen(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String CurrentUserName = preferences.getString("CurrentUserName", "");
        Intent intent = new Intent(this, ViewPlayList.class);
        intent.putExtra("arrAudioList",audioList);
        intent.putExtra("CurrentUserName",CurrentUserName);
        startActivity(intent);

    }



    //Method to show all user details in dialog view-- only admin can view this options
    private void openViewAudience(){
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        Cursor crPlaylist = da.getAllAudience();

        if(crPlaylist.getCount() == 0) {
            // show message
            showMesage("Error","Nothing found");
            return;
        }
        else if (crPlaylist.getCount() <0){
            showMesage("Error","Nothing found");
            return;
        }
        else{
            StringBuffer buffer = new StringBuffer();
            while (crPlaylist.moveToNext()) {
                buffer.append("UserId :"+ crPlaylist.getString(0)+"\n");
                buffer.append("FirstName :"+ crPlaylist.getString(1)+"\n");
                buffer.append("LastName :"+ crPlaylist.getString(2)+"\n");
                buffer.append("App Used :"+ crPlaylist.getString(3)+"\n");
                buffer.append("Rating :"+ crPlaylist.getString(4)+"\n");
                buffer.append("Email:"+ crPlaylist.getString(5)+"\n\n");
            }
            // Show all data
            showMesage("Audience Details",buffer.toString());
        }
    }

    //show altert dialog with value passes
    public void showMesage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }



    //helper method
    //play song on click in recycler view
    @Override
    public void onSongClick(int position) {
        String songName = audioList.get(position).getTitle();

        //sending array list "mySongs" and song name
        startActivity(new Intent(getApplicationContext(),Player.class)
                .putExtra("songs",audioList)
                .putExtra("songname",songName)
                .putExtra("pos",position));
    }


    //DB METHOD

    //insert new playlist name to database table
    private void insertNewPlaylistToDb(String playListName)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String CurrentUserName = preferences.getString("CurrentUserName", "");

        String strQuery = "Insert into playlist " +
                "(playlistname,useremail) values " +
                "('"+playListName+"','"+CurrentUserName+"')";
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        int resultFlag = da.insertUserRecordToDb(strQuery);
        da.close();
        if(resultFlag == 0){
            Toast.makeText(DisplaySongList.this,"Value not inserted",Toast.LENGTH_SHORT).show();
        }
        else{
        }

    }

}
