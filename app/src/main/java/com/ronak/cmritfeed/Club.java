package com.ronak.cmritfeed;

import java.io.Serializable;

/**
 * Created by ronak on 12/8/15.
 */
public class Club implements Serializable{

    private int id;
    private String title;
    private String description;
    private String president;
    private String contact;
    private long lastmod;

    public Club(){}

    public Club(int id,String title, String description, String president, String contact, long lastmod ){
        this.id = id;
        this.title = title;
        this.description = description;
        this.president = president;
        this.contact = contact;
        this.lastmod = lastmod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public long getLastmod() {
        return lastmod;
    }

    public void setLastmod(long lastmod) {
        this.lastmod = lastmod;
    }



}


