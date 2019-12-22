package com.example.theultimatemusicentertainmentapp.Model;

public class PlayList {

    private int PlayListId;
    private String PlayListName;
    private  String UserId;

    public PlayList(){

    }

    public PlayList(Integer playlistid,String playlistname,String userid){
        this.PlayListId = playlistid;
        this.PlayListName = playlistname;
        this.UserId = userid;
    }

    public int getPlayListId() {
        return PlayListId;
    }

    public String getPlayListName() {
        return PlayListName;
    }

    public void setPlayListId(int playListId) {
        PlayListId = playListId;
    }

    public void setPlayListName(String playListName) {
        PlayListName = playListName;
    }

    public String getUserId() {
        return UserId;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
}
