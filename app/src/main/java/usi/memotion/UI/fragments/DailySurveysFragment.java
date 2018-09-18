package usi.memotion.UI.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import usi.memotion.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailySurveysFragment extends Fragment {
    ExpandableRelativeLayout expandableLayoutFatigue, expandableLayoutMood, expandableLayoutProductivity, expandableLayoutStress, expandableLayoutSleep;


    public DailySurveysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_daily_surveys, container, false);

        expandableLayoutMood = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayoutMood);
        expandableLayoutFatigue = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayoutFatigue);
        expandableLayoutProductivity = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayoutProductivity);
        expandableLayoutSleep = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayoutSleep);
        expandableLayoutStress = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayoutStress);



        return root;
    }

}
