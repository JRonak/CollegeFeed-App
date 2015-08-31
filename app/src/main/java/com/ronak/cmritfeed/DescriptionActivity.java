package com.ronak.cmritfeed;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ronak.cmritfeed.Api.Const;
import com.ronak.cmritfeed.database.DbEventHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ronak on 13/8/15.
 */
public class DescriptionActivity extends AppCompatActivity  {
    TextView eventName, location, time, attending, description;
    Button attend;
    Event event;
    boolean setRemainder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_APPEND);
        setRemainder = sharedPreferences.getBoolean("eventNotify",true);
        setContentView(R.layout.event);
        ActionBar actionBar = getActionBar();
        eventName = (TextView) findViewById(R.id.eventNameTv);
        description = (TextView) findViewById(R.id.eventDescTv);
        location = (TextView) findViewById(R.id.eventLocationTv);
        time = (TextView) findViewById(R.id.eventTimeTv);
        attending = (TextView) findViewById(R.id.attendCountTv);
        attend = (Button)findViewById(R.id.attendingButton);
        event = (Event) getIntent().getSerializableExtra("event");
        Date date = event.getDate();
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.dark));
        getSupportActionBar().setTitle(event.getClub());
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date);
        String year = (String) android.text.format.DateFormat.format("yyyy", date);
        String day = (String) android.text.format.DateFormat.format("dd", date);
        time.setText(day+" "+stringMonth+" "+year);
        eventName.setText(event.getTitle());
        description.setText(event.getDescription());
        location.setText(event.getLocation());
        handleAttendee();
        handleAttend();
        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upvote upvote = new Upvote();
                upvote.execute();
            }
        });
    }

    void setupRemainder(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this,Notifier.class);
        intent.putExtra("type",Notifier.REDIRECT_EVENT);
        intent.putExtra("event", event);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getDate().getTime());
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, month, date, 8, 00);
        Log.e("Description",calendar.getTime().toString());
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    void handleAttendee(){
        if(event.getAttendcount()==0){
            attending.setText("Its Lonely here");
        }else{
            attending.setText(String.valueOf(event.getAttendcount())+" Attendees");
        }
    }

    void handleAttend(){

        if(event.getAttending()==1){
            attend.setText("Voted!, Thank you");
            attend.setEnabled(false);
        }else{
            attend.setText("Click here to vote");
        }
    }

    class Upvote extends AsyncTask<String,Boolean,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            attend.setText("Voting, Please wait");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL(Const.HOST+Const.SEND_ATTENDING+String.valueOf(event.getId()));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String s = reader.readLine();
                if(s.contains("true")){
                    return true;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                DbEventHelper db = DbEventHelper.getInstance(getApplicationContext());
                db.setEventKeyAttending(event.getId());
                db.updateAttendEvent(event.getId(), event.getAttendcount() + 1);
                event.setAttending(1);
                event.setAttendcount(event.getAttendcount() + 1);
                handleAttend();
                handleAttendee();
                if(setRemainder)
                    setupRemainder();
            }else{
                attend.setText("Unable to communicate");
            }
        }
    }
}
