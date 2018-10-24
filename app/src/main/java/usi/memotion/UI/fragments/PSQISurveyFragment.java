package usi.memotion.UI.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import usi.memotion.MainActivity;
import usi.memotion.R;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.db.LocalSQLiteDBHelper;
import usi.memotion.local.database.tables.PSQISurveyTable;
import usi.memotion.local.database.tables.UserTable;
import usi.memotion.remote.database.controllers.SwitchDriveController;
import usi.memotion.remote.database.upload.UploadAlarmReceiver;
import usi.memotion.remote.database.upload.Uploader;

/**
 * Created by biancastancu
 */
public class PSQISurveyFragment extends Fragment {
    private String question1, question2, question3, question4, question5a,
            question5b, question5c, question5d, question5e, question5f, question5g, question5h, question5i,
            question5j, question5j_text, question6, question7, question8, question9, question10,
            question10a, question10b, question10c, question10d, question10e;

    LocalSQLiteDBHelper dbHelper;
    private LocalStorageController localController;

    SwitchDriveController switchDriveController;
    String androidID;

    private Button submitButton;
    private LocalStorageController localcontroller;

    private EditText psqi_q1_answer;
    private EditText psqi_q2_answer;
    private EditText psqi_q3_answer;
    private EditText psqi_q4_answer;
    private EditText psqi_q5j_text;

    private RadioGroup group_5a;
    private RadioGroup group_5b;
    private RadioGroup group_5c;
    private RadioGroup group_5d;
    private RadioGroup group_5e;
    private RadioGroup group_5f;
    private RadioGroup group_5g;
    private RadioGroup group_5h;
    private RadioGroup group_5i;
    private RadioGroup group_5j;

    private RadioGroup group_6;
    private RadioGroup group_7;
    private RadioGroup group_8;
    private RadioGroup group_9;
    private RadioGroup group_10;
    private RadioGroup group_10a;
    private RadioGroup group_10b;
    private RadioGroup group_10c;
    private RadioGroup group_10d;
    private RadioGroup group_10e;

    private TextView group5a_tv;
    private TextView group5b_tv;
    private TextView group5c_tv;
    private TextView group5d_tv;
    private TextView group5e_tv;
    private TextView group5f_tv;
    private TextView group5g_tv;
    private TextView group5h_tv;
    private TextView group5i_tv;
    private TextView group6_tv;
    private TextView group7_tv;
    private TextView group8_tv;
    private TextView group9_tv;
    private TextView group10_tv;
    private TextView group10a_tv;
    private TextView group10b_tv;
    private TextView group10c_tv;
    private TextView group10d_tv;
    private TextView group10e_tv;

