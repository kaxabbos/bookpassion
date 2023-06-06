package com.lib.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long bookid;
    private String username;
    private String date;
    private String comment;

    public Comments() {
    }

    public Comments(long bookid, String username, String date, String comment) {
        this.bookid = bookid;
        this.username = username;
        this.date = date;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public long getbookid() {
        return bookid;
    }

    public void setbookid(long bookid) {
        this.bookid = bookid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
