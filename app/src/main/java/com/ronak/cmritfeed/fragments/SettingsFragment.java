package com.ronak.cmritfeed.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ronak.cmritfeed.Fragment;
import com.ronak.cmritfeed.R;

/**
 * Created by ronak on 15/8/15.
 */
public class SettingsFragment extends Fragment{
    CheckBox eventNotify,latestEventNotify;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_APPEND);
        View view = inflater.inflate(R.layout.settings,container,false);
        eventNotify = (CheckBox) view.findViewById(R.id.eventNotify);
        latestEventNotify = (CheckBox) view.findViewById(R.id.eventNewNotify);
        eventNotify.setChecked(sharedPreferences.getBoolean("eventNotify",true));
        latestEventNotify.setChecked(sharedPreferences.getBoolean("eventLatest", true));
        eventNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("eventNotify",isChecked).apply();
            }
        });
        latestEventNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("eventLatest",isChecked).apply();
            }
        });
        return view;
    }
}
