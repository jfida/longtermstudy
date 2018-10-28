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

import usi.memotion.UI.fragments.PamSurveyView;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.tables.FatigueSurveyTable;
import usi.memotion.local.database.tables.OverallSurveyTable;
import usi.memotion.local.database.tables.PAMSurveyTable;
import usi.memotion.local.database.tables.PAMTable;
import usi.memotion.local.database.tables.ProductivitySurveyTable;
import usi.memotion.local.database.tables.SleepQualityTable;
import usi.memotion.local.database.tables.StressSurveyTable;
import usi.memotion.local.database.tables.WeeklySurveyTable;

/**
 * Created by biancastancu
 */

public class Surveys extends AppCompatActivity {

    private LocalStorageController localController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localController = SQLiteController.getInstance(Surveys.this);
        LayoutInflater inflater = (this).getLayoutInflater();

        if (getIntent().getStringExtra("type_survey") != null) {
            if (getIntent().getStringExtra("type_survey").equals(getString(R.string.early_morning))) {
                final AlertDialog.Builder builderSleep = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);

                builderSleep.setCancelable(false);
                builderFatigue.setCancelable(false);
                builderPAM.setCancelable(false);

                builderSleep.setTitle(getString(R.string.sleep_quality));
                builderFatigue.setTitle(getString(R.string.fatigue));
                builderPAM.setTitle(getString(R.string.PAM));

                builderSleep.setView(inflater.inflate(R.layout.early_morning_questionnaire_sleep, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));
                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));

                builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AlertDialog)dialogInterface).dismiss();
                        builderFatigue.create();
                        builderFatigue.show();
                    }
                });
                builderFatigue.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup fatigue_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.fatigue_q1);
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

                        sayThankYou(getString(R.string.thank_you));

                    }
                });

                builderSleep.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText early_morning_q1 = (EditText) ((AlertDialog) dialog).findViewById(R.id.early_morning_q1);
                        EditText early_morning_q2 = (EditText) ((AlertDialog) dialog).findViewById(R.id.early_morning_q2);
                        RadioGroup early_morning_q3 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.early_morning_q3);
                        RadioGroup early_morning_q4 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.early_morning_q4);
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
                        final AlertDialog PAMdialog = builderPAM.create();
                        PAMdialog.show();
                        if (PAMdialog.isShowing()) {
                            PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                            PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                                @Override
                                public void onPAMSelection(int position) {
                                    ContentValues record = new ContentValues();
                                    record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                                    localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                                    Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                                    PAMdialog.dismiss();
                                    builderFatigue.create();
                                    builderFatigue.show();
                                }
                            };
                            view.setOnPAMSelectionListener(changeListener);
                        }
                    }
                });
                builderSleep.create();
                builderSleep.show();
            } else if (getIntent().getStringExtra("type_survey").equals(getString(R.string.morning))) {
                final AlertDialog.Builder builderOverall = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderProductivity = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderStress = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);

                builderOverall.setCancelable(false);
                builderProductivity.setCancelable(false);
                builderStress.setCancelable(false);
                builderFatigue.setCancelable(false);
                builderPAM.setCancelable(false);

                builderOverall.setTitle(getString(R.string.overall));
                builderProductivity.setTitle(getString(R.string.productivity));
                builderStress.setTitle(getString(R.string.stress));
                builderFatigue.setTitle(getString(R.string.fatigue));
                builderPAM.setTitle(getString(R.string.PAM));

                builderOverall.setView(inflater.inflate(R.layout.morning_questionnaire_overall, null));
                builderProductivity.setView(inflater.inflate(R.layout.morning_productivity_survey, null));
                builderStress.setView(inflater.inflate(R.layout.morning_questionnaire_stress, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));
                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));

                builderOverall.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup morning_overall_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.morning_overall_q1);
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
                                RadioGroup productivity_morning_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q1);
                                RadioGroup productivity_morning_q2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q2);
                                EditText productivity_morning_q3 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q3);
                                EditText productivity_morning_q4 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q4);

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
                                        RadioGroup stress_morning_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.stress_morning_q1);
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
                                                RadioGroup fatigue_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.fatigue_q1);
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

                                                sayThankYou(getString(R.string.thank_you));
                                            }
                                        });
                                        builderFatigue.create();
                                        builderFatigue.show();
                                    }
                                });
                                builderStress.create();
                                builderStress.show();
                            }
                        });

                        builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((AlertDialog)dialogInterface).dismiss();
                                builderProductivity.create();
                                builderProductivity.show();
                            }
                        });

                        final AlertDialog PAMdialog = builderPAM.create();
                        PAMdialog.show();
                        if (PAMdialog.isShowing()) {
                            PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                            PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                                @Override
                                public void onPAMSelection(int position) {
                                    ContentValues record = new ContentValues();
                                    record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                                    localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                                    Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                                    PAMdialog.dismiss();
                                    builderProductivity.create();
                                    builderProductivity.show();
                                }
                            };
                            view.setOnPAMSelectionListener(changeListener);
                        }
                    }
                });
                builderOverall.create();
                builderOverall.show();
            } else if (getIntent().getStringExtra("type_survey").equals(getString(R.string.afternoon))) {
                final AlertDialog.Builder builderOverall = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderProductivity = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderStress = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);

                builderOverall.setCancelable(false);
                builderProductivity.setCancelable(false);
                builderStress.setCancelable(false);
                builderFatigue.setCancelable(false);
                builderPAM.setCancelable(false);

                builderOverall.setTitle(getString(R.string.overall));
                builderProductivity.setTitle(getString(R.string.productivity));
                builderStress.setTitle(getString(R.string.stress));
                builderFatigue.setTitle(getString(R.string.fatigue));
                builderPAM.setTitle(getString(R.string.PAM));

                builderOverall.setView(inflater.inflate(R.layout.afternoon_questionnaire_overall, null));
                builderProductivity.setView(inflater.inflate(R.layout.afternoon_productivity_survey, null));
                builderStress.setView(inflater.inflate(R.layout.afternoon_questionnaire_stress, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));
                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));

                builderOverall.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup afternoon_overall_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.afternoon_overall_q1);
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
                                RadioGroup productivity_afternoon_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q1);
                                RadioGroup productivity_afternoon_q2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q2);
                                EditText productivity_afternoon_q3 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q3);
                                EditText productivity_afternoon_q4 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q4);

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
                                        RadioGroup stress_afternoon_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.stress_afternoon_q1);
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
                                                RadioGroup fatigue_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.fatigue_q1);
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
                                                sayThankYou(getString(R.string.thank_you));
                                            }
                                        });
                                        builderFatigue.create();
                                        builderFatigue.show();
                                    }
                                });
                                builderStress.create();
                                builderStress.show();
                            }
                        });

                        builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((AlertDialog)dialogInterface).dismiss();
                                builderProductivity.create();
                                builderProductivity.show();
                            }
                        });

                        final AlertDialog PAMdialog = builderPAM.create();
                        PAMdialog.show();
                        if (PAMdialog.isShowing()) {
                            PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                            PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                                @Override
                                public void onPAMSelection(int position) {
                                    ContentValues record = new ContentValues();
                                    record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                                    localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                                    Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                                    PAMdialog.dismiss();
                                    builderProductivity.create();
                                    builderProductivity.show();
                                }
                            };
                            view.setOnPAMSelectionListener(changeListener);
                        }
                    }
                });
                builderOverall.create();
                builderOverall.show();
            } else if (getIntent().getStringExtra("type_survey").equals(getString(R.string.weekly))) {
                final AlertDialog.Builder builderWeekly = new AlertDialog.Builder(this);
                builderWeekly.setCancelable(false);
                builderWeekly.setTitle(getString(R.string.weekly_survey));
                builderWeekly.setView(inflater.inflate(R.layout.weekly_questionnaire, null));
                builderWeekly.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ContentValues record = new ContentValues();
                        record.put(WeeklySurveyTable.TIMESTAMP, System.currentTimeMillis());

                        RadioGroup weeklyq1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq1);
                        RadioGroup weeklyq2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq2);
                        RadioGroup weeklyq3 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq3);
                        RadioGroup weeklyq4 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq4);
                        RadioGroup weeklyq5 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq5);
                        RadioGroup weeklyq6 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq6);
                        RadioGroup weeklyq7 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq7);
                        RadioGroup weeklyq8 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq8);
                        RadioGroup weeklyq9 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq9);
                        RadioGroup weeklyq10 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq10);
                        RadioGroup weeklyq11 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq11);
                        RadioGroup weeklyq12 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq12);
                        RadioGroup weeklyq13 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq13);
                        RadioGroup weeklyq14 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq14);

                        record.put(WeeklySurveyTable.QUESTION_1, getAnswer(weeklyq1));
                        record.put(WeeklySurveyTable.QUESTION_2, getAnswer(weeklyq2));
                        record.put(WeeklySurveyTable.QUESTION_3, getAnswer(weeklyq3));
                        record.put(WeeklySurveyTable.QUESTION_4, getAnswer(weeklyq4));
                        record.put(WeeklySurveyTable.QUESTION_5, getAnswer(weeklyq5));
                        record.put(WeeklySurveyTable.QUESTION_6, getAnswer(weeklyq6));
                        record.put(WeeklySurveyTable.QUESTION_7, getAnswer(weeklyq7));
                        record.put(WeeklySurveyTable.QUESTION_8, getAnswer(weeklyq8));
                        record.put(WeeklySurveyTable.QUESTION_9, getAnswer(weeklyq9));
                        record.put(WeeklySurveyTable.QUESTION_10, getAnswer(weeklyq10));
                        record.put(WeeklySurveyTable.QUESTION_11, getAnswer(weeklyq11));
                        record.put(WeeklySurveyTable.QUESTION_12, getAnswer(weeklyq12));
                        record.put(WeeklySurveyTable.QUESTION_13, getAnswer(weeklyq13));
                        record.put(WeeklySurveyTable.QUESTION_14, getAnswer(weeklyq14));

                        localController.insertRecord(WeeklySurveyTable.TABLE_WEEKLY_SURVEY, record);

                        Log.d("WEEKLY SURVEYS", "Added record: ts: " + record.get(WeeklySurveyTable.TIMESTAMP));

                        dialog.dismiss();
                        sayThankYou(getString(R.string.weekly_thank_you));
                    }
                });
                builderWeekly.create();
                builderWeekly.show();
            } else {
                Surveys.this.finish();
            }
        }
    }

        private void sayThankYou (String message) {
            AlertDialog.Builder builderThankYou = new AlertDialog.Builder(Surveys.this);
            builderThankYou.setMessage(message);
            builderThankYou.setCancelable(false);
            builderThankYou.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Surveys.this.finish();
                }
            });
            builderThankYou.create();
            builderThankYou.show();
        }

        private String getAnswer(RadioGroup radioGroup){
            int choice = radioGroup.getCheckedRadioButtonId();
            if (choice > 0) {
                RadioButton radio = (RadioButton) radioGroup.findViewById(choice);
                return radio.getText().toString();
            } else {
                return "UNKNOWN";
            }
        }
    }
