package usi.memotion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shkurtagashi on 9/6/18.
 */


public class FacebookArrayAdapter extends ArrayAdapter<PostLectureSurvey> {

    private Context context;
    private List<PostLectureSurvey> data;

    public FacebookArrayAdapter(@NonNull Context context, @NonNull List<PostLectureSurvey> objects) {
        super(context, 0, objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        PostLectureSurvey item = data.get(position);

        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }


        TextView textViewSocialMediaApp = (TextView) row.findViewById(R.id.social_media_app);
        textViewSocialMediaApp.setText(String.format("%s", item.get_question1()));


        TextView textViewStart = (TextView) row.findViewById(R.id.session_start);
        textViewStart.setText(String.format("%s", item.get_question2()));
//        Date currentDate = new Date(item.getTimestamp());
//        DateFormat df = new SimpleDateFormat("HH:mm:ss");
//        textViewStart.setText(String.format("Time: %s", df.format(currentDate)));

        TextView textViewLength = (TextView) row.findViewById(R.id.session_length);

        DecimalFormat dformat = new DecimalFormat("#.###");
        dformat.setRoundingMode(RoundingMode.CEILING);

        textViewLength.setText(String.format("Length: %s min", dformat.format(item.getTimestamp())));

        return row;
    }
}
