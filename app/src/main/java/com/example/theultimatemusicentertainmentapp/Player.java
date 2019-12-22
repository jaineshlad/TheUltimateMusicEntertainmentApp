package com.example.theultimatemusicentertainmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theultimatemusicentertainmentapp.DBHelper.DatabaseAccess;
import com.example.theultimatemusicentertainmentapp.Model.Audio;
import com.example.theultimatemusicentertainmentapp.Model.PlayList;
import com.example.theultimatemusicentertainmentapp.Model.SongsPlayList;

import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Button next,previous,pause,btnPlayList;
    TextView txtSongTitle;
    SeekBar seekBar;

    static MediaPlayer myMediaPlayer;
    int position;
    String sname;
    View vwAlertCreatPlaylist;
    EditText edtAlertCreatePlaylist;

    View vwAlertRating;
    RatingBar alertRatingBar;

    ArrayList<Audio> audioList;
    ArrayList<String> arrPlayListName;
    ArrayList<Integer> arrayPlayListId;

    Thread updateSeekbar;

    @SuppressLint({"NewApi", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);



        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        pause = findViewById(R.id.pause);
        txtSongTitle = findViewById(R.id.txtSongTitle);
        seekBar = findViewById(R.id.seekBar);
        btnPlayList = findViewById(R.id.btnPlayList);
        //to set title
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateSeekbar = new Thread()
        {
            @Override
            public void run()
            {
                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration)
                {
                    try
                    {
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if (myMediaPlayer != null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }


        //get value from intent
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        audioList =  (ArrayList) bundle.getParcelableArrayList("songs");
        sname = audioList.get(position).getTitle();
        String songName2 = i.getStringExtra("songname");
        txtSongTitle.setText(songName2);
        txtSongTitle.setSelected(true);
        position = bundle.getInt("pos",0);
        Uri uri = Uri.parse(audioList.get(position).getData());
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        //Play selected song using media player
        myMediaPlayer.start();
        seekBar.setMax(myMediaPlayer.getDuration());
        updateSeekbar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);


        //seekbar listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        //set all button click event - pause button click event
        pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seekBar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying())
                {
                    pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    myMediaPlayer.pause();
                }
                else
                {
                    pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    myMediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                //increase position to play next song
                position = ((position + 1) % audioList.size());

                Uri u = Uri.parse(audioList.get(position).getData().toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                //set name of next song
                sname = audioList.get(position).getTitle().toString();
                txtSongTitle.setText(sname);

                myMediaPlayer.start();

            }
        });

        previous.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                //decrease position to play back song
                position = ((position - 1) < 0) ? (audioList.size()-1):(position - 1);

                Uri u = Uri.parse(audioList.get(position).getData().toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                //set name of back song
                sname = audioList.get(position).getTitle().toString();
                txtSongTitle.setText(sname);

                myMediaPlayer.start();


            }
        });

        setPlayListButtonForCurrentSong();
    }


    //option menu - create by Disha
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionamenu,menu);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isAdmin = preferences.getBoolean("isAdmin",false);

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


    //click event for menu option
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed(); return true;
            //break;
            case R.id.mnuCreatePlaylist:
                showViewAlertDialog();
                return true;
            //break;
            case R.id.mnuViewPlaylist:
                openViewPlayListScreen();
                return true;
            //break;
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


    //method to show all audience only admin can access this method
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

    public void showMesage(String title, String Message)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    //show create new plalist view dialog
    public  void showViewAlertDialog()
    {
        vwAlertCreatPlaylist = View.inflate(this, R.layout.myalertplaylist, null);
        edtAlertCreatePlaylist = (EditText) vwAlertCreatPlaylist.findViewById(R.id.edtPlaylistTitle);


        AlertDialog.Builder builder = new AlertDialog.Builder(Player.this);
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
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //show rating dialog view
    public  void showRatingAlertDialog(){
        vwAlertRating = View.inflate(this, R.layout.ratingalertdalog, null);
        //edtAlertCreatePlaylist = (EditText) vwAlertCreatPlaylist.findViewById(R.id.edtPlaylistTitle);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String CurrentUserID = preferences.getString("CurrentUserID", "");
        final String CurrentUserName = preferences.getString("CurrentUserName", "");


        alertRatingBar = (RatingBar) vwAlertRating.findViewById(R.id.alertViewRatingBar);
        alertRatingBar.setNumStars(5);
        AlertDialog.Builder builder = new AlertDialog.Builder(Player.this);
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
                        Toast.makeText(Player.this,"Value not inserted",Toast.LENGTH_SHORT).show();
                    }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //button with heart symbol---this will be used to add any songs to playlist and remove current song from playlist
    //BUTTON CLICK
    public void btnAddCurrentSongToPlayList(View v){

        String currentSongID = audioList.get(position).getId();
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        SongsPlayList currentSongInPlayList = da.getSongFromPlayListBySongId(currentSongID);
        da.close();
        if(currentSongInPlayList != null){
            if(currentSongInPlayList.getSongId() != null){
                //remove
                removeCurrentSongFromPlayList(currentSongID);
            }else {
                openCreateNewPlayListView();
            }
        }
        else {
            openCreateNewPlayListView();
        }





    }

    //HELPER METHOD
    //this method will show all playlist screen with current playlist available in user
    private void openViewPlayListScreen(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String CurrentUserName = preferences.getString("CurrentUserName", "");


        Intent intent = new Intent(this, ViewPlayList.class);
        intent.putExtra("arrAudioList",audioList);
        intent.putExtra("CurrentUserName",CurrentUserName);
        startActivity(intent);

    }

    private void openCreateNewPlayListView(){
        arrPlayListName = getAllPlaylistName();
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Player.this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select PlayList:-");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Player.this, android.R.layout.select_dialog_singlechoice);

        for (String a: arrPlayListName)
        {
            arrayAdapter.add(a);
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strSelectedPlaylistId = String.valueOf(arrayPlayListId.get(which));
                String currentSongID = audioList.get(position).getId();
                insertSongToPlaylistDb(strSelectedPlaylistId,currentSongID);
            }
        });
        builderSingle.show();
    }


    //get all play list - for current user
    public ArrayList<String> getAllPlaylistName()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String CurrentUserName = preferences.getString("CurrentUserName", "");
        ArrayList<String> arrplaylist = new ArrayList<>();
        ArrayList<Integer> arrplaylistID = new ArrayList<>();
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        Cursor crPlaylist = da.getPlayList(CurrentUserName);

        //arrayPlayListId.clear();
        //arrayPlayListId.remove(arrayPlayListId);
        //crBodyParts.moveToFirst();
        while(crPlaylist.moveToNext()) {
            Integer id = crPlaylist.getInt(0);   //0 is the number of id column in your database table
            String name = crPlaylist.getString(1);
            //PlayList play = new PlayList(id,name);
            arrplaylist.add(name);
            arrplaylistID.add(id);
        }
        arrayPlayListId = arrplaylistID;
        return arrplaylist;
    }


    //DB METHODS
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
            Toast.makeText(Player.this,"Value not inserted",Toast.LENGTH_SHORT).show();
        }
        else{
        }

    }
    private void insertSongToPlaylistDb(String playlistid,String songid)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String CurrentUserName = preferences.getString("CurrentUserName", "");

        String strQuery = "INSERT INTO songplaylist (playlistid,songid,useremail) VALUES ('"+playlistid+"','"+songid+"','"+CurrentUserName+"')";
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        int resultFlag = da.insertUserRecordToDb(strQuery);
        da.close();
        if(resultFlag == 0){
            Toast.makeText(Player.this,"Song Not added to playlist",Toast.LENGTH_SHORT).show();
        }
        else {
            btnPlayList.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        }

    }

    private void removeCurrentSongFromPlayList(String songId){
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        Boolean resultFlag = da.removeSongFromPlayList(songId);
        if(resultFlag){
            btnPlayList.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unfavorite));
            Toast.makeText(Player.this,"Song removed from playlist",Toast.LENGTH_SHORT).show();
        }else{
            btnPlayList.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite));
            Toast.makeText(Player.this,"Song Remove failed",Toast.LENGTH_SHORT).show();
        }
        da.close();
    }

    private void setPlayListButtonForCurrentSong(){
        DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
        da.open();
        String currentSongID = audioList.get(position).getId();
        SongsPlayList currentuser = da.getSongFromPlayListBySongId(currentSongID);

        if(currentuser != null){
            if(currentuser.getSongId() != null){
                btnPlayList.setBackgroundResource(R.drawable.ic_favorite);
            }else{
                btnPlayList.setBackgroundResource(R.drawable.ic_unfavorite);
            }

        }
        else{
            btnPlayList.setBackgroundResource(R.drawable.ic_unfavorite);
        }
        da.close();
    }
}
