package com.ronak.cmritfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ronak on 11/8/15.
 */
public class DrawerAdapter extends ArrayAdapter {

    private Context context;
    private String titles[];
    private int ids[];

    public DrawerAdapter(Context context,int resource, String titles[], int[] ids) {
        super(context, resource, titles);
        this.context = context;
        this.titles = titles;
        this.ids = ids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.drawer_list_item,parent,false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.drawerListIcon);
        TextView textView = (TextView) convertView.findViewById(R.id.drawerListText);
        imageView.setImageResource(ids[position]);
        textView.setText(titles[position]);
        return convertView;
    }
}
