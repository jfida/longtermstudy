package usi.memotion.UI.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import usi.memotion.R;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.db.LocalSQLiteDBHelper;
import usi.memotion.local.database.tables.AnxietySurveyTable;
import usi.memotion.local.database.tables.LectureSurveyTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnxietySurveyFragment extends Fragment {

    Button submitSurveyButton;
    private RadioGroup question1Options, question2Options, question3Options, question4Options, question5Options, question6Options, question7Options;
    int question1Selection = 0, question2Selection = 0, question3Selection = 0, question4Selection = 0, question5Selection = 0, question6Selection = 0, question7Selection = 0;

    LocalSQLiteDBHelper dbHelper;

    Date currentTimestamp;

    private LocalStorageController localcontroller;


    public AnxietySurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_anxiety_survey, container, false);
        localcontroller = SQLiteController.getInstance(getContext());
        dbHelper = new LocalSQLiteDBHelper(getContext());

        currentTimestamp = Calendar.getInstance().getTime();

        submitSurveyButton = (Button) root.findViewById(R.id.gad_submit);
        question1Options = (RadioGroup) root.findViewById(R.id.gad_question1_options);
        question2Options = (RadioGroup) root.findViewById(R.id.gad_question2_options);
        question3Options = (RadioGroup) root.findViewById(R.id.gad_question3_options);
        question4Options = (RadioGroup) root.findViewById(R.id.gad_question4_options);
        question5Options = (RadioGroup) root.findViewById(R.id.gad_question5_options);
        question6Options = (RadioGroup) root.findViewById(R.id.gad_question6_options);
        question7Options = (RadioGroup) root.findViewById(R.id.gad_question7_options);


        setUpQuestion1RadioButtons();
        setUpQuestion2RadioButtons();
        setUpQuestion3RadioButtons();
        setUpQuestion4RadioButtons();
        setUpQuestion5RadioButtons();
        setUpQuestion6RadioButtons();
        setUpQuestion7RadioButtons();

        submitSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ask if they are sure to add or update the data
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Submit Survey Answers");
//                alertDialog.setIcon(R.drawable.logo3);

                if(question1Selection == 0 & question2Selection == 0 & question3Selection == 0 & question4Selection == 0 & question5Selection == 0 & question6Selection == 0 & question7Selection == 0){
                    alertDialog.setMessage("Could you please provide some answers to this survey?");

                    alertDialog.setNegativeButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.setPositiveButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Close this activity
                                    getActivity().finish();
                                }
                            });

                }else{
                    alertDialog.setMessage("Do you want to save these answers?");
                    alertDialog.setNegativeButton(R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    ContentValues record = new ContentValues();
                                    record.put(AnxietySurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(AnxietySurveyTable.QUESTION_1, question1Selection);
                                    record.put(AnxietySurveyTable.QUESTION_2, question2Selection);
                                    record.put(AnxietySurveyTable.QUESTION_3, question3Selection);
                                    record.put(AnxietySurveyTable.QUESTION_4, question4Selection);
                                    record.put(AnxietySurveyTable.QUESTION_5, question5Selection);
                                    record.put(AnxietySurveyTable.QUESTION_6, question6Selection);
                                    record.put(AnxietySurveyTable.QUESTION_7, question7Selection);
                                    localcontroller.insertRecord(AnxietySurveyTable.TABLE_ANXIETY_SURVEY, record);

                                    Log.d("ANXIETY SURVEYS", "Added record: ts: " + record.get(AnxietySurveyTable.TIMESTAMP) + ", Q1: " + record.get(AnxietySurveyTable.QUESTION_1) + ", Q2: " + record.get(AnxietySurveyTable.QUESTION_2)  + ", Q3: " + record.get(AnxietySurveyTable.QUESTION_3));


                                    //Show thank you message
                                    Toast.makeText(getContext(), "Thank you very much!", Toast.LENGTH_SHORT).show();

                                    Fragment newFragment = new HomeFragment();
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.content_frame, newFragment);
                                    ft.commit();

                                }
                            });

                    alertDialog.setPositiveButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                }
                alertDialog.show();
            }
        });



        return root;
    }


    /*
     *   Question 1 -  Radio Buttons
     */
    private void setUpQuestion1RadioButtons(){

        question1Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gad_answer11:
                        question1Selection = 0;
                        break;
                    case R.id.gad_answer12:
                        question1Selection = 1;
                        break;
                    case R.id.gad_answer13:
                        question1Selection = 2;
                        break;
                    case R.id.gad_answer14:
                        question1Selection = 3;
                        break;
                    default:
                        question1Selection = 0;
                }
            }
        });
    }

    /*
     *   Question 2 -  Radio Buttons
     */
    private void setUpQuestion2RadioButtons(){

        question2Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gad_answer21:
                        question2Selection = 0;
                        break;
                    case R.id.gad_answer22:
                        question2Selection = 1;
                        break;
                    case R.id.gad_answer23:
                        question2Selection = 2;
                        break;
                    case R.id.gad_answer24:
                        question2Selection = 3;
                        break;
                    default:
                        question2Selection = 0;
                }
            }
        });
    }

    /*
     *   Question 3 -  Radio Buttons
     */
    private void setUpQuestion3RadioButtons(){

        question3Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gad_answer31:
                        question3Selection = 0;
                        break;
                    case R.id.gad_answer32:
                        question3Selection = 1;
                        break;
                    case R.id.gad_answer33:
                        question3Selection = 2;
                        break;
                    case R.id.gad_answer34:
                        question3Selection = 3;
                        break;
                    default:
                        question3Selection = 0;
                }
            }
        });
    }

    /*
     *   Question 4 -  Radio Buttons
     */
    private void setUpQuestion4RadioButtons(){

        question4Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gad_answer41:
                        question4Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.gad_answer42:
                        question4Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.gad_answer43:
                        question4Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.gad_answer44:
                        question4Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    default:
                        question4Selection = 0;
                }
            }
        });
    }

    /*
     *   Question 5 -  Radio Buttons
     */
    private void setUpQuestion5RadioButtons(){

        question5Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gad_answer51:
                        question5Selection = 0;
                        break;
                    case R.id.gad_answer52:
                        question5Selection = 1;
                        break;
                    case R.id.gad_answer53:
                        question5Selection = 2;
                        break;
                    case R.id.gad_answer54:
                        question5Selection = 3;
                        break;
                    default:
                        question5Selection = 0;
                }
            }
        });
    }

    /*
     *   Question 6 -  Radio Buttons
     */
    private void setUpQuestion6RadioButtons(){

        question6Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gad_answer61:
                        question6Selection = 0;
                        break;
                    case R.id.gad_answer62:
                        question6Selection = 1;
                        break;
                    case R.id.gad_answer63:
                        question6Selection = 2;
                        break;
                    case R.id.gad_answer64:
                        question6Selection = 3;
                        break;
                    default:
                        question6Selection = 100;
                }
            }
        });
    }

    /*
     *   Question 7 -  Radio Buttons
     */
    private void setUpQuestion7RadioButtons(){

        question7Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gad_answer71:
                        question7Selection = 0;
                        break;
                    case R.id.gad_answer72:
                        question7Selection = 1;
                        break;
                    case R.id.gad_answer73:
                        question7Selection = 2;
                        break;
                    case R.id.gad_answer74:
                        question7Selection = 3;
                        break;
                    default:
                        question7Selection = 100;
                }
            }
        });
    }


}
