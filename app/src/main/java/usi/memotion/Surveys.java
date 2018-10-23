package usi.memotion;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.tables.SleepQualityTable;

public class Surveys extends AppCompatActivity {

    private EditText early_morning_q1;
    private EditText early_morning_q2;
    private RadioGroup early_morning_q3;
    private RadioGroup early_morning_q4;

    private LocalStorageController localController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localController = SQLiteController.getInstance(Surveys.this);
        LayoutInflater inflater = (this).getLayoutInflater();

        AlertDialog.Builder builderSleep = new AlertDialog.Builder(this);
        builderSleep.setCancelable(false);
        builderSleep.setTitle("Sleep");
        builderSleep.setView(inflater.inflate(R.layout.early_morning_questionnaire_sleep, null));
        builderSleep.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        early_morning_q1 = (EditText) ((AlertDialog)dialog).findViewById(R.id.early_morning_q1);
                        early_morning_q2 = (EditText) ((AlertDialog)dialog).findViewById(R.id.early_morning_q2);
                        early_morning_q3 = (RadioGroup) ((AlertDialog)dialog).findViewById(R.id.early_morning_q3);
                        early_morning_q4 = (RadioGroup) ((AlertDialog)dialog).findViewById(R.id.early_morning_q4);
                        ContentValues record = new ContentValues();
                        record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                        record.put(SleepQualityTable.QUESTION_1, early_morning_q1.getText().toString());
                        record.put(SleepQualityTable.QUESTION_2, early_morning_q2.getText().toString());
                        int choiceq3 = early_morning_q3.getCheckedRadioButtonId();
                        String answer3;
                        if (choiceq3 > 0) {
                            RadioButton radio3 = (RadioButton) early_morning_q3.findViewById(choiceq3);
                            answer3 = radio3.getText().toString();
                        } else {
                            answer3 = "UNKNOWN";
                        }
                        record.put(SleepQualityTable.QUESTION_3, answer3);
                        int choiceq4 = early_morning_q4.getCheckedRadioButtonId();
                        String answer4;
                        if (choiceq4 > 0) {
                            RadioButton radio4 = (RadioButton) early_morning_q4.findViewById(choiceq4);
                            answer4 = radio4.getText().toString();
                        } else {
                            answer4 = "UNKNOWN";
                        }
                        record.put(SleepQualityTable.QUESTION_4, answer4);

                        localController.insertRecord(SleepQualityTable.TABLE_SLEEP_QUALITY_SURVEY, record);

                        Log.d("SLEEP QUALITY SURVEYS", "Added record: ts: " + record.get(SleepQualityTable.TIMESTAMP));

                        dialog.dismiss();
                    }
                });
        builderSleep.create();
        builderSleep.show();
    }
}