    public PSQISurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_psqi_survey, container, false);

        dbHelper = new LocalSQLiteDBHelper(getContext());
        localController = SQLiteController.getInstance(getContext());

        localcontroller = SQLiteController.getInstance(getContext());

        psqi_q1_answer = (EditText) root.findViewById(R.id.psqi_q1_answer);
        psqi_q2_answer = (EditText) root.findViewById(R.id.psqi_q2_answer);
        psqi_q3_answer = (EditText) root.findViewById(R.id.psqi_q3_answer);
        psqi_q4_answer = (EditText) root.findViewById(R.id.psqi_q4_answer);
        psqi_q5j_text = (EditText) root.findViewById(R.id.psqi_q5j_text);

        group_5a = (RadioGroup) root.findViewById(R.id.group_5a);
        group_5b = (RadioGroup) root.findViewById(R.id.group_5b);
        group_5c = (RadioGroup) root.findViewById(R.id.group_5c);
        group_5d = (RadioGroup) root.findViewById(R.id.group_5d);
        group_5e = (RadioGroup) root.findViewById(R.id.group_5e);
        group_5f = (RadioGroup) root.findViewById(R.id.group_5f);
        group_5g = (RadioGroup) root.findViewById(R.id.group_5g);
        group_5h = (RadioGroup) root.findViewById(R.id.group_5h);
        group_5i = (RadioGroup) root.findViewById(R.id.group_5i);
        group_5j = (RadioGroup) root.findViewById(R.id.group_5j);
        group_6 = (RadioGroup) root.findViewById(R.id.group_6);
        group_7 = (RadioGroup) root.findViewById(R.id.group_7);
        group_8 = (RadioGroup) root.findViewById(R.id.group_8);
        group_9 = (RadioGroup) root.findViewById(R.id.group_9);
        group_10 = (RadioGroup) root.findViewById(R.id.group_10);
        group_10a = (RadioGroup) root.findViewById(R.id.group_10a);
        group_10b = (RadioGroup) root.findViewById(R.id.group_10b);
        group_10c = (RadioGroup) root.findViewById(R.id.group_10c);
        group_10d = (RadioGroup) root.findViewById(R.id.group_10d);
        group_10e = (RadioGroup) root.findViewById(R.id.group_10e);

        group5a_tv = (TextView) root.findViewById(R.id.group_5a_tv);
        group5b_tv = (TextView) root.findViewById(R.id.group_5b_tv);
        group5c_tv = (TextView) root.findViewById(R.id.group_5c_tv);
        group5d_tv = (TextView) root.findViewById(R.id.group_5d_tv);
        group5e_tv = (TextView) root.findViewById(R.id.group_5e_tv);
        group5f_tv = (TextView) root.findViewById(R.id.group_5f_tv);
        group5g_tv = (TextView) root.findViewById(R.id.group_5g_tv);
        group5h_tv = (TextView) root.findViewById(R.id.group_5h_tv);
        group5i_tv = (TextView) root.findViewById(R.id.group_5i_tv);
        group6_tv = (TextView) root.findViewById(R.id.group_6_tv);
        group7_tv = (TextView) root.findViewById(R.id.group_7_tv);
        group8_tv = (TextView) root.findViewById(R.id.group_8_tv);
        group9_tv = (TextView) root.findViewById(R.id.group_9_tv);
        group10_tv = (TextView) root.findViewById(R.id.group_10_tv);
        group10a_tv = (TextView) root.findViewById(R.id.group_10a_tv);
        group10b_tv = (TextView) root.findViewById(R.id.group_10b_tv);
        group10c_tv = (TextView) root.findViewById(R.id.group_10c_tv);
        group10d_tv = (TextView) root.findViewById(R.id.group_10d_tv);
        group10e_tv = (TextView) root.findViewById(R.id.group_10e_tv);
        submitButton = (Button) root.findViewById(R.id.submit_psqi);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getAnswers()) {
                    ContentValues record = new ContentValues();
                    record.put(PSQISurveyTable.TIMESTAMP, System.currentTimeMillis());
                    record.put(PSQISurveyTable.QUESTION_1, question1);
                    record.put(PSQISurveyTable.QUESTION_2, question2);
                    record.put(PSQISurveyTable.QUESTION_3, question3);
                    record.put(PSQISurveyTable.QUESTION_4, question4);
                    record.put(PSQISurveyTable.QUESTION_5a, question5a);
                    record.put(PSQISurveyTable.QUESTION_5b, question5b);
                    record.put(PSQISurveyTable.QUESTION_5c, question5c);
                    record.put(PSQISurveyTable.QUESTION_5d, question5d);
                    record.put(PSQISurveyTable.QUESTION_5e, question5e);
                    record.put(PSQISurveyTable.QUESTION_5f, question5f);
                    record.put(PSQISurveyTable.QUESTION_5g, question5g);
                    record.put(PSQISurveyTable.QUESTION_5h, question5h);
                    record.put(PSQISurveyTable.QUESTION_5i, question5i);
                    int choice5j = group_5j.getCheckedRadioButtonId();
                    question5j_text = psqi_q5j_text.getText().toString();
                    if (choice5j > 0 || !question5j_text.isEmpty()) {
                        RadioButton radio5j = (RadioButton) group_5j.findViewById(choice5j);
                        question5j = radio5j.getText().toString();
                        record.put(PSQISurveyTable.QUESTION_5j, question5j);
                    }
                    record.put(PSQISurveyTable.QUESTION_6, question6);
                    record.put(PSQISurveyTable.QUESTION_7, question7);
                    record.put(PSQISurveyTable.QUESTION_8, question8);
                    record.put(PSQISurveyTable.QUESTION_9, question9);
                    record.put(PSQISurveyTable.QUESTION_10, question10);
                    record.put(PSQISurveyTable.QUESTION_10a, question10a);
                    record.put(PSQISurveyTable.QUESTION_10b, question10b);
                    record.put(PSQISurveyTable.QUESTION_10c, question10c);
                    record.put(PSQISurveyTable.QUESTION_10d, question10d);
                    record.put(PSQISurveyTable.QUESTION_10e, question10e);

                    localcontroller.insertRecord(PSQISurveyTable.TABLE_PSQUI_SURVEY, record);

                    Log.d("PSQI SURVEYS", "Added record: ts: " + record.get(PSQISurveyTable.TIMESTAMP));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.thank_you);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    uploadRemotely();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.fill_answers);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        return root;
    }

    boolean getAnswers() {
        boolean q1 = false;
        boolean q2 = false;
        boolean q3 = false;
        boolean q4 = false;
        boolean q5a = false;
        boolean q5b = false;
        boolean q5c = false;
        boolean q5d = false;
        boolean q5e = false;
        boolean q5f = false;
        boolean q5g = false;
        boolean q5h = false;
        boolean q5i = false;
        boolean q6 = false;
        boolean q7 = false;
        boolean q8 = false;
        boolean q9 = false;
        boolean q10 = false;
        boolean q10a = false;
        boolean q10b = false;
        boolean q10c = false;
        boolean q10d = false;
        boolean q10e = false;

        question1 = psqi_q1_answer.getText().toString();
        if (question1.isEmpty()){
            q1 = true;
            psqi_q1_answer.setError("error");
        } else {
            psqi_q1_answer.setError(null);
        }

        question2 = psqi_q2_answer.getText().toString();
        if (question2.isEmpty()){
            q2 = true;
            psqi_q2_answer.setError("error");
        } else {
            psqi_q2_answer.setError(null);
        }

        question3 = psqi_q3_answer.getText().toString();
        if (question3.isEmpty()){
            q3 = true;
            psqi_q3_answer.setError("error");
        } else {
            psqi_q3_answer.setError(null);
        }

        question4 = psqi_q4_answer.getText().toString();
        if (question4.isEmpty()){
            q4 = true;
            psqi_q4_answer.setError("error");
        } else {
            psqi_q4_answer.setError(null);
        }

        int choice5a = group_5a.getCheckedRadioButtonId();
        if (choice5a <= 0) {
            q5a = true;
            group5a_tv.setError("error");
        } else {
            RadioButton radio5a = (RadioButton) group_5a.findViewById(choice5a);
            question5a = radio5a.getText().toString();
            group5a_tv.setError(null);
        }

        int choice5b = group_5b.getCheckedRadioButtonId();
        if (choice5b <= 0) {
            q5b = true;
            group5b_tv.setError("error");
        } else {
            RadioButton radio5b = (RadioButton) group_5b.findViewById(choice5b);
            question5b = radio5b.getText().toString();
            group5b_tv.setError(null);
        }

        int choice5c = group_5c.getCheckedRadioButtonId();
        if (choice5c <= 0) {
            q5c = true;
            group5c_tv.setError("error");
        } else {
            RadioButton radio5c = (RadioButton) group_5c.findViewById(choice5c);
            question5c = radio5c.getText().toString();
            group5c_tv.setError(null);
        }

        int choice5d = group_5d.getCheckedRadioButtonId();
        if (choice5d <= 0) {
            q5d = true;
            group5d_tv.setError("error");
        } else {
            RadioButton radio5d = (RadioButton) group_5d.findViewById(choice5d);
            question5d = radio5d.getText().toString();
            group5d_tv.setError(null);
        }

        int choice5e = group_5e.getCheckedRadioButtonId();
        if (choice5e <= 0) {
            q5e = true;
            group5e_tv.setError("error");
        } else {
            RadioButton radio5e = (RadioButton) group_5e.findViewById(choice5e);
            question5e = radio5e.getText().toString();
            group5e_tv.setError(null);
        }

        int choice5f = group_5f.getCheckedRadioButtonId();
        if (choice5f <= 0) {
            q5f = true;
            group5f_tv.setError("error");
        } else {
            RadioButton radio5f = (RadioButton) group_5f.findViewById(choice5f);
            question5f = radio5f.getText().toString();
            group5f_tv.setError(null);

        }

        int choice5g = group_5g.getCheckedRadioButtonId();
        if (choice5g <= 0) {
            q5g = true;
            group5g_tv.setError("error");
        } else {
            RadioButton radio5g = (RadioButton) group_5g.findViewById(choice5g);
            question5g = radio5g.getText().toString();
            group5g_tv.setError(null);
        }

        int choice5h = group_5h.getCheckedRadioButtonId();
        if (choice5h <= 0) {
            q5h = true;
            group5h_tv.setError("error");
        } else {
            RadioButton radio5h = (RadioButton) group_5h.findViewById(choice5h);
            question5h = radio5h.getText().toString();
            group5h_tv.setError(null);

        }

        int choice5i = group_5i.getCheckedRadioButtonId();
        if (choice5i <= 0) {
            q5i = true;
            group5i_tv.setError("error");
        } else {
            RadioButton radio5i = (RadioButton) group_5i.findViewById(choice5i);
            question5i = radio5i.getText().toString();
            group5i_tv.setError(null);
        }

        int choice6 = group_6.getCheckedRadioButtonId();
        if (choice6 <= 0) {
            q6 = true;
            group6_tv.setError("error");
        } else {
            RadioButton radio6 = (RadioButton) group_6.findViewById(choice6);
            question6 = radio6.getText().toString();
            group6_tv.setError(null);
        }

        int choice7 = group_7.getCheckedRadioButtonId();
        if (choice7 <= 0) {
            q7 = true;
            group7_tv.setError("error");
        } else {
            RadioButton radio7 = (RadioButton) group_7.findViewById(choice7);
            question7 = radio7.getText().toString();
            group7_tv.setError(null);
        }

        int choice8 = group_8.getCheckedRadioButtonId();
        if (choice8 <= 0) {
            q8 = true;
            group8_tv.setError("error");
        } else {
            RadioButton radio8 = (RadioButton) group_8.findViewById(choice8);
            question8 = radio8.getText().toString();
            group8_tv.setError(null);
        }

        int choice9 = group_9.getCheckedRadioButtonId();
        if (choice9 <= 0) {
            q9 = true;
            group9_tv.setError("error");
        } else {
            RadioButton radio9 = (RadioButton) group_9.findViewById(choice9);
            question9 = radio9.getText().toString();
            group9_tv.setError(null);
        }

        int choice10 = group_10.getCheckedRadioButtonId();
        if (choice10 <= 0) {
            q10 = true;
            group10_tv.setError("error");
        } else {
            RadioButton radio10 = (RadioButton) group_10.findViewById(choice10);
            question10 = radio10.getText().toString();
            group10_tv.setError(null);
        }

        int choice10a = group_10a.getCheckedRadioButtonId();
        if (choice10a <= 0) {
            q10a = true;
            group10a_tv.setError("error");
        } else {
            RadioButton radio10a = (RadioButton) group_10a.findViewById(choice10a);
            question10a = radio10a.getText().toString();
            group10a_tv.setError(null);
        }

        int choice10b = group_10b.getCheckedRadioButtonId();
        if (choice10b <= 0) {
            q10b = true;
            group10b_tv.setError("error");
        } else {
            RadioButton radio10b = (RadioButton) group_10b.findViewById(choice10b);
            question10b = radio10b.getText().toString();
            group10b_tv.setError(null);
        }

        int choice10c = group_10c.getCheckedRadioButtonId();
        if (choice10c <= 0) {
            q10c = true;
            group10c_tv.setError("error");
        } else {
            RadioButton radio10c = (RadioButton) group_10c.findViewById(choice10c);
            question10c = radio10c.getText().toString();
            group10c_tv.setError(null);
        }

        int choice10d = group_10d.getCheckedRadioButtonId();
        if (choice10d <= 0) {
            q10d = true;
            group10d_tv.setError("error");
        } else {
            RadioButton radio10d = (RadioButton) group_10d.findViewById(choice10d);
            question10d = radio10d.getText().toString();
            group10d_tv.setError(null);
        }

        int choice10e = group_10e.getCheckedRadioButtonId();
        if (choice10e <= 0) {
            q10e = true;
            group10e_tv.setError("error");
        } else {
            RadioButton radio10e = (RadioButton) group_10e.findViewById(choice10e);
            question10e = radio10e.getText().toString();
            group10e_tv.setError(null);
        }
        return q1 && q2 && q3 &&q4 &&q5a && q5b && q5c && q5d && q5e && q5f && q5g && q5h &&
                q5i &&q6 && q7 && q8 && q9 && q10 && q10a && q10b &&q10c
                && q10d && q10e;
    }


    public void uploadRemotely(){
        androidID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        dbHelper = new LocalSQLiteDBHelper(getContext());
        switchDriveController = new SwitchDriveController(getContext().getString(R.string.server_address),
                getContext().getString(R.string.token), getContext().getString(R.string.password));
        localController = SQLiteController.getInstance(getContext());


        String query = "SELECT * FROM usersTable";
        Cursor records = localController.rawQuery(query, null);
        records.moveToFirst();

        String username = null;

        if (records.getCount() > 0){
            username = records.getString(records.getColumnIndex(UserTable.USERNAME));

        }

        String userName = username + "_" + androidID;
        final Uploader uploader = new Uploader(userName, switchDriveController, localController, dbHelper);
        uploader.oneTimeUpload();
    }
}