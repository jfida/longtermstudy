package usi.memotion.UI.fragments;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usi.memotion.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AnxietySurveyFragment extends Fragment {


    public AnxietySurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anxiety_survey, container, false);
    }

}
