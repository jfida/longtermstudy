package usi.memotion.UI.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Toast;

import usi.memotion.R;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.tables.PSQISurveyTable;
import usi.memotion.local.database.tables.PSSSurveyTable;

public class PSQISurveyFragment extends Fragment {
    private String question1, question2, question3, question4, question5a,
            question5b, question5c, question5d, question5e, question5f, question5g, question5h, question5i,
            question5j,question5j_text, question6, question7, question8, question9, question10,
            question10a, question10b, question10c, question10d, question10e;

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

    public PSQISurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_psqi_survey, container, false);

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

        submitButton = (Button) root.findViewById(R.id.submit_psqi);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswers();
                ContentValues record = new ContentValues();
                record.put(PSQISurveyTable.TIMESTAMP, System.currentTimeMillis());
                record.put(PSQISurveyTable.QUESTION_1, System.currentTimeMillis());

                localcontroller.insertRecord(PSQISurveyTable.TABLE_PSQUI_SURVEY, record);

                Log.d("PSQI SURVEYS", "Added record: ts: " + record.get(PSQISurveyTable.TIMESTAMP));


                //Show thank you message
                Toast.makeText(getContext(), "Thank you very much!", Toast.LENGTH_SHORT).show();

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
        });

        return root;
    }

    void getAnswers() {
        question1 = psqi_q1_answer.getText().toString();
        question2 = psqi_q2_answer.getText().toString();
        question3 = psqi_q3_answer.getText().toString();
        question4 = psqi_q4_answer.getText().toString();

        int choice5a = group_5a.getCheckedRadioButtonId();
        RadioButton radio5a = (RadioButton) group_5a.findViewById(choice5a);
        question5a = radio5a.getText().toString();

        int choice5b = group_5b.getCheckedRadioButtonId();
        RadioButton radio5b = (RadioButton) group_5b.findViewById(choice5b);
        question5b = radio5b.getText().toString();

        int choice5c = group_5c.getCheckedRadioButtonId();
        RadioButton radio5c = (RadioButton) group_5c.findViewById(choice5c);
        question5c = radio5c.getText().toString();

        int choice5d = group_5d.getCheckedRadioButtonId();
        RadioButton radio5d = (RadioButton) group_5d.findViewById(choice5d);
        question5d = radio5d.getText().toString();

        int choice5e = group_5e.getCheckedRadioButtonId();
        RadioButton radio5e = (RadioButton) group_5e.findViewById(choice5e);
        question5e = radio5e.getText().toString();

        int choice5f = group_5f.getCheckedRadioButtonId();
        RadioButton radio5f = (RadioButton) group_5f.findViewById(choice5f);
        question5f = radio5f.getText().toString();

        int choice5g = group_5g.getCheckedRadioButtonId();
        RadioButton radio5g = (RadioButton) group_5g.findViewById(choice5g);
        question5g = radio5g.getText().toString();

        int choice5h = group_5h.getCheckedRadioButtonId();
        RadioButton radio5h = (RadioButton) group_5h.findViewById(choice5h);
        question5h = radio5h.getText().toString();

        int choice5i = group_5i.getCheckedRadioButtonId();
        RadioButton radio5i = (RadioButton) group_5i.findViewById(choice5i);
        question5i = radio5i.getText().toString();

        int choice5j = group_5j.getCheckedRadioButtonId();
        RadioButton radio5j = (RadioButton) group_5j.findViewById(choice5j);
        question5j = radio5j.getText().toString();
        question5j_text = psqi_q5j_text.getText().toString();

        int choice6 = group_6.getCheckedRadioButtonId();
        RadioButton radio6 = (RadioButton) group_6.findViewById(choice6);
        question6 = radio6.getText().toString();

        int choice7 = group_7.getCheckedRadioButtonId();
        RadioButton radio7 = (RadioButton) group_7.findViewById(choice7);
        question7 = radio7.getText().toString();

        int choice8 = group_8.getCheckedRadioButtonId();
        RadioButton radio8 = (RadioButton) group_8.findViewById(choice8);
        question8 = radio8.getText().toString();

        int choice9 = group_9.getCheckedRadioButtonId();
        RadioButton radio9 = (RadioButton) group_9.findViewById(choice9);
        question9 = radio9.getText().toString();

        int choice10 = group_10.getCheckedRadioButtonId();
        RadioButton radio10 = (RadioButton) group_10.findViewById(choice10);
        question10 = radio10.getText().toString();

        int choice10a = group_10a.getCheckedRadioButtonId();
        RadioButton radio10a = (RadioButton) group_10a.findViewById(choice10a);
        question10a = radio10a.getText().toString();

        int choice10b = group_10b.getCheckedRadioButtonId();
        RadioButton radio10b = (RadioButton) group_10b.findViewById(choice10b);
        question10b = radio10b.getText().toString();

        int choice10c = group_10c.getCheckedRadioButtonId();
        RadioButton radio10c = (RadioButton) group_10c.findViewById(choice10c);
        question10c = radio10c.getText().toString();

        int choice10d = group_10d.getCheckedRadioButtonId();
        RadioButton radio10d = (RadioButton) group_10d.findViewById(choice10d);
        question10d = radio10d.getText().toString();

        int choice10e = group_10e.getCheckedRadioButtonId();
        RadioButton radio10e = (RadioButton) group_10e.findViewById(choice10e);
        question10e = radio10e.getText().toString();

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
        record.put(PSQISurveyTable.QUESTION_5h, question5j);
        record.put(PSQISurveyTable.QUESTION_5j, question5j);
        record.put(PSQISurveyTable.QUESTION_5j_text, question5j_text);
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
    }
}
