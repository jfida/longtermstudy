package usi.memotion.UI.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import usi.memotion.FacebookArrayAdapter;
import usi.memotion.R;
import usi.memotion.local.database.db.LocalSQLiteDBHelper;

/**
 * Created by usi on 04/02/17.
 */

public class MapFragment extends Fragment {
    private ListView mListView;
    TextView facebookUsage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.map_layout, container, false);

        mListView = (ListView) root.findViewById(R.id.facebook_log_list_view);

        facebookUsage = (TextView) root.findViewById(R.id.totalUsage);

        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalSQLiteDBHelper data = new LocalSQLiteDBHelper(getActivity());
        List<PostLectureSurvey> allData = data.getAllPostlectureSurveys();


        ArrayAdapter adapter = new FacebookArrayAdapter(getActivity(), allData);

        int minutes = 0;
        for(PostLectureSurvey item: allData){
            minutes += item.getTimestamp();
        }

        facebookUsage.setText(String.format("Today you  minutes per day.", minutes));


        mListView.setAdapter(adapter);

    }
}

