package com.ronak.cmritfeed.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ronak.cmritfeed.Api.Const;
import com.ronak.cmritfeed.Event;
import com.ronak.cmritfeed.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by ronak on 14/8/15.
 */
public class SimpleAdapter extends ArrayAdapter {

    private Context context;
    private int mode;
    private ArrayList<Event> events;
    private String[] clubs;
    private HashMap<String, Integer> iconIds;
    private int[] colorIds;
    private static int iter;

    public SimpleAdapter(Context context, int resource, String[] clubs, HashMap<String, Integer> iconIds) {
        super(context, resource, clubs);
        this.context=context;
        this.mode=1;
        this.clubs = clubs;
        this.iconIds = iconIds;
    }

    public SimpleAdapter(Context context, int resource, ArrayList<Event> events) {
        super(context, resource, events);
        this.context=context;
        this.mode=2;
        this.events=events;
        this.colorIds = new int[]{R.color.red,R.color.green,R.color.blue,R.color.dark,R.color.orange};
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder holder;
        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.simple_list,parent,false);
            holder = new holder();
            holder.eventName =(TextView) convertView.findViewById(R.id.eventSimpleName);
            holder.time= (TextView) convertView.findViewById(R.id.eventSimpleDate);
            holder.imageView = (ImageView) convertView.findViewById(R.id.eventSimpleIcon);
            convertView.setTag(holder);
        }else{
            holder =(holder) convertView.getTag();
        }
        if(mode==2){
            holder.eventName.setText(events.get(position).getTitle());
            Date date = events.get(position).getDate();
            if(iter==colorIds.length){
                iter=0;
            }
            holder.imageView.setImageDrawable(context.getResources().getDrawable(colorIds[iter++]));
            String stringMonth = (String) android.text.format.DateFormat.format("MMM", date);
            String year = (String) android.text.format.DateFormat.format("yyyy", date);
            String day = (String) android.text.format.DateFormat.format("dd", date);
            holder.time.setText(day+" "+stringMonth+" "+year);
        }else{
            holder.eventName.setText(clubs[position]);
            Integer id = iconIds.get(clubs[position]);
            if(id==null){
                id = R.drawable.kannada;
            }
            holder.imageView.setImageResource(id);
        }
        return convertView;
    }

    static class holder{
        TextView eventName,time;
        ImageView imageView;
    }
}
