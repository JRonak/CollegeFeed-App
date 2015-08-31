package com.ronak.cmritfeed;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ronak on 12/8/15.
 */
public class Event implements Serializable{
    private int id;
    private String title;
    private String description;
    private String club;
    private int attendcount;
    private long lastmod;
    private long eventTime;
    private int attending;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;
    private Date date;

    public Event(){}

    public Event(int id, String title, String description, String club, long eventTime) {
        this.eventTime = eventTime;
        this.title = title;
        this.club = club;
        this.description = description;
        setDate();

    }

    public Event(int id, String title, String description,  int attendcount,long eventTime,
                 String club, long lastmod, int attending,String location) {
        this.eventTime = eventTime;
        this.id=id;
        this.title = title;
        this.club = club;
        this.description = description;
        this.lastmod=lastmod;
        this.attendcount=attendcount;
        this.attending = attending;
        this.location=location;
        setDate();
    }

    public int getAttending() {
        return attending;
    }

    public void setAttending(int attending) {
        this.attending = attending;
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

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public int getAttendcount() {
        return attendcount;
    }

    public void setAttendcount(int attendcount) {
        this.attendcount = attendcount;
    }

    public long getLastmod() {
        return lastmod;
    }

    public void setLastmod(long lastmod) {
        this.lastmod = lastmod;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public void setDate(){
        date = new Date();
        date.setTime(eventTime*1000l);
    }

    public Date getDate(){
        return this.date;
    }
}