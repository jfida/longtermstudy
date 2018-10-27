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
import android.widget.Toast;

import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.tables.FatigueSurveyTable;
import usi.memotion.local.database.tables.OverallSurveyTable;
import usi.memotion.local.database.tables.ProductivitySurveyTable;
import usi.memotion.local.database.tables.SleepQualityTable;
import usi.memotion.local.database.tables.StressSurveyTable;

/**
 * Created by biancastancu
 */

public class Surveys extends AppCompatActivity {

    private EditText early_morning_q1;
    private EditText early_morning_q2;
    private RadioGroup early_morning_q3;
    private RadioGroup early_morning_q4;

    private RadioGroup fatigue_q1;

    private RadioGroup morning_overall_q1;
    private RadioGroup afternoon_overall_q1;

    private RadioGroup productivity_morning_q1;
    private RadioGroup productivity_afternoon_q1;
    private RadioGroup productivity_morning_q2;
    private RadioGroup productivity_afternoon_q2;
    private EditText productivity_morning_q3;
    private EditText productivity_afternoon_q3;
    private EditText productivity_morning_q4;
    private EditText productivity_afternoon_q4;

    private RadioGroup stress_morning_q1;
    private RadioGroup stress_afternoon_q1;


    private LocalStorageController localController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localController = SQLiteController.getInstance(Surveys.this);
        LayoutInflater inflater = (this).getLayoutInflater();

        if (getIntent().getStringExtra("type_daily") != null) {
            if (getIntent().getStringExtra("type_daily").equals(getString(R.string.early_morning))) {
                final AlertDialog.Builder builderSleep = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);

                builderSleep.setCancelable(false);
                builderFatigue.setCancelable(false);

                builderSleep.setTitle(getString(R.string.sleep_quality));
                builderFatigue.setTitle(getString(R.string.fatigue));

                builderSleep.setView(inflater.inflate(R.layout.early_morning_questionnaire_sleep, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));

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

                        builderFatigue.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
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

                                sayThankYou();

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
            } else if (getIntent().getStringExtra("type_daily").equals(getString(R.string.morning))) {
                final AlertDialog.Builder builderOverall = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderProductivity = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderStress = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);

                builderOverall.setCancelable(false);
                builderProductivity.setCancelable(false);
                builderStress.setCancelable(false);
                builderFatigue.setCancelable(false);

                builderOverall.setTitle(getString(R.string.overall));
                builderProductivity.setTitle(getString(R.string.productivity));
                builderStress.setTitle(getString(R.string.stress));
                builderFatigue.setTitle(getString(R.string.fatigue));

                builderOverall.setView(inflater.inflate(R.layout.morning_questionnaire_overall, null));
                builderProductivity.setView(inflater.inflate(R.layout.morning_productivity_survey, null));
                builderStress.setView(inflater.inflate(R.layout.morning_questionnaire_stress, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));

