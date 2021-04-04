package com.example.yourchat;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.yourchat.Helpers.FirebaseHelper;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class User {

    private String email = "";
    private String fullName = "";
    private String login = "";
    private String password = "";
    private String downdloadUrl = "";


    public String getEmail() {
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login){
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getDowndloadUrl() {
        return downdloadUrl;
    }
    public void setDowndloadUrl(String url){
        this.downdloadUrl = url;
    }

}
