package usi.memotion.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usi.memotion.R;

/**
 * Created by shkurtagashi on 12/09/18.
 */

public class HomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_layout, null);
        return root;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}