package com.example.yourchat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;

public class Message {
    private String author;
    private String author_id;
    private String avatar;
    private Date date;
    private String message_text;

    public String getAuthor() {
        return author;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage_text() {
        return message_text;
    }

    public Message(){}
    public Message(String avatar, String message_text, String author, Date date, String author_id) {
        this.avatar = avatar;
        this.message_text = message_text;
        this.author = author;
        this.date = date;
        this.author_id = author_id;
    }
}
