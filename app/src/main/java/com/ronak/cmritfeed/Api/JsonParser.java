package com.ronak.cmritfeed.Api;

import android.util.JsonReader;
import android.util.Log;

import com.ronak.cmritfeed.Club;
import com.ronak.cmritfeed.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ronak on 13/8/15.
 */
public class JsonParser {
    public JsonParser(){}

    private ArrayList<Event> events;

    public ArrayList<Event> parseEvent(String s){
        try {
            String json = s.substring(s.indexOf("{"),s.lastIndexOf("}")+1);
            json = json.replace("\\","");
            JSONObject jsonObject =(JSONObject) new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Events");
            JSONObject obj;
            events = new ArrayList<Event>();
            for(int i=0; i<jsonArray.length(); i++){
                obj = jsonArray.getJSONObject(i);
                Event event = new Event();
                event.setId(obj.getInt(Const.ID));
                event.setTitle(obj.getString(Const.TITLE));
                event.setDescription(obj.getString(Const.DESCRIPTION));
                event.setAttendcount(obj.getInt(Const.ATTENDCOUNT));
                event.setEventTime(obj.getLong(Const.TIME));
                event.setClub(obj.getString(Const.CLUB));
                event.setLastmod(obj.getLong(Const.LASTMOD));
                event.setLocation(obj.getString(Const.LOCATION));
                event.setAttending(0);
                events.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }

    public ArrayList<Club> parseClub(String s){
        String json = s.substring(s.indexOf("{"),s.lastIndexOf("}")+1).replace("\\","");
        ArrayList<Club> clubs = new ArrayList<Club>();
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("Clubs");
            JSONObject jsonObject1;
            for(int i=0; i<jsonArray.length(); i++){
                jsonObject1 = jsonArray.getJSONObject(i);
                Club club = new Club();
                club.setId(jsonObject1.getInt(Const.ID));
                club.setTitle(jsonObject1.getString(Const.TITLE));
                club.setDescription(jsonObject1.getString(Const.DESCRIPTION));
                club.setContact(jsonObject1.getString(Const.CONTACT));
                club.setPresident(jsonObject1.getString(Const.PRESIDENT));
                club.setLastmod(jsonObject1.getLong(Const.LASTMOD));
                clubs.add(club);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return clubs;
    }

    public ArrayList<Event> parseAttend(String s){
        try {
            String json = s.substring(s.indexOf("["),s.lastIndexOf("]")+1);
            json = json.replace("\\","");
            JSONArray jsonArray = new JSONArray(json);
            JSONObject obj;
            events = new ArrayList<Event>();
            for(int i=0; i<jsonArray.length(); i++){
                obj = jsonArray.getJSONObject(i);
                Event event = new Event();
                event.setId(obj.getInt(Const.ID));
                event.setAttendcount(obj.getInt(Const.ATTENDCOUNT));
                events.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }

}
