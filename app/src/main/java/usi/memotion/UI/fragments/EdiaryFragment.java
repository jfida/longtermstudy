package usi.memotion.UI.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usi.memotion.R;

public class EdiaryFragment extends Fragment {


    public EdiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ediary, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_activity_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builderAdd = new AlertDialog.Builder(getContext());

                builderAdd.setCancelable(false);
                builderAdd.setView(inflater.inflate(R.layout.create_activity_layout, null));
                builderAdd.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builderAdd.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builderAdd.create();
                builderAdd.show();
            }

            ;
        });
        return view;
    }

}
