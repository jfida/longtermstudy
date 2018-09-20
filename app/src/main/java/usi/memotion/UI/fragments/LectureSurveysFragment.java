package usi.memotion.UI.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.Calendar;
import java.util.Date;

import usi.memotion.R;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.db.LocalSQLiteDBHelper;
import usi.memotion.local.database.tableHandlers.LectureSurvey;
import usi.memotion.local.database.tables.AccelerometerTable;
import usi.memotion.local.database.tables.LectureSurveyTable;
import usi.memotion.local.database.tables.UserTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class LectureSurveysFragment extends Fragment {

    ExpandableRelativeLayout expandableLayout0, expandableLayout1, expandableLayout2;

    Button submitSurveyButton;
    private RadioGroup question1Options;
    private RadioGroup question2Options;
    private RadioGroup question3Options;
    private RadioGroup question4Options;
    private RadioGroup question5Options;
    private RadioGroup question6Options;
    private RadioGroup question7Options;
    private RadioGroup question8Options;
    private RadioGroup question9Options;
    private RadioGroup question10Options;


    int question1Selection = 0;
    int question2Selection = 0;
    int question3Selection = 0;
    int question4Selection = 0;
    int question5Selection = 0;
    int question6Selection = 0;
    int question7Selection = 0;
    int question8Selection = 0;
    int question9Selection = 0;
    int question10Selection = 0;

    LocalSQLiteDBHelper dbHelper;

    Date currentTimestamp;

    private LocalStorageController localcontroller;

    public LectureSurveysFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("SurveyType fragment", "onCreateView");
        View root = inflater.inflate(R.layout.lecture_surveys_fragment, container, false);

        localcontroller = SQLiteController.getInstance(getContext());

        expandableLayout0 = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayout0);
        expandableLayout1 = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayout1);
        expandableLayout2 = (ExpandableRelativeLayout) root.findViewById(R.id.expandableLayout2);

        String session = getArguments().getString("LectureSession");
        if(session != null){
            if(session.equals("Wednesday - Pre") || session.equals("Friday - Pre"))
                    expandableLayout0.setExpanded(true);
            else if(session.equals("Wednesday - Break") || session.equals("Friday - Break"))
                expandableLayout1.setExpanded(true);
            else if(session.equals("Wednesday - Post") || session.equals("Friday - Post"))
                expandableLayout2.setExpanded(true);
        }


        dbHelper = new LocalSQLiteDBHelper(getContext());

        currentTimestamp = Calendar.getInstance().getTime();

        submitSurveyButton = (Button) root.findViewById(R.id.submit_lecture_survey);
        question1Options = (RadioGroup) root.findViewById(R.id.question1Options);
        question2Options = (RadioGroup) root.findViewById(R.id.question2Options);
        question3Options = (RadioGroup) root.findViewById(R.id.question3Options);
        question4Options = (RadioGroup) root.findViewById(R.id.question4Options);
        question5Options = (RadioGroup) root.findViewById(R.id.question5Options);
        question6Options = (RadioGroup) root.findViewById(R.id.question6Options);
        question7Options = (RadioGroup) root.findViewById(R.id.question7Options);
        question8Options = (RadioGroup) root.findViewById(R.id.question8Options);
        question9Options = (RadioGroup) root.findViewById(R.id.question9Options);
        question10Options = (RadioGroup) root.findViewById(R.id.question10Options);

        setUpQuestion1RadioButtons();
        setUpQuestion2RadioButtons();
        setUpQuestion3RadioButtons();
        setUpQuestion4RadioButtons();
        setUpQuestion5RadioButtons();
        setUpQuestion6RadioButtons();
        setUpQuestion7RadioButtons();
        setUpQuestion8RadioButtons();
        setUpQuestion9RadioButtons();
        setUpQuestion10RadioButtons();


        submitSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ask if they are sure to add or update the data
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Submit Survey Answers");
//                alertDialog.setIcon(R.drawable.logo3);

                if(question1Selection == 0 & question2Selection == 0 & question3Selection == 0 & question4Selection == 0 & question5Selection == 0 & question6Selection == 0 & question7Selection == 0 & question9Selection == 0 & question10Selection==0){
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
                                    record.put(LectureSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(LectureSurveyTable.QUESTION_1, question1Selection);
                                    record.put(LectureSurveyTable.QUESTION_2, question2Selection);
                                    record.put(LectureSurveyTable.QUESTION_3, question3Selection);
                                    record.put(LectureSurveyTable.QUESTION_4, question4Selection);
                                    record.put(LectureSurveyTable.QUESTION_5, question5Selection);
                                    record.put(LectureSurveyTable.QUESTION_6, question6Selection);
                                    record.put(LectureSurveyTable.QUESTION_7, question7Selection);
                                    record.put(LectureSurveyTable.QUESTION_8, question8Selection);
                                    record.put(LectureSurveyTable.QUESTION_9, question9Selection);
                                    record.put(LectureSurveyTable.QUESTION_10, question10Selection);
                                    localcontroller.insertRecord(LectureSurveyTable.TABLE_LECTURE_SURVEY, record);

                                    Log.d("LECTURE SURVEYS", "Added record: ts: " + record.get(LectureSurveyTable.TIMESTAMP) + ", Q1: " + record.get(LectureSurveyTable.QUESTION_1) + ", Q2: " + record.get(LectureSurveyTable.QUESTION_2)  + ", Q3: " + record.get(LectureSurveyTable.QUESTION_3));


                                    //Show thank you message
                                    Toast.makeText(getContext(), "Thank you very much!", Toast.LENGTH_SHORT).show();

//                                    //Close this activity
//                                    getActivity().finish();
                                    expandableLayout1.setExpanded(false);

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
                    case R.id.answer1:
                        question1Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer2:
                        question1Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer3:
                        question1Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer4:
                        question1Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer5:
                        question1Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer6:
                        question1Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer7:
                        question1Selection = LectureSurveyTable.ANSWER_7;
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
                    case R.id.answer12:
                        question2Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer22:
                        question2Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer32:
                        question2Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer42:
                        question2Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer52:
                        question2Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer62:
                        question2Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer72:
                        question2Selection = LectureSurveyTable.ANSWER_7;
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
                    case R.id.answer13:
                        question3Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer23:
                        question3Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer33:
                        question3Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer43:
                        question3Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer53:
                        question3Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer63:
                        question3Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer73:
                        question3Selection = LectureSurveyTable.ANSWER_7;
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
                    case R.id.answer14:
                        question4Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer24:
                        question4Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer34:
                        question4Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer44:
                        question4Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer54:
                        question4Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer64:
                        question4Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer74:
                        question4Selection = LectureSurveyTable.ANSWER_7;
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
                    case R.id.answer15:
                        question5Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer25:
                        question5Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer35:
                        question5Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer45:
                        question5Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer55:
                        question5Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer65:
                        question5Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer75:
                        question5Selection = LectureSurveyTable.ANSWER_7;
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
                    case R.id.answer16:
                        question6Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer26:
                        question6Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer36:
                        question6Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer46:
                        question6Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer56:
                        question6Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer66:
                        question6Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer76:
                        question6Selection = LectureSurveyTable.ANSWER_7;
                        break;
                    default:
                        question6Selection = 0;
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
                    case R.id.answer17:
                        question7Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer27:
                        question7Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer37:
                        question7Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer47:
                        question7Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer57:
                        question7Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer67:
                        question7Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer77:
                        question7Selection = LectureSurveyTable.ANSWER_7;
                        break;
                    default:
                        question7Selection = 0;
                }
            }
        });
    }

    /*
*   Question 8 -  Radio Buttons
*/
    private void setUpQuestion8RadioButtons(){

        question8Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.answer18:
                        question8Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer28:
                        question8Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer38:
                        question8Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer48:
                        question8Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer58:
                        question8Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer68:
                        question8Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer78:
                        question8Selection = LectureSurveyTable.ANSWER_7;
                        break;
                    default:
                        question8Selection = 0;
                }
            }
        });
    }

    /*
*   Question 9 -  Radio Buttons
*/
    private void setUpQuestion9RadioButtons(){

        question9Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.answer19:
                        question9Selection = LectureSurveyTable.ANSWER_1;
                        break;
                    case R.id.answer29:
                        question9Selection = LectureSurveyTable.ANSWER_2;
                        break;
                    case R.id.answer39:
                        question9Selection = LectureSurveyTable.ANSWER_3;
                        break;
                    case R.id.answer49:
                        question9Selection = LectureSurveyTable.ANSWER_4;
                        break;
                    case R.id.answer59:
                        question9Selection = LectureSurveyTable.ANSWER_5;
                        break;
                    case R.id.answer69:
                        question9Selection = LectureSurveyTable.ANSWER_6;
                        break;
                    case R.id.answer79:
                        question9Selection = LectureSurveyTable.ANSWER_7;
                        break;
                    default:
                        question9Selection = 0;
                }
            }
        });
    }

    /*
*   Question 10 -  Radio Buttons
*/
    private void setUpQuestion10RadioButtons(){

        question10Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.answer110:
                        question10Selection = LectureSurveyTable.PART_1;
                        break;
                    case R.id.answer210:
                        question10Selection = LectureSurveyTable.PART_2;
                        break;
                    case R.id.answer310:
                        question10Selection = LectureSurveyTable.PART_3;
                        break;
                    default:
                        question10Selection = 0;
                }
            }
        });
    }



}
