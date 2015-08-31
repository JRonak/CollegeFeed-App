package com.ronak.cmritfeed.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ronak.cmritfeed.Adapter.OverviewAdapter;
import com.ronak.cmritfeed.DescriptionActivity;
import com.ronak.cmritfeed.Event;
import com.ronak.cmritfeed.Fragment;
import com.ronak.cmritfeed.R;
import com.ronak.cmritfeed.database.DbEventHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TooManyListenersException;

/**
 * Created by ronak on 13/8/15.
 */
public class GeneralEventFragment extends Fragment {

    private static int UPCOMING_EVENT = 1;
    private static int ATTENDED_EVENT = 3;
    private static int TRENDING_EVENT = 2;

    int request;
    View view;
    boolean loadList;
    HashMap<String,Integer> iconIds;
    ArrayList<Event> events;
    DbEventHelper dbEventHelper;
    ListView listView;
    OverviewAdapter adapter;
    LinearLayout linearLayout;
    TextView textView;
    LinearLayout.LayoutParams layoutParams;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.trending, container, false);
        Bundle bundle = getArguments();
        iconIds = (HashMap<String,Integer>)bundle.getSerializable("hashmap");
        request = bundle.getInt("request");
        Log.e("Fragment",String.valueOf(request));
        dbEventHelper = DbEventHelper.getInstance(getActivity());
        linearLayout = (LinearLayout) view.findViewById(R.id.trendingLL);
        layoutParams = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
        layoutParams.setMargins(5, 5, 5, 5);
        getEvents();
        if(events.size()==0){
            displayTextview();
        }else{
            displayList();
        }
        return view;
    }

    void getEvents(){
        if(request==UPCOMING_EVENT){
            events = dbEventHelper.getUpcomingEvent();
        }else if(request==ATTENDED_EVENT){
            events = dbEventHelper.getEventsAttended();
        }else{
            events = dbEventHelper.getTrendingEvents();
        }
    }

    void displayTextview(){
        textView = new TextView(getActivity());
        if(request==ATTENDED_EVENT)
            textView.setText("You haven't liked any events yet");
        else
            textView.setText("Nothing yet, check us back later");
        linearLayout.addView(textView);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        loadList=false;
    }

    void displayList(){
        loadList=true;
        listView = new ListView(getActivity());
        adapter  = new OverviewAdapter(getActivity(),0,events,iconIds);
        listView.setAdapter(adapter);
        linearLayout.addView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                intent.putExtra("event", events.get(position));
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(loadList)
            updateList();
        else{
            getEvents();
            if(events.size()!=0){
                linearLayout.removeView(textView);
                displayList();
            }
        }
        getActivity().setTitle(getTitle());
    }

    String getTitle(){
        if(request == TRENDING_EVENT)
            return "Trending";
        else if(request == UPCOMING_EVENT){
           return "Upcoming";
        }else{
            return "Liked";
        }
    }

    void updateList(){
        getEvents();
        adapter.clear();
        adapter.addAll(events);
        adapter.notifyDataSetChanged();
    }
}
