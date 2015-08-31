package com.ronak.cmritfeed.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ronak.cmritfeed.Adapter.SimpleAdapter;
import com.ronak.cmritfeed.Fragment;
import com.ronak.cmritfeed.R;

import java.util.HashMap;

/**
 * Created by ronak on 14/8/15.
 */
public class SimpleClubFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trending, container, false);
        final Bundle bundle = getArguments();
        HashMap<String,Integer> iconIds = (HashMap<String,Integer>)bundle.getSerializable("hashmap");
        final String[] clubs = getResources().getStringArray(R.array.clubNames);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),0,clubs,iconIds);
        ListView listView = new ListView(getActivity());
        listView.setDivider(getActivity().getResources().getDrawable(R.color.listbg));
        listView.setDividerHeight(15);
        listView.setAdapter(simpleAdapter);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.trendingLL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
        layoutParams.setMargins(5,5,5,5);
        listView.setLayoutParams(layoutParams);
        linearLayout.addView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("club", clubs[position]);
                EventsByClubFragment eventsByClubFragment = new EventsByClubFragment();
                eventsByClubFragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.frame,eventsByClubFragment)
                        .setCustomAnimations(android.R.animator.fade_in,0,0,android.R.animator.fade_out).addToBackStack(null).commit();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Clubs");
    }
}
