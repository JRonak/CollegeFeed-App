package com.ronak.cmritfeed.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ronak.cmritfeed.Event;
import com.ronak.cmritfeed.R;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ronak on 13/8/15.
 */
public class OverviewAdapter extends ArrayAdapter<Event> {

    private Context context;
    private ArrayList<Event> events;
    private HashMap<String,Integer> iconIds;

    public OverviewAdapter(Context context, int resource, ArrayList<Event> events, HashMap<String,Integer> iconIds) {
        super(context, resource, events);
        this.context = context;
        this.events = events;
        this.iconIds = iconIds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder holder;
        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.overview_list,parent,false);
            holder = new holder();
            holder.club =(TextView) convertView.findViewById(R.id.clubName);
            holder.eventName =(TextView) convertView.findViewById(R.id.eventName);
            holder.time= (TextView) convertView.findViewById(R.id.eventDate);
            holder.imageView = (ImageView) convertView.findViewById(R.id.eventIcon);
            convertView.setTag(holder);
        }else{
            holder =(holder) convertView.getTag();
        }
        holder.club.setText(events.get(position).getClub());
        holder.eventName.setText(events.get(position).getTitle());
        Date date = events.get(position).getDate();
        Integer id = iconIds.get(events.get(position).getClub());
        if(id==null){
            id = R.drawable.kannada;
        }
        holder.imageView.setImageResource(id);
       // String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
      //  String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        holder.time.setText(day+" "+stringMonth+" "+year);
        return convertView;
    }

    static class holder{
        TextView club,eventName,time;
        ImageView imageView;
    }
}
