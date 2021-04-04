package com.example.yourchat;

import android.net.Uri;

public class Room {

    private String image_source;
    private String room_name;

    private Room(){}

    private Room(String image_source, String room_name){
        this.room_name = room_name;
        this.image_source = image_source;
    }


    public String getImage_source() {
        return image_source;
    }

    public String getRoom_name() {
        return room_name;
    }

}