                builderOverall.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        morning_overall_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.morning_overall_q1);
                        ContentValues record = new ContentValues();
                        record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                        int choiceq1 = morning_overall_q1.getCheckedRadioButtonId();
                        String answer1;
                        if (choiceq1 > 0) {
                            RadioButton radio1 = (RadioButton) morning_overall_q1.findViewById(choiceq1);
                            answer1 = radio1.getText().toString();
                        } else {
                            answer1 = "UNKNOWN";
                        }
                        record.put(OverallSurveyTable.QUESTION_1, answer1);

                        localController.insertRecord(OverallSurveyTable.TABLE_OVERALL_SURVEY, record);

                        Log.d("OVERALL SURVEYS", "Added record: ts: " + record.get(OverallSurveyTable.TIMESTAMP));

                        dialog.dismiss();

                        builderProductivity.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                productivity_morning_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q1);
                                productivity_morning_q2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q2);
                                productivity_morning_q3 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q3);
                                productivity_morning_q4 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q4);

                                ContentValues record = new ContentValues();
                                record.put(ProductivitySurveyTable.TIMESTAMP, System.currentTimeMillis());
                                int choiceq1 = productivity_morning_q1.getCheckedRadioButtonId();
                                String answer1;
                                if (choiceq1 > 0) {
                                    RadioButton radio1 = (RadioButton) productivity_morning_q1.findViewById(choiceq1);
                                    answer1 = radio1.getText().toString();
                                } else {
                                    answer1 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_1, answer1);
                                int choiceq2 = productivity_morning_q2.getCheckedRadioButtonId();
                                String answer2;
                                if (choiceq2 > 0) {
                                    RadioButton radio2 = (RadioButton) productivity_morning_q2.findViewById(choiceq2);
                                    answer2 = radio2.getText().toString();
                                } else {
                                    answer2 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_2, answer2);
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_morning_q3.getText().toString());
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_morning_q4.getText().toString());

                                localController.insertRecord(ProductivitySurveyTable.TABLE_PRODUCTIVITY_SURVEY, record);

                                Log.d("PRODUCTIVITY SURVEYS", "Added record: ts: " + record.get(ProductivitySurveyTable.TIMESTAMP));

                                dialog.dismiss();

                                builderStress.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        stress_morning_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.stress_morning_q1);
                                        ContentValues record = new ContentValues();
                                        record.put(StressSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                        int choiceq1 = stress_morning_q1.getCheckedRadioButtonId();
                                        String answer1;
                                        if (choiceq1 > 0) {
                                            RadioButton radio1 = (RadioButton) stress_morning_q1.findViewById(choiceq1);
                                            answer1 = radio1.getText().toString();
                                        } else {
                                            answer1 = "UNKNOWN";
                                        }
                                        record.put(StressSurveyTable.QUESTION_1, answer1);

                                        localController.insertRecord(StressSurveyTable.TABLE_STRESS_SURVEY, record);

                                        Log.d("STRESS SURVEYS", "Added record: ts: " + record.get(StressSurveyTable.TIMESTAMP));

                                        dialog.dismiss();

                                        builderFatigue.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int id) {
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

                                                sayThankYou();
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
                                builderStress.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Surveys.this.finish();
                                    }
                                });
                                builderStress.create();
                                builderStress.show();
                            }
                        });
                        builderProductivity.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Surveys.this.finish();
                            }
                        });
                        builderProductivity.create();
                        builderProductivity.show();
                    }
                });
                builderOverall.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Surveys.this.finish();
                    }
                });
                builderOverall.create();
                builderOverall.show();
            } else if (getIntent().getStringExtra("type_daily").equals(getString(R.string.afternoon))) {
                final AlertDialog.Builder builderOverall = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderProductivity = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderStress = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);

                builderOverall.setCancelable(false);
                builderProductivity.setCancelable(false);
                builderStress.setCancelable(false);
                builderFatigue.setCancelable(false);

                builderOverall.setTitle(getString(R.string.overall));
                builderProductivity.setTitle(getString(R.string.productivity));
                builderStress.setTitle(getString(R.string.stress));
                builderFatigue.setTitle(getString(R.string.fatigue));

                builderOverall.setView(inflater.inflate(R.layout.afternoon_questionnaire_overall, null));
                builderProductivity.setView(inflater.inflate(R.layout.afternoon_productivity_survey, null));
                builderStress.setView(inflater.inflate(R.layout.afternoon_questionnaire_stress, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));

                builderOverall.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        afternoon_overall_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.afternoon_overall_q1);
                        ContentValues record = new ContentValues();
                        record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                        int choiceq1 = afternoon_overall_q1.getCheckedRadioButtonId();
                        String answer1;
                        if (choiceq1 > 0) {
                            RadioButton radio1 = (RadioButton) afternoon_overall_q1.findViewById(choiceq1);
                            answer1 = radio1.getText().toString();
                        } else {
                            answer1 = "UNKNOWN";
                        }
                        record.put(OverallSurveyTable.QUESTION_1, answer1);

                        localController.insertRecord(OverallSurveyTable.TABLE_OVERALL_SURVEY, record);

                        Log.d("OVERALL SURVEYS", "Added record: ts: " + record.get(OverallSurveyTable.TIMESTAMP));

                        dialog.dismiss();

                        builderProductivity.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                productivity_afternoon_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q1);
                                productivity_afternoon_q2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q2);
                                productivity_afternoon_q3 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q3);
                                productivity_afternoon_q4 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q4);

                                ContentValues record = new ContentValues();
                                record.put(ProductivitySurveyTable.TIMESTAMP, System.currentTimeMillis());
                                int choiceq1 = productivity_afternoon_q1.getCheckedRadioButtonId();
                                String answer1;
                                if (choiceq1 > 0) {
                                    RadioButton radio1 = (RadioButton) productivity_afternoon_q1.findViewById(choiceq1);
                                    answer1 = radio1.getText().toString();
                                } else {
                                    answer1 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_1, answer1);
                                int choiceq2 = productivity_afternoon_q2.getCheckedRadioButtonId();
                                String answer2;
                                if (choiceq2 > 0) {
                                    RadioButton radio2 = (RadioButton) productivity_afternoon_q2.findViewById(choiceq2);
                                    answer2 = radio2.getText().toString();
                                } else {
                                    answer2 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_2, answer2);
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_afternoon_q3.getText().toString());
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_afternoon_q4.getText().toString());

                                localController.insertRecord(ProductivitySurveyTable.TABLE_PRODUCTIVITY_SURVEY, record);

                                Log.d("PRODUCTIVITY SURVEYS", "Added record: ts: " + record.get(ProductivitySurveyTable.TIMESTAMP));

                                dialog.dismiss();

                                builderStress.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        stress_afternoon_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.stress_afternoon_q1);
                                        ContentValues record = new ContentValues();
                                        record.put(StressSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                        int choiceq1 = stress_afternoon_q1.getCheckedRadioButtonId();
                                        String answer1;
                                        if (choiceq1 > 0) {
                                            RadioButton radio1 = (RadioButton) stress_afternoon_q1.findViewById(choiceq1);
                                            answer1 = radio1.getText().toString();
                                        } else {
                                            answer1 = "UNKNOWN";
                                        }
                                        record.put(StressSurveyTable.QUESTION_1, answer1);

                                        localController.insertRecord(StressSurveyTable.TABLE_STRESS_SURVEY, record);

                                        Log.d("STRESS SURVEYS", "Added record: ts: " + record.get(StressSurveyTable.TIMESTAMP));

                                        dialog.dismiss();

                                        builderFatigue.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
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
                                                sayThankYou();
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
                                builderStress.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Surveys.this.finish();
                                    }
                                });
                                builderStress.create();
                                builderStress.show();
                            }
                        });
                        builderProductivity.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Surveys.this.finish();
                            }
                        });
                        builderProductivity.create();
                        builderProductivity.show();
                    }
                });
                builderOverall.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Surveys.this.finish();
                    }
                });
                builderOverall.create();
                builderOverall.show();
            }
        } else {
            Surveys.this.finish();
        }
    }

    private void sayThankYou() {
        AlertDialog.Builder builderThankYou = new AlertDialog.Builder(Surveys.this);
        builderThankYou.setMessage(getString(R.string.thank_you));
        builderThankYou.setCancelable(false);
        builderThankYou.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }
}
