package com.ronak.cmritfeed.Api;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import com.ronak.cmritfeed.Club;
import com.ronak.cmritfeed.Event;
import com.ronak.cmritfeed.Home;
import com.ronak.cmritfeed.database.DbEventHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ronak on 13/8/15.
 */
public class FetchService extends IntentService {

    public FetchService() {
        super(null);
    }

    ResultReceiver resultReceiver;
    JsonParser jsonParser;
    DbEventHelper db;
    ArrayList<Event> events,attends;
    ArrayList<Club> clubs;
    @Override
    protected void onHandleIntent(Intent intent) {
        db = DbEventHelper.getInstance(this);
        long lastmod = db.getRecentModEvent();
        jsonParser = new JsonParser();
        try {
            String event = Getdata(Const.HOST + Const.FETCH_EVENT + String.valueOf(lastmod));
            events = jsonParser.parseEvent(event);
            lastmod = db.getRecentModClub();
            String club = Getdata(Const.HOST + Const.FETCH_CLUB + String.valueOf(lastmod));
            clubs = jsonParser.parseClub(club);
            String attend = Getdata(Const.HOST + Const.FETCH_ATTEND);
            attends = jsonParser.parseAttend(attend);
            HandleEvent();
            HandleClub();
            HandleAttend();
            sendBroadcast(new Intent("success"));
        }catch (NullPointerException e){
            e.printStackTrace();
            sendBroadcast(new Intent("fail"));
        }
    }
    public void HandleEvent(){
        int id = db.getLatestEventId();
        Log.e("HandleEvent",String.valueOf(id));
        for(Event e:events){
            if(e.getId()>id){
                db.addEvent(e);
            }else{
                db.updateEvent(e);
            }
        }
    }

    public void HandleClub(){
        int id = db.getLatestClubId();
        for(Club c : clubs){
            if(c.getId()>id){
                db.addClub(c);
            }else{
                db.updateClub(c);
            }
        }
    }

    public void HandleAttend(){
        for(Event e:attends){
            db.updateAttendEvent(e.getId(),e.getAttendcount());
        }
    }

    public String Getdata(String u) {
        URL url = null;
        StringBuilder stringBuilder=null;
        try {
            url = new URL(u);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String s=null;
            stringBuilder = new StringBuilder();
            while ((s=bufferedReader.readLine())!=null){
                stringBuilder.append(s);
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s=null;
        try{
           s= stringBuilder.toString();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return s;
    }



}
