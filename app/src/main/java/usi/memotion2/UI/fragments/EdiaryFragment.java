package usi.memotion2.UI.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import usi.memotion2.R;
import usi.memotion2.UI.EdiaryAdapter;
import usi.memotion2.local.database.controllers.LocalStorageController;
import usi.memotion2.local.database.controllers.SQLiteController;
import usi.memotion2.local.database.tables.EdiaryTable;

public class EdiaryFragment extends Fragment {
    ListView listView;
    private LocalStorageController localController;
    private String dateView;
    private String selectedActivity = "";


    public EdiaryFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ediary, container, false);
        localController = SQLiteController.getInstance(getContext());
        listView = view.findViewById(R.id.entriesList);
        final FloatingActionButton fab = view.findViewById(R.id.add_activity_button);
        final Spinner daySpinner = view.findViewById(R.id.daySpinner);

        // Put the setup function here!

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                switch (daySpinner.getItemAtPosition(i).toString()) {
                    case "Today":
                        dateView = formatter.format(new DateTime(new Date()).toDate());
                        break;
                    case "Yesterday":
                        dateView = formatter.format(new DateTime(new Date()).minusDays(1).toDate());
                        break;
                }
                new LoadEdiary().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final EdiaryActivity ediary = (EdiaryActivity) listView.getAdapter().getItem(i);
                final AlertDialog.Builder builderUpdate = new AlertDialog.Builder(getContext());
                builderUpdate.setCancelable(false);
                builderUpdate.setView(inflater.inflate(R.layout.create_activity_layout, null));
                builderUpdate.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builderUpdate.setPositiveButton("SUBMIT", null);
                final AlertDialog update = builderUpdate.create();
                update.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Log.d("DEBUG", "EDIT ACTIVITY");

                        final LinearLayout otherLayout = (LinearLayout) ((AlertDialog) update).findViewById(R.id.otherLayout);
                        final EditText ediary_activity_edit = (EditText) ((AlertDialog) update).findViewById(R.id.ediary_activity_edit);
                        String activity = ediary.getActivity();
                        initializeActivityBar((AlertDialog) dialog);
                        update.findViewById(getButtonIdFromActivityName(activity)).callOnClick();

                        set24HoursClock(update);

                        TimePicker activity_start = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.activity_start);
                        TimePicker activity_stop = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.activity_stop);

                        String start[] = ediary.getStart_time().split(":");
                        String end[] = ediary.getEnd_time().split(":");

                        activity_start.setHour(Integer.parseInt(start[0]));
                        activity_start.setMinute(Integer.parseInt(start[1]));

                        activity_stop.setHour(Integer.parseInt(end[0]));
                        activity_stop.setMinute(Integer.parseInt(end[1]));

                        setBtnListeners((AlertDialog) dialog);
                        RadioButton ediary_interaction_yes = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_interaction_yes);
                        RadioButton ediary_interaction_no = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_interaction_no);

                        switch (ediary.getSocial_interaction()) {
                            case "Yes":
                                ediary_interaction_yes.setChecked(true);
                                break;
                            case "No":
                                ediary_interaction_no.setChecked(true);
                                break;
                        }

                        RadioButton ediary_emotion_very_happy = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_very_happy);
                        RadioButton ediary_emotion_happy = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_happy);
                        RadioButton ediary_emotion_neutral = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_neutral);
                        RadioButton ediary_emotion_sad = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_sad);
                        RadioButton ediary_emotion_very_sad = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_very_sad);

                        switch (ediary.getEmotion()) {
                            case "very_happy":
                                ediary_emotion_very_happy.setChecked(true);
                                break;
                            case "happy":
                                ediary_emotion_happy.setChecked(true);
                                break;
                            case "neutral":
                                ediary_emotion_neutral.setChecked(true);
                                break;
                            case "sad":
                                ediary_emotion_sad.setChecked(true);
                                break;
                            case "very_sad":
                                ediary_emotion_very_sad.setChecked(true);
                                break;
                        }
                        EditText ediary_comments = (EditText) ((AlertDialog) update).findViewById(R.id.ediary_comments);
                        ediary_comments.setText(ediary.getComments());
                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("DEBUG", "EDIT ACTIVITY - SUBMISSION");
                                TextView chooseActivityText = (TextView) ((AlertDialog) dialog).findViewById(R.id.choose_activity_text);
                                final LinearLayout ediaryActivityButtonsGroup = (LinearLayout) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_buttons_group);

                                LinearLayout otherLayout = (LinearLayout) ((AlertDialog) dialog).findViewById(R.id.otherLayout);
                                EditText ediary_activity_edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_edit);

                                TimePicker activity_start = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.activity_start);

                                TimePicker activity_stop = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.activity_stop);

                                RadioGroup ediary_interaction_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_interaction_group);

                                RadioGroup ediary_emotion_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_emotion_group);

                                EditText ediary_comments = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_comments);

                                boolean error = false;
                                String activity = "";
                                if (otherLayout.getVisibility() == View.VISIBLE) {
                                    if (ediary_activity_edit.getText().toString().isEmpty()) {
                                        ediary_activity_edit.setError("Please enter a value!");
                                        error = true;
                                    } else {
                                        activity = ediary_activity_edit.getText().toString();
                                    }
                                } else {
                                    if (getActiveChildId(ediaryActivityButtonsGroup) == -1) {
                                        error = true;
                                        chooseActivityText.setError("Please enter a value!");
                                    } else {
                                        activity = getActivityNameFromButtonId(getActiveChildId(ediaryActivityButtonsGroup));
                                    }
                                }

                                if (!error) {
                                    int choice = ediary_interaction_group.getCheckedRadioButtonId();
                                    String interaction = "";
                                    if (choice > 0) {
                                        RadioButton radio = (RadioButton) ediary_interaction_group.findViewById(choice);
                                        interaction = radio.getText().toString();
                                    }

                                    String start_time   = String.format("%02d", activity_start.getHour()) + ":" + String.format("%02d", activity_start.getMinute());
                                    String end_time     = String.format("%02d", activity_stop.getHour()) + ":" + String.format("%02d", activity_stop.getMinute());

                                    String comments = ediary_comments.getText().toString().isEmpty() ? "" : ediary_comments.getText().toString();


                                    int choice_emotion = ediary_emotion_group.getCheckedRadioButtonId();

                                    String emotion = "";
                                    if (choice_emotion > 0) {
                                        switch (choice_emotion) {
                                            case R.id.ediary_emotion_very_happy:
                                                emotion = "very_happy";
                                                break;
                                            case R.id.ediary_emotion_happy:
                                                emotion = "happy";
                                                break;
                                            case R.id.ediary_emotion_neutral:
                                                emotion = "neutral";
                                                break;
                                            case R.id.ediary_emotion_sad:
                                                emotion = "sad";
                                                break;
                                            case R.id.ediary_emotion_very_sad:
                                                emotion = "very_sad";
                                                break;
                                        }
                                    }
                                    String timestamp = ediary.getTimestamp();
                                    EdiaryActivity entry = new EdiaryActivity(emotion, activity, start_time, end_time, interaction, comments, "", "");
                                    ContentValues record = new ContentValues();
                                    record.put(EdiaryTable.ACTIVITY, entry.getActivity());
                                    record.put(EdiaryTable.START_TIME, entry.getStart_time());
                                    record.put(EdiaryTable.END_TIME, entry.getEnd_time());
                                    record.put(EdiaryTable.SOCIAL_INTERACTION, entry.getSocial_interaction());
                                    record.put(EdiaryTable.EMOTION, entry.getEmotion());
                                    record.put(EdiaryTable.COMMENTS, entry.getComments());
                                    localController.update(EdiaryTable.TABLE_EDIARY_TABLE, record, EdiaryTable.TIMESTAMP + "=" + timestamp);
                                    Log.d("EDIARY UPDATE", "Added record: ts: " + record.get(EdiaryTable.TIMESTAMP));
                                    new LoadEdiary().execute();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                update.show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builderAdd = new AlertDialog.Builder(getContext());
                builderAdd.setCancelable(false);
                builderAdd.setView(inflater.inflate(R.layout.create_activity_layout, null));
                builderAdd.setPositiveButton("SUBMIT", null);
                builderAdd.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog add = builderAdd.create();
                add.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        setBtnListeners((AlertDialog) dialog);
                        final LinearLayout otherLayout = (LinearLayout) add.findViewById(R.id.otherLayout);
                        final EditText ediary_activity_edit = (EditText) add.findViewById(R.id.ediary_activity_edit);
                        otherLayout.setVisibility(View.GONE);
                        initializeActivityBar(add);

                        setBtnListeners((AlertDialog) dialog);
                        set24HoursClock((AlertDialog) dialog);

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                TextView chooseActivityText = (TextView) ((AlertDialog) dialog).findViewById(R.id.choose_activity_text);
                                final LinearLayout ediaryActivityButtonsGroup = (LinearLayout) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_buttons_group);

                                LinearLayout otherLayout = (LinearLayout) ((AlertDialog) dialog).findViewById(R.id.otherLayout);
                                EditText ediary_activity_edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_edit);

                                TimePicker activity_start = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.activity_start);

                                TimePicker activity_stop = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.activity_stop);

                                RadioGroup ediary_interaction_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_interaction_group);

                                RadioGroup ediary_emotion_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_emotion_group);

                                EditText ediary_comments = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_comments);

                                boolean error = false;
                                String activity = "";
                                if (otherLayout.getVisibility() == View.VISIBLE) {
                                    if (ediary_activity_edit.getText().toString().isEmpty()) {
                                        ediary_activity_edit.setError("Please enter a value!");
                                        error = true;
                                    } else {
                                        activity = ediary_activity_edit.getText().toString();
                                    }
                                } else {
                                    if (getActiveChildId(ediaryActivityButtonsGroup) == -1) {
                                        error = true;
                                        chooseActivityText.setError("Please enter a value!");
                                    } else {
                                        activity = getActivityNameFromButtonId(getActiveChildId(ediaryActivityButtonsGroup));
                                    }
                                }

                                if (!error) {
                                    int choice = ediary_interaction_group.getCheckedRadioButtonId();
                                    String interaction = "";
                                    if (choice > 0) {
                                        RadioButton radio = ediary_interaction_group.findViewById(choice);
                                        interaction = radio.getText().toString();
                                    }

                                    String start_time   = String.format("%02d", activity_start.getHour()) + ":" + String.format("%02d", activity_start.getMinute());
                                    String end_time     = String.format("%02d", activity_stop.getHour()) + ":" + String.format("%02d", activity_stop.getMinute());

                                    String comments = ediary_comments.getText().toString().isEmpty() ? "" : ediary_comments.getText().toString();

                                    int choice_emotion = ediary_emotion_group.getCheckedRadioButtonId();
                                    String emotion = "";
                                    if (choice_emotion > 0) {
                                        switch (choice_emotion) {
                                            case R.id.ediary_emotion_very_happy:
                                                emotion = "very_happy";
                                                break;
                                            case R.id.ediary_emotion_happy:
                                                emotion = "happy";
                                                break;
                                            case R.id.ediary_emotion_neutral:
                                                emotion = "neutral";
                                                break;
                                            case R.id.ediary_emotion_sad:
                                                emotion = "sad";
                                                break;
                                            case R.id.ediary_emotion_very_sad:
                                                emotion = "very_sad";
                                                break;
                                        }
                                    }
                                    String timestamp = String.valueOf(System.currentTimeMillis());
                                    EdiaryActivity entry = new EdiaryActivity(emotion, activity, start_time, end_time, interaction, comments, dateView, timestamp);
                                    ContentValues record = new ContentValues();
                                    record.put(EdiaryTable.TIMESTAMP, timestamp);
                                    record.put(EdiaryTable.ACTIVITY, entry.getActivity());
                                    record.put(EdiaryTable.START_TIME, entry.getStart_time());
                                    record.put(EdiaryTable.END_TIME, entry.getEnd_time());
                                    record.put(EdiaryTable.SOCIAL_INTERACTION, entry.getSocial_interaction());
                                    record.put(EdiaryTable.EMOTION, entry.getEmotion());
                                    record.put(EdiaryTable.COMMENTS, entry.getComments());
                                    record.put(EdiaryTable.ENTRY_DATE, entry.getDate_entry());
                                    localController.insertRecord(EdiaryTable.TABLE_EDIARY_TABLE, record);
                                    Log.d("EDIARY ENTRY", "Added record: ts: " + record.get(EdiaryTable.TIMESTAMP));
                                    new LoadEdiary().execute();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                add.show();
            }

            ;
        });
        return view;
    }

    private int getActiveChildId(LinearLayout ediaryActivityButtonsGroup) {
        for (int i = 0; i < ediaryActivityButtonsGroup.getChildCount() - 1; ++i) {
            if (ediaryActivityButtonsGroup.getChildAt(i).isActivated())
                return ediaryActivityButtonsGroup.getChildAt(i).getId();
        }
        return -1;
    }

    private void initializeActivityBar(final AlertDialog dialog) {
        // activity image buttons
        final LinearLayout ediaryActivityButtonsGroup = (LinearLayout) dialog.findViewById(R.id.ediary_activity_buttons_group);
        ImageButton activityButton;
        for (int i = 0; i < ediaryActivityButtonsGroup.getChildCount() - 1; ++i) {
            activityButton = (ImageButton) ediaryActivityButtonsGroup.getChildAt(i);
            activityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageButton target = (ImageButton) view;
                    LinearLayout container = (LinearLayout) target.getParent();
                    TextView activityViewer = (TextView) dialog.findViewById(R.id.ediary_selected_activity_text);
                    assert activityViewer != null;
                    // deselect activity
                    if (target.isActivated()) {
                        target.setActivated(false);
                        selectedActivity = "";
                        activityViewer.setVisibility(View.GONE);
                        ((EditText) dialog.findViewById(R.id.ediary_activity_edit)).setText("");
                        dialog.findViewById(R.id.otherLayout).setVisibility(View.GONE);
                        for (int i = 0; i < container.getChildCount() - 1; ++i) {
                            ImageButton button = (ImageButton) container.getChildAt(i);
                            button.setVisibility(View.VISIBLE);
                            button.setActivated(false);
                        }
                    // select activity
                    } else {
                        target.setActivated(true);
                        if (target.getId() == R.id.other_activity_button)
                            dialog.findViewById(R.id.otherLayout).setVisibility(View.VISIBLE);
                        else {
                            ((EditText) dialog.findViewById(R.id.ediary_activity_edit)).setText("");
                            dialog.findViewById(R.id.otherLayout).setVisibility(View.GONE);
                        }
                        for (int i = 0; i < container.getChildCount() - 1; ++i) {
                            ImageButton button = (ImageButton) container.getChildAt(i);
                            if (button == target) {
                                selectedActivity = getActivityNameFromButtonId(target.getId());
                                activityViewer.setText(selectedActivity);
                                activityViewer.setVisibility(View.VISIBLE);
                            } else button.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }

    private String getActivityNameFromButtonId(int id) {
        switch (id) {
            case R.id.sleep_activity_button:
                return "Sleep";
            case R.id.study_activity_button:
                return "Study";
            case R.id.relaxation_activity_button:
                return "Relaxation";
            case R.id.physical_exercise_activity_button:
                return "Physical exercise";
            case R.id.attend_lecture_activity_button:
                return "Attend lecture";
            case R.id.eat_activity_button:
                return "Eat";
            case R.id.commute_activity_button:
                return "Commute";
            case R.id.socialize_activity_button:
                return "Socialize";
            case R.id.work_activity_button:
                return "Work";
            default:
                return "Other";
        }
    }

    private int getButtonIdFromActivityName(String name) {
        switch (name) {
            case "Sleep":
                return R.id.sleep_activity_button;
            case "Study":
                return R.id.study_activity_button;
            case "Relaxation":
                return R.id.relaxation_activity_button;
            case "Physical exercise":
                return R.id.physical_exercise_activity_button;
            case "Attend lecture":
                return R.id.attend_lecture_activity_button;
            case "Eat":
                return R.id.eat_activity_button;
            case "Commute":
                return R.id.commute_activity_button;
            case "Socialize":
                return R.id.socialize_activity_button;
            case "Work":
                return R.id.work_activity_button;
            default:
                return R.id.other_activity_button;
        }
    }


    public class EdiaryActivity {
        String timestamp;
        String emotion;
        String activity;
        String start_time;
        String date_entry;
        String end_time;
        String social_interaction;
        String comments;

        EdiaryActivity(String emotion, String activity, String start_time, String end_time, String social_interaction, String comments, String date_entry, String timestamp) {
            this.emotion = emotion;
            this.activity = activity;
            this.start_time = start_time;
            this.end_time = end_time;
            this.social_interaction = social_interaction;
            this.comments = comments;
            this.date_entry = date_entry;
            this.timestamp = timestamp;
        }

        public String getEmotion() {
            return emotion;
        }

        public String getActivity() {
            return activity;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getSocial_interaction() {
            return social_interaction;
        }

        public String getComments() {
            return comments;
        }

        String getDate_entry() {
            return date_entry;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }

    public class LoadEdiary extends AsyncTask {
        ArrayList<EdiaryActivity> ediaries = new ArrayList<>();

        @Override
        protected Object doInBackground(Object[] objects) {
            String query = "SELECT * FROM " + EdiaryTable.TABLE_EDIARY_TABLE + " WHERE " + EdiaryTable.ENTRY_DATE + " = \"" + dateView + "\" ORDER BY " + EdiaryTable.START_TIME + ";";
            Cursor c = localController.rawQuery(query, null);
            while (c.moveToNext()) {
                EdiaryActivity ediary = new EdiaryActivity(c.getString(6), c.getString(2), c.getString(3), c.getString(4), c.getString(7), c.getString(8), c.getString(5), c.getString(1));
                ediaries.add(ediary);
            }
            c.close();
            final EdiaryAdapter adapter = new EdiaryAdapter(getContext(), ediaries);
            getActivity().runOnUiThread(new Thread(new Runnable() {
                public void run() {
                    listView.setAdapter(adapter);
                }
            }));
            return null;
        }
    }


    /**
     * Set emotion and interaction
     */

    public void set24HoursClock(final AlertDialog dialog){
        TimePicker start = (TimePicker) dialog.findViewById(R.id.activity_start);
        TimePicker stop = (TimePicker) dialog.findViewById(R.id.activity_stop);
        start.setIs24HourView(true);
        stop.setIs24HourView(true);
    }

    /**
     * Set emotion/interaction buttons eventListeners
     */

    public void setBtnListeners(final AlertDialog dialog){
        setRadioBtnsListeners(dialog, R.id.ediary_interaction_group);
        setRadioBtnsListeners(dialog, R.id.ediary_emotion_group);
    }

    public void setRadioBtnsListeners(final AlertDialog dialog, final int groupId){
        RadioGroup ediary_emotion_group = (RadioGroup) dialog.findViewById(groupId);
        try {
            ediary_emotion_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    deselectAllRadioBtns(group, groupId);
                    selectCheckedBtn(group, checkedId, groupId);
                }
            });
        } catch (NullPointerException e){
            Log.e("Exception", "Found the following NullPointerException", e);
        }
    }

    public void deselectAllRadioBtns(RadioGroup group, int groupId){

        for(int i=0;i < group.getChildCount();i++) {

            RadioButton rb = (RadioButton) group.getChildAt(i);

            if(groupId ==  R.id.ediary_emotion_group){
                deselectEmotionRadioBtn(rb);
            } else if(groupId ==  R.id.ediary_interaction_group){
                deselectInteractionRadioBtn(rb);
            }
        }
    }

    public void deselectInteractionRadioBtn(RadioButton rb){
        rb.setTextColor(getResources().getColor(R.color.ms_black, getActivity().getTheme()));
    }

    public void deselectEmotionRadioBtn(RadioButton rb){
        switch (rb.getId()) {
            case R.id.ediary_emotion_very_happy:
                rb.setBackgroundResource(R.drawable.activity_very_happy);
                break;
            case R.id.ediary_emotion_happy:
                rb.setBackgroundResource(R.drawable.activity_happy);
                break;
            case R.id.ediary_emotion_neutral:
                rb.setBackgroundResource(R.drawable.activity_neutral);
                break;
            case R.id.ediary_emotion_sad:
                rb.setBackgroundResource(R.drawable.activity_sad);
                break;
            case R.id.ediary_emotion_very_sad:
                rb.setBackgroundResource(R.drawable.activity_very_sad);
                break;
        }
        rb.setScaleX(0.8f);
        rb.setScaleY(0.8f);
    }

    public void selectCheckedBtn(RadioGroup group, int btnId, int groupId){

        RadioButton rb = group.findViewById(btnId);

        if(groupId ==  R.id.ediary_emotion_group){
            selectEmotionRadioBtn(rb);
        } else if(groupId ==  R.id.ediary_interaction_group){
            selectInteractionRadioBtn(rb);
        }
    }

    public void selectInteractionRadioBtn(RadioButton rb){
        rb.setTextColor(getResources().getColor(R.color.colorAccent, getActivity().getTheme()));
    }

    public void selectEmotionRadioBtn(RadioButton rb){
        switch (rb.getId()) {
            case R.id.ediary_emotion_very_happy:
                rb.setBackgroundResource(R.drawable.activity_very_happy_selected);
                break;
            case R.id.ediary_emotion_happy:
                rb.setBackgroundResource(R.drawable.activity_happy_selected);
                break;
            case R.id.ediary_emotion_neutral:
                rb.setBackgroundResource(R.drawable.activity_neutral_selected);
                break;
            case R.id.ediary_emotion_sad:
                rb.setBackgroundResource(R.drawable.activity_sad_selected);
                break;
            case R.id.ediary_emotion_very_sad:
                rb.setBackgroundResource(R.drawable.activity_very_sad_selected);
                break;
        }
        rb.setScaleX(1);
        rb.setScaleY(1);
    }

}
