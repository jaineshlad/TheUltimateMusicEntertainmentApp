package com.example.theultimatemusicentertainmentapp.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.theultimatemusicentertainmentapp.Model.PlayList;
import com.example.theultimatemusicentertainmentapp.Model.SongsPlayList;
import com.example.theultimatemusicentertainmentapp.Model.User;

import java.util.ArrayList;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    private  DatabaseAccess (Context context)
    {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public  static  DatabaseAccess getInstance(Context context){

        if (instance == null){
            instance = new DatabaseAccess(context);
        }
        return  instance;
    }


    //open database connection
    public  void open(){
        this.db = openHelper.getWritableDatabase();
    }
    //close database connection
    public  void close()
    {
        if (db!= null)
        {
            this.db.close();
        }
    }


    //INSERT user record in table
    public  int insertUserRecordToDb(String strQuery)
    {
        int resultFlag = 1;
        try
        {
            db.execSQL(strQuery);
            return resultFlag;
        }
        catch (Exception e)
        {
            resultFlag = 0;
            return resultFlag;
            //Log.d("0",e.getMessage().toString());
        }
    }

    //Get all playlist by user email address
    public Cursor getPlayList(String useremail)
    {
        Cursor cursor = db.query("playlist",
                new String[] {"playlistid", "playlistname","useremail"},
                "useremail" + "=?",
                new String[] {String.valueOf(useremail)}, null, null, null, null);
        return  cursor;
    }


    //get all songs from playlist which is selected for any specific user
    public ArrayList<SongsPlayList> getSongFromPlayListByPlayListId(String playlistId) {
        ArrayList<SongsPlayList> arr = new ArrayList<SongsPlayList>();
        SongsPlayList song = new SongsPlayList();
        Cursor cursor = db.query("songplaylist",
                new String[] {"playlistid", "songid","useremail"},
                "playlistid" + "=?",
                new String[] {String.valueOf(playlistId)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                song = new SongsPlayList(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)


                );
                arr.add(song);
            }

        }
        // return contact
        cursor.close();
        db.close();
        return arr;
    }


    //check if user is exists or not
    //get user detail by email
    public User getUserByEmail(String stremail) {
        User user = new User();
        Cursor cursor = db.query("user",
                new String[] {"userid", "firstname", "lastname", "appdatetime","rating","email","password"},
                "email" + "=?",
                new String[] {String.valueOf(stremail)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                user = new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
            }
        }
        cursor.close();
        db.close();
        return user;
    }
    // get all audience detail for admin only
    public Cursor getAllAudience()
    {
        Cursor c = db.rawQuery("select * from user",null);
        return  c;
    }

//using songid fetching songs from playlist
    public SongsPlayList getSongFromPlayListBySongId(String songid) {
        SongsPlayList song = new SongsPlayList();
        Cursor cursor = db.query("songplaylist",
                new String[] {"playlistid", "songid","useremail"},
                "songid" + "=?",
                new String[] {String.valueOf(songid)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                song = new SongsPlayList(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
            }

        }
        // return contact
        cursor.close();
        db.close();
        return song;
    }
//to remove songs from playlist
    public boolean removeSongFromPlayList(String id) {
       // SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("songplaylist", "songid" + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
        if(i <= 0){
            return false;
        }
        else{
            return true;
        }
    }

    //getting playlist using email address
    public PlayList getPlayListByUserEmail(String useremail) {
        PlayList song = new PlayList();
        Cursor cursor = db.query("playlist",
                new String[] {"playlistid", "playlistname","useremail"},
                "useremail" + "=?",
                new String[] {String.valueOf(useremail)}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                song = new PlayList(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
            }

        }

        cursor.close();
        db.close();
        return song;
    }
}
