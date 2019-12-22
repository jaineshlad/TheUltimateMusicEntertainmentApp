package com.example.theultimatemusicentertainmentapp.Model;

public class SongsPlayList {

    private String PlayListId;
    private String SongId;
    private  String UserEmail;



    public  SongsPlayList(){}
    public SongsPlayList(String playlistid,String songid,String UserEmail)
    {
        this.PlayListId = playlistid;
        this.SongId = songid;
        this.UserEmail = UserEmail;
    }
    public String getPlayListId() {
        return PlayListId;
    }

    public String getSongId() {
        return SongId;
    }

    public void setPlayListId(String playListId) {
        PlayListId = playListId;
    }

    public void setSongId(String songId) {
        SongId = songId;
    }


    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }
}
