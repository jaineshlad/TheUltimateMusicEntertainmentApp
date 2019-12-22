package com.example.theultimatemusicentertainmentapp.Model;

public class User {

    private String UserId;
    private String FirstName;
    private String LastName;
    private String AppDateTime;
    private String Rating;
    private String Email;
    private String Password;

    public User()
    {
    }
    public User(String userid,String firstname,String lastname,String appdatetime,String rating,String email,String password)
    {
        this.UserId = userid;
        this.FirstName = firstname;
        this.LastName = lastname;
        this.AppDateTime = appdatetime;
        this.Rating = rating;
        this.Email = email;
        this.Password = password;
    }

    //GET

    public String getUserId() {
        return UserId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getAppDateTime() {
        return AppDateTime;
    }

    public String getRating() {
        return Rating;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }
    //SET


    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setAppDateTime(String appDateTime) {
        AppDateTime = appDateTime;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
