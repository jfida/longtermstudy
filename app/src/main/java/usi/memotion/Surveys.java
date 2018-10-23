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
import usi.memotion.local.database.tables.FatigueSurveyTable;
import usi.memotion.local.database.tables.SleepQualityTable;

public class Surveys extends AppCompatActivity {

    private EditText early_morning_q1;
    private EditText early_morning_q2;
    private RadioGroup early_morning_q3;
    private RadioGroup early_morning_q4;
    private RadioGroup fatigue_q1;

    private LocalStorageController localController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("type_daily")!= null) {
            if (getIntent().getStringExtra("type_daily").equals(getString(R.string.early_morning))) {

                localController = SQLiteController.getInstance(Surveys.this);
                LayoutInflater inflater = (this).getLayoutInflater();

                AlertDialog.Builder builderSleep = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);

                builderSleep.setCancelable(false);
                builderFatigue.setCancelable(false);

                builderSleep.setTitle(getString(R.string.sleep_quality));
                builderFatigue.setTitle(getString(R.string.fatigue));

                builderSleep.setView(inflater.inflate(R.layout.early_morning_questionnaire_sleep, null));
                builderFatigue.setView(inflater.inflate(R.layout.early_morning_questionnaire_fatigue, null));

                builderSleep.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        early_morning_q1 = (EditText) ((AlertDialog) dialog).findViewById(R.id.early_morning_q1);
                        early_morning_q2 = (EditText) ((AlertDialog) dialog).findViewById(R.id.early_morning_q2);
                        early_morning_q3 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.early_morning_q3);
                        early_morning_q4 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.early_morning_q4);
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

                        builderFatigue.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                fatigue_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.fatigue_q1);
                                ContentValues record = new ContentValues();
                                record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                                int choiceq1 = fatigue_q1.getCheckedRadioButtonId();
                                String answer1;
                                if (choiceq1 > 0) {
                                    RadioButton radio1 = (RadioButton) fatigue_q1.findViewById(choiceq1);
                                    answer1 = radio1.getText().toString();
                                } else {
                                    answer1 = "UNKNOWN";
                                }
                                record.put(FatigueSurveyTable.QUESTION_1, answer1);

                                localController.insertRecord(FatigueSurveyTable.TABLE_FATIGUE_SURVEY, record);

                                Log.d("FATIGUE SURVEYS", "Added record: ts: " + record.get(FatigueSurveyTable.TIMESTAMP));

                                dialog.dismiss();

                            }
                        });
                        builderFatigue.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Surveys.this.finish();
                            }
                        });

                        builderFatigue.create();
                        builderFatigue.show();
                    }
                });
                builderSleep.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Surveys.this.finish();
                    }
                });
                builderSleep.create();
                builderSleep.show();
            } else if(getIntent().getStringExtra("type_daily").equals(getString(R.string.morning))){

            } else if(getIntent().getStringExtra("type_daily").equals(getString(R.string.afternoon))){
            }
        } else {
            Surveys.this.finish();
        }
    }
}
