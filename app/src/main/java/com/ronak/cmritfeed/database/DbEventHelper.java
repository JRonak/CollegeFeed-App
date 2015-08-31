package com.ronak.cmritfeed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.style.TtsSpan;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.ronak.cmritfeed.Club;
import com.ronak.cmritfeed.Event;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by ronak on 12/8/15.
 */
public class DbEventHelper extends SQLiteOpenHelper {

    Context context;
    private static DbEventHelper instance=null;

    private static String DBNAME = "cmritfeed";
    private static int VERSION = 1;

    private static String EVENT_TABLE = "event";
    private static String CLUB_TABLE = "club";

    private static String EVENT_KEY_ID = "id";
    private static String EVENT_KEY_TITLE = "title";
    private static String EVENT_KEY_DESC = "description";
    private static String EVENT_KEY_ATTCOUNT = "attendcount";
    private static String EVENT_KEY_TIME = "time";
    private static String EVENT_KEY_CLUB = "club";
    private static String EVENT_KEY_MOD = "lastmod";
    private static String EVENT_KEY_ATTENDING = "attending";
    private static String EVENT_KEY_LOCATION = "location";


    private static String CLUB_KEY_ID = "id";
    private static String CLUB_KEY_TITLE = "title";
    private static String CLUB_KEY_DESC = "description";
    private static String CLUB_KEY_PRES = "president";
    private static String CLUB_KEY_CONTACT = "contact";
    private static String CLUB_KEY_MOD = "mod";


    private static String[] EVENT_KEYS = {EVENT_KEY_ID, EVENT_KEY_TITLE, EVENT_KEY_DESC,
            EVENT_KEY_ATTCOUNT, EVENT_KEY_TIME, EVENT_KEY_CLUB, EVENT_KEY_MOD,EVENT_KEY_LOCATION};
    private static String[] CLUB_KEYS = {CLUB_KEY_ID,CLUB_KEY_TITLE,CLUB_KEY_DESC,CLUB_KEY_PRES,CLUB_KEY_CONTACT,CLUB_KEY_MOD, EVENT_KEY_ATTENDING};

    private static String CREATE_EVENT = "CREATE TABLE IF NOT EXISTS " + EVENT_TABLE + " ( "
            + EVENT_KEY_ID + " INTEGER UNIQUE PRIMARY KEY," + EVENT_KEY_TITLE + " VARCHAR(30) NOT NULL,"
            + EVENT_KEY_DESC + " VARCHAR(2000) NOT NULL," + EVENT_KEY_ATTCOUNT + " INTEGER NOT NULL,"
            + EVENT_KEY_TIME + " INTEGER NOT NULL," + EVENT_KEY_CLUB + " VARCHAR(30) NOT NULL,"
            + EVENT_KEY_MOD + " INTEGER NOT NULL,"+EVENT_KEY_ATTENDING+" INTEGER NOT NULL,"+
            EVENT_KEY_LOCATION +" VARCHAR(30) NOT NULL)";
    
    private static String CREATE_CLUB = "CREATE TABLE IF NOT EXISTS "+ CLUB_TABLE + " ( "
            +CLUB_KEY_ID + " INTEGER UNIQUE PRIMARY KEY," + EVENT_KEY_TITLE + " VARCHAR(30) NOT NULL,"
            +CLUB_KEY_DESC + " VARCHAR(300) NOT NULL," + CLUB_KEY_PRES + " VARHCAR(30) NOT NULL,"
            +CLUB_KEY_CONTACT + " VARCHAR(12) NOT NULL, " + CLUB_KEY_MOD + " INTEGER NOT NULL)";


