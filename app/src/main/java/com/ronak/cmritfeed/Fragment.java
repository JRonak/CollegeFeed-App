package com.ronak.cmritfeed;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ronak.cmritfeed.Api.FetchService;
import com.ronak.cmritfeed.database.DbEventHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ronak on 11/8/15.
 */
public class Fragment extends android.app.Fragment {
    public Fragment() {
        super();
    }
    TextView textView;
    @Nullable

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final String s=msg.getData().getString("data");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(s);
                }
            });
        }
    };

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view;
        view = inflater.inflate(R.layout.trending,container,false);
        //Downloader downloader = new Downloader(handler,"http://192.168.1.9:1080/event");
        //downloader.Download();
        DbEventHelper db = DbEventHelper.getInstance(getActivity());
        ArrayList<Event> clubs = db.getAllEvents();
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/font.ttf");
        Intent intent = new Intent(getActivity(), FetchService.class);
        getActivity().startService(intent);
        return view;

    }

    public void Set(String s){
        textView.setText(s);
    }

}
