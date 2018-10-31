package usi.memotion.UI.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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
                    public void onClick(DialogInterface dialog, int i) {
                        Spinner ediary_activity_spinner = (Spinner) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_spinner);
                        LinearLayout otherLayout = (LinearLayout) ((AlertDialog) dialog).findViewById(R.id.otherLayout);
                        EditText ediary_activity_edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_edit);

                        Spinner activity_start_hour = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_start_hour);
                        Spinner activity_start_minute = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_start_minute);

                        Spinner activity_end_hour = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_end_hour);
                        Spinner activity_end_minute = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_end_minute);

                        RadioGroup ediary_interaction_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_interaction_group);

                        RadioGroup ediary_emotion_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_emotion_group);

                        EditText ediary_comments = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_comments);

                        String activity = "";
                        if (otherLayout.getVisibility() == View.VISIBLE) {
                            if (ediary_activity_edit.getText().toString().isEmpty()) {
                                activity = "Other";
                            } else {
                                activity = ediary_activity_edit.getText().toString();
                            }
                        } else {
                            activity = ediary_activity_spinner.getSelectedItem().toString();
                        }

                        int choice = ediary_interaction_group.getCheckedRadioButtonId();
                        String interaction = "";
                        if (choice > 0) {
                            RadioButton radio = (RadioButton) ediary_interaction_group.findViewById(choice);
                            interaction = radio.getText().toString();
                        }
                        String start_time = activity_start_hour.getSelectedItem().toString() + ":" + activity_start_minute.getSelectedItem().toString();
                        String end_time = activity_end_hour.getSelectedItem().toString() + ":" + activity_end_minute.getSelectedItem().toString();
                        String comments = ediary_comments.getText().toString().isEmpty() ? "" : ediary_comments.getText().toString();

                        int choice_emotion = ediary_emotion_group.getCheckedRadioButtonId();
                        String emotion = "";
                        if (choice > 0) {
                            RadioButton radio = (RadioButton) ediary_emotion_group.findViewById(choice_emotion);
                            emotion = radio.getText().toString();
                        }
                        EdiaryActivity entry = new EdiaryActivity(emotion, activity, start_time, end_time, interaction, comments);
                    }
                });
                builderAdd.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog add = builderAdd.create();
                add.show();
                if (add.isShowing()) {
                    final Spinner ediary_activity_spinner = (Spinner) add.findViewById(R.id.ediary_activity_spinner);
                    final LinearLayout otherLayout = (LinearLayout) add.findViewById(R.id.otherLayout);
                    final EditText ediary_activity_edit = (EditText) add.findViewById(R.id.ediary_activity_edit);
                    ediary_activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (ediary_activity_spinner.getItemAtPosition(i).toString().equals("Other")) {
                                otherLayout.setVisibility(View.VISIBLE);
                            } else {
                                ediary_activity_edit.setText("");
                                otherLayout.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            };
        });
        return view;
    }


    public class EdiaryActivity {
        String emotion;
        String activity;
        String start_time;
        String end_time;
        String social_interaction;
        String comments;

        public EdiaryActivity(String emotion, String activity, String start_time, String end_time, String social_interaction, String comments) {
            this.emotion = emotion;
            this.activity = activity;
            this.start_time = start_time;
            this.end_time = end_time;
            this.social_interaction = social_interaction;
            this.comments = comments;
        }

        @Override
        public String toString() {
            return "EdiaryActivity{" +
                    "emotion='" + emotion + '\'' +
                    ", activity='" + activity + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", social_interaction='" + social_interaction + '\'' +
                    '}';
        }
    }

}