    public static synchronized DbEventHelper getInstance(Context context){
        if(instance==null){
            instance = new DbEventHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DbEventHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT);
        db.execSQL(CREATE_CLUB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        onCreate(db);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_KEY_ID, event.getId());
        contentValues.put(EVENT_KEY_TITLE, event.getTitle());
        contentValues.put(EVENT_KEY_DESC, event.getDescription());
        contentValues.put(EVENT_KEY_ATTCOUNT, event.getAttendcount());
        contentValues.put(EVENT_KEY_TIME, event.getEventTime());
        contentValues.put(EVENT_KEY_CLUB, event.getClub());
        contentValues.put(EVENT_KEY_MOD, event.getLastmod());
        contentValues.put(EVENT_KEY_ATTENDING,event.getAttending());
        contentValues.put(EVENT_KEY_LOCATION,event.getLocation());
        long status = db.insert(EVENT_TABLE, null, contentValues);
        Log.e("Db", String.valueOf(status));
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(EVENT_TABLE, EVENT_KEYS, EVENT_KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        Event event = null;
        if (cursor != null) {
            cursor.moveToFirst();
            event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                    cursor.getLong(4), cursor.getString(5), cursor.getLong(6), cursor.getInt(7),cursor.getString(8));
            cursor.close();
        }
        return event;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> list = new ArrayList<Event>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENT_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        cursor.getLong(4), cursor.getString(5), cursor.getLong(6), cursor.getInt(7),cursor.getString(8));
                list.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void deleteEventById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENT_TABLE, EVENT_KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteEventByTitle(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENT_TABLE, EVENT_KEY_TITLE + "=?", new String[]{name});
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_KEY_TITLE, event.getTitle());
        contentValues.put(EVENT_KEY_DESC, event.getDescription());
        contentValues.put(EVENT_KEY_ATTCOUNT, event.getAttendcount());
        contentValues.put(EVENT_KEY_TIME, event.getEventTime());
        contentValues.put(EVENT_KEY_CLUB, event.getClub());
        contentValues.put(EVENT_KEY_MOD, event.getLastmod());
        contentValues.put(EVENT_KEY_LOCATION, event.getLocation());
        db.update(EVENT_TABLE, contentValues, EVENT_KEY_ID + "=?", new String[]{String.valueOf(event.getId())});
    }

    public long getRecentModEvent(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+EVENT_TABLE+" ORDER BY "+EVENT_KEY_MOD+" DESC",null);
        if(cursor.moveToFirst())
            return cursor.getLong(6);
        return -1;
    }

    public ArrayList<Event> getUpcomingEvent(){
        ArrayList<Event> list = new ArrayList<Event>();
        SQLiteDatabase db =this.getReadableDatabase();
        long time = System.currentTimeMillis()/1000;
        Cursor cursor = db.rawQuery("SELECT * FROM "+EVENT_TABLE+" WHERE "+EVENT_KEY_TIME+">=? ORDER BY "+EVENT_KEY_TIME,new String[]{String.valueOf(time)});
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        cursor.getLong(4), cursor.getString(5), cursor.getLong(6), cursor.getInt(7), cursor.getString(8));
                list.add(event);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public int getLatestEventId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+EVENT_TABLE+" ORDER BY "+EVENT_KEY_ID+" desc LIMIT 1",null);
        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }

    public ArrayList<Event> getByClubEvent(String club){
        ArrayList<Event> list = new ArrayList<Event>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+EVENT_TABLE+" WHERE "+EVENT_KEY_CLUB+"=? ORDER BY "+EVENT_KEY_TIME+" desc",new String[]{club});
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        cursor.getLong(4), cursor.getString(5), cursor.getLong(6), cursor.getInt(7),cursor.getString(8));
                list.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<Event> getEventsAttended(){
        ArrayList<Event> list = new ArrayList<Event>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+EVENT_TABLE+" WHERE "+EVENT_KEY_ATTENDING+"!=0 ORDER BY "+EVENT_KEY_TIME+" desc",null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        cursor.getLong(4), cursor.getString(5), cursor.getLong(6), cursor.getInt(7),cursor.getString(8));
                list.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<Event> getTrendingEvents(){
        ArrayList<Event> list = new ArrayList<Event>();
        SQLiteDatabase db =this.getReadableDatabase();
        long time = System.currentTimeMillis()/1000l;
        Cursor cursor = db.rawQuery("SELECT * FROM "+EVENT_TABLE+" WHERE "+EVENT_KEY_ATTCOUNT+">10 and "+EVENT_KEY_TIME+">? ORDER BY "+EVENT_KEY_ATTCOUNT+" desc limit 5",new String[]{String.valueOf(time)});
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        cursor.getLong(4), cursor.getString(5), cursor.getLong(6), cursor.getInt(7),cursor.getString(8));
                list.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void updateAttendEvent(int id, int count){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE "+EVENT_TABLE+" SET "+EVENT_KEY_ATTCOUNT+"="+String.valueOf(count)+" WHERE "
                +EVENT_KEY_ID+"="+String.valueOf(id);
        db.execSQL(sql);
    }

    public void setEventKeyAttending(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE "+EVENT_TABLE+" SET "+EVENT_KEY_ATTENDING+"=1"+" WHERE ID="+String.valueOf(id);
        db.execSQL(sql);
    }

    public void addClub(Club club){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLUB_KEY_ID,club.getId());
        contentValues.put(CLUB_KEY_TITLE,club.getTitle());
        contentValues.put(CLUB_KEY_DESC,club.getDescription());
        contentValues.put(CLUB_KEY_PRES,club.getPresident());
        contentValues.put(CLUB_KEY_CONTACT,club.getContact());
        contentValues.put(CLUB_KEY_MOD,club.getLastmod());
        db.insert(CLUB_TABLE, null, contentValues);
    }

    public Club getClubById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CLUB_TABLE,CLUB_KEYS,CLUB_KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        Club club=null;
        if(cursor!=null){
            cursor.moveToFirst();
            club = new Club(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)
            ,cursor.getLong(5));
        }
        return club;
    }

    public Club getClubByTitle(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CLUB_TABLE, CLUB_KEYS, CLUB_KEY_TITLE + "=?", new String[]{title}, null, null, null);
        Club club=null;
        if(cursor!=null){
            cursor.moveToFirst();
            club = new Club(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)
                    ,cursor.getLong(5));
        }
        return club;
    }

    public ArrayList<Club> getAllClub(){
        ArrayList<Club> list = new ArrayList<Club>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM club",null);
        if(cursor.moveToFirst()){
            do{
                Club club = new Club(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)
                        ,cursor.getLong(5));
                list.add(club);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public long getRecentModClub(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+CLUB_TABLE+" ORDER BY "+CLUB_KEY_MOD+" DESC",null);
        if(cursor.moveToFirst()){
            return cursor.getLong(5);
        }
        cursor.close();
        return -1;
    }

    public void updateClub(Club club){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLUB_KEY_TITLE, club.getTitle());
        contentValues.put(CLUB_KEY_DESC,club.getDescription());
        contentValues.put(CLUB_KEY_PRES,club.getPresident());
        contentValues.put(CLUB_KEY_CONTACT,club.getContact());
        contentValues.put(CLUB_KEY_MOD,club.getLastmod());
        db.update(CLUB_TABLE, contentValues, CLUB_KEY_ID + "=?", new String[]{String.valueOf(club.getId())});
    }

    public void deleteClubById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CLUB_TABLE, CLUB_KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteClubByTitle(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CLUB_TABLE,CLUB_KEY_TITLE+"=?",new String[]{title});
    }

    public int getLatestClubId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+CLUB_TABLE+" ORDER BY "+CLUB_KEY_ID+" desc LIMIT 1",null);
        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }

}