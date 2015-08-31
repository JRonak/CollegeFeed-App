package com.ronak.cmritfeed;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ronak.cmritfeed.Api.FetchService;
import com.ronak.cmritfeed.fragments.GeneralEventFragment;
import com.ronak.cmritfeed.fragments.SettingsFragment;
import com.ronak.cmritfeed.fragments.SimpleClubFragment;

import java.util.Calendar;
import java.util.HashMap;

public class Home extends AppCompatActivity {

    private static String UPDATE_SUCCESSFUL = "success";
    private static String UPDATE_FAIL = "fail";

    private String[] drawerTitles;
    private DrawerLayout drawerLayout;
    public String[] clubNames;
    public HashMap<String,Integer> iconIds;
    ActionBar actionBar;
    ListView listView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    int drawerPosition;
    boolean drawerOpen;
    CustomBroadcast broadcast;
    boolean refresStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_APPEND);
        if(sharedPreferences.getBoolean("first",true)){
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(this,0,new Intent(this,FetchService.class),0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            calendar.set(year, month, date, 20, 49);
            alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),24*60*60*1000,pendingIntent);
            sharedPreferences.edit().putBoolean("first",false).apply();
        }
        iconIds = new HashMap<String,Integer>();
        clubNames = getResources().getStringArray(R.array.clubNames);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        drawerTitles = getResources().getStringArray(R.array.drawerNames);
        int icons[] = {R.drawable.upcoming_drawer,R.drawable.trending,R.drawable.like_drawer,
        R.drawable.contact_drawer, R.drawable.settings};
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.red));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.abc_action_mode_done,R.string.abc_action_bar_home_description_format){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                listView.setItemChecked(drawerPosition, true);
                drawerOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerOpen = false;
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        SetupHashMap();
        if(savedInstanceState==null){
            updateFragment(0);
            drawerLayout.openDrawer(listView);
        }
        DrawerAdapter adapter = new DrawerAdapter(this,0,drawerTitles,icons);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateFragment(position);
            }
        });
        Intent intent = new Intent(this, FetchService.class);
        startService(intent);
    }

    void updateFragment(int position){
        FragmentManager manager = getFragmentManager();
        if(position<3){
            Bundle bundle = new Bundle();
            bundle.putSerializable("hashmap",iconIds);
            bundle.putInt("request", position + 1);
            GeneralEventFragment latestFragment = (GeneralEventFragment) new GeneralEventFragment();
            latestFragment.setArguments(bundle);
            manager.beginTransaction().replace(R.id.frame, latestFragment).commit();
        }else if(position==3){
            SimpleClubFragment simpleClubFragment = new SimpleClubFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("hashmap", iconIds);
            simpleClubFragment.setArguments(bundle);
            manager.beginTransaction().replace(R.id.frame, simpleClubFragment).commit();
        }
        else{
            SettingsFragment fragment = new SettingsFragment();
            manager.beginTransaction().replace(R.id.frame,fragment).commit();
        }
        listView.setItemChecked(position, true);
        drawerLayout.closeDrawer(listView);
        drawerPosition=position;
    }

    void SetupHashMap(){
        iconIds.put("Technical", R.drawable.techincal);
        iconIds.put("Art", R.drawable.art);
        iconIds.put("Dance",R.drawable.dance);
        iconIds.put("Theatre",R.drawable.theatre);
        iconIds.put("Kannada",R.drawable.kannada);
        iconIds.put("Music", R.drawable.music);
        iconIds.put("Literature", R.drawable.literature);
        iconIds.put("Photography", R.drawable.photography);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount()==0)
            super.onBackPressed();
        else{
            getFragmentManager().popBackStack();
        }
    }

    public void setIcon(int id){
        actionBar.setIcon(id);
    }

    public void setTitle(String title){
        actionBar.setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==android.R.id.home){
            if(drawerOpen)
                drawerLayout.closeDrawer(listView);
            else
                drawerLayout.openDrawer(listView);
        }else if(id == R.id.action_refresh){
            if(!refresStatus){
                refresStatus=false;
                Intent intent = new Intent(this, FetchService.class);
                startService(intent);
                Toast.makeText(getBaseContext(),"Syncing, please wait",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getBaseContext(),"Sync in progress, please wait",Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcast = new CustomBroadcast();
        IntentFilter intentFilter =new IntentFilter(UPDATE_SUCCESSFUL);
        intentFilter.addAction(UPDATE_FAIL);
        registerReceiver(broadcast, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcast);
    }

    void refresh(){
        if(getFragmentManager().getBackStackEntryCount()==0){
            updateFragment(drawerPosition);
            Log.e("Fragment","Updated");
        }
    }

    private class CustomBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction()==UPDATE_SUCCESSFUL){
                refresh();
                Toast.makeText(getBaseContext(),"Sync completed",Toast.LENGTH_SHORT).show();
            }else if(intent.getAction()==UPDATE_FAIL){
                Toast.makeText(getBaseContext(),"Sync failed, unable to communicate",Toast.LENGTH_SHORT).show();
            }
            refresStatus=false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
