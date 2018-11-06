package usi.memotion2.UI.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import usi.memotion2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutApplicationFragment extends Fragment {


    public AboutApplicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_application, container, false);

    }

}
