package usi.memotion.UI.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import usi.memotion.R;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.tables.PersonalitySurveyTable;
import usi.memotion.local.database.tables.SWLSSurveyTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SWLSSurveyFragment extends Fragment {

    private Spinner question1Options, question2Options, question3Options, question4Options, question5Options;

    private String answer1, answer2, answer3, answer4, answer5;

    private Button submitButton;

    private ImageButton information;

    private LocalStorageController localcontroller;

    public SWLSSurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_swls_survey, container, false);

        question1Options = (Spinner) root.findViewById(R.id.swls_q1_options);
        question2Options = (Spinner) root.findViewById(R.id.swls_q2_options);
        question3Options = (Spinner) root.findViewById(R.id.swls_q3_options);
        question4Options = (Spinner) root.findViewById(R.id.swls_q4_options);
        question5Options = (Spinner) root.findViewById(R.id.swls_q5_options);

        localcontroller = SQLiteController.getInstance(getContext());

        submitButton = (Button) root.findViewById(R.id.submit_swls);

        information = (ImageButton) root.findViewById(R.id.info_SWLS);

        Spinner[] spinners = {question1Options, question2Options, question3Options, question4Options, question5Options};

        for (Spinner s : spinners) {
            setupSpinnerOptions(s);
        }

        setUpSpinnerListeners();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateSpinners()) {
                    ContentValues record = new ContentValues();
                    record.put(SWLSSurveyTable.TIMESTAMP, System.currentTimeMillis());
                    record.put(SWLSSurveyTable.QUESTION_1, Integer.parseInt(question1Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_2, Integer.parseInt(question2Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_3, Integer.parseInt(question3Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_4, Integer.parseInt(question4Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_5, Integer.parseInt(question5Options.getSelectedItem().toString()));
                    localcontroller.insertRecord(SWLSSurveyTable.TABLE_SWLSS_SURVEY, record);

                    Log.d("SWLSS SURVEYS", "Added record: ts: " + record.get(PersonalitySurveyTable.TIMESTAMP));


                    //Show thank you message
                    Toast.makeText(getContext(), "Thank you very much!", Toast.LENGTH_SHORT).show();

                    /*Fragment newFragment = new SWLSSurveyFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, newFragment);
                    ft.commit();*/

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

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.TIPI_scale_title);
                builder.setMessage(R.string.TIPI_scale);
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

    /*
     * Questions spinner values
     */
    private void setupSpinnerOptions(Spinner s) {
        ArrayAdapter<CharSequence> tipiAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tipi_questions_choices, android.R.layout.simple_spinner_item);

        tipiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(tipiAdapter);
    }

    private void setUpSpinnerListeners() {

        // Set the integer ageSelection to the constant values
        question1Options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                answer1 = selection;
                Log.d("SWLSFragment", "Question 1 answer: " + answer1);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                answer1 = ""; // Unknown
            }
        });

        question2Options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                answer2 = selection;
                Log.d("SWLSFragment", "Question 2 answer: " + answer2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                answer2 = "";
            }
        });

        question3Options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                answer3 = selection;
                Log.d("SWLSFragment", "Question 3 answer: " + answer3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                answer3 = "";
            }
        });

        question4Options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                answer4 = selection;
                Log.d("SWLSFragment", "Question 4 answer: " + answer4);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                answer4 = ""; // Unknown
            }
        });

        question5Options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                answer5 = selection;
                Log.d("SWLSFragment", "Question 5 answer: " + answer5);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                answer5 = ""; // Unknown
            }
        });
    }

        private boolean validateSpinners () {
            boolean valid = true;
            valid = isSpinnerValid(question1Options);
            valid = isSpinnerValid(question2Options);
            valid = isSpinnerValid(question3Options);
            valid = isSpinnerValid(question4Options);
            valid = isSpinnerValid(question5Options);
            return valid;
        }

        private boolean isSpinnerValid(Spinner spinner){
            int selected = spinner.getSelectedItemPosition();
            TextView text = (TextView)spinner.getSelectedView();
            if(((String)spinner.getItemAtPosition(selected)).equals("Select")){
                text.setError("error");
                return false;
            } else {
                text.setError(null);
                return true;
            }
        }

}


