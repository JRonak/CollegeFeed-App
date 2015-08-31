package com.ronak.cmritfeed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ronak.cmritfeed.Adapter.SimpleAdapter;
import com.ronak.cmritfeed.DescriptionActivity;
import com.ronak.cmritfeed.Event;
import com.ronak.cmritfeed.Fragment;
import com.ronak.cmritfeed.R;
import com.ronak.cmritfeed.database.DbEventHelper;

import java.util.ArrayList;

/**
 * Created by ronak on 15/8/15.
 */
public class EventsByClubFragment extends Fragment {
    boolean eventAvail;
    String club;
    DbEventHelper dbEventHelper;
    ArrayList<Event> events;
    SimpleAdapter simpleAdapter;
    LinearLayout linearLayout;
    LinearLayout.LayoutParams layoutParams;
    TextView t;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending, container, false);
        Bundle bundle = getArguments();
        club = bundle.getString("club");
        getActivity().setTitle(club);
        dbEventHelper = DbEventHelper.getInstance(getActivity());
        getEvents();
        linearLayout = (LinearLayout) view.findViewById(R.id.trendingLL);
        layoutParams = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
        layoutParams.setMargins(5, 5, 5, 5);
        if(eventAvail) {
            displayList();
        }else{
            displayText();
        }
        return view;
    }

    void displayList(){
        simpleAdapter = new SimpleAdapter(getActivity(),0,events);
        listView = new ListView(getActivity());
        listView.setDivider(getActivity().getResources().getDrawable(R.color.listbg));
        listView.setDividerHeight(15);
        listView.setAdapter(simpleAdapter);
        listView.setLayoutParams(layoutParams);
        linearLayout.addView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                intent.putExtra("event",events.get(position));
                getActivity().startActivity(intent);
            }
        });
    }

    void displayText(){
        t = new TextView(getActivity());
        t.setText(club+" has no events, check us back later");
        t.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        t.setGravity(Gravity.CENTER);
        t.setTextSize(18);
        t.setLayoutParams(layoutParams);
        linearLayout.addView(t);
    }

    void getEvents(){
        events = dbEventHelper.getByClubEvent(club);
        if(events.size()==0){
            eventAvail = false;
        }else{
            eventAvail = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!eventAvail){
            getEvents();
            if(eventAvail){
                linearLayout.removeView(t);
                displayList();
            }
        }
        if(eventAvail){
            events = dbEventHelper.getByClubEvent(club);
            simpleAdapter.clear();
            simpleAdapter.addAll(events);
            simpleAdapter.notifyDataSetChanged();
        }
    }
}
