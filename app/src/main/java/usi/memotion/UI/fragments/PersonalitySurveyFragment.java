package usi.memotion.UI.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
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
import usi.memotion.local.database.db.LocalSQLiteDBHelper;
import usi.memotion.local.database.tables.PersonalitySurveyTable;
import usi.memotion.local.database.tables.UserTable;
import usi.memotion.remote.database.controllers.SwitchDriveController;
import usi.memotion.remote.database.upload.Uploader;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalitySurveyFragment extends Fragment {

    private Spinner question1Options, question2Options, question3Options, question4Options, question5Options, question6Options,
            question7Options, question8Options, question9Options, question10Options;

    LocalSQLiteDBHelper dbHelper;
    private LocalStorageController localController;

    SwitchDriveController switchDriveController;
    String androidID;


    private Button submitButton;

    private ImageButton information;

    private LocalStorageController localcontroller;

    public PersonalitySurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_personality_survey, container, false);

        dbHelper = new LocalSQLiteDBHelper(getContext());
        localController = SQLiteController.getInstance(getContext());

        question1Options = (Spinner) root.findViewById(R.id.tipi_q1_options);
        question2Options = (Spinner) root.findViewById(R.id.tipi_q2_options);
        question3Options = (Spinner) root.findViewById(R.id.tipi_q3_options);
        question4Options = (Spinner) root.findViewById(R.id.tipi_q4_options);
        question5Options = (Spinner) root.findViewById(R.id.tipi_q5_options);
        question6Options = (Spinner) root.findViewById(R.id.tipi_q6_options);
        question7Options = (Spinner) root.findViewById(R.id.tipi_q7_options);
        question8Options = (Spinner) root.findViewById(R.id.tipi_q8_options);
        question9Options = (Spinner) root.findViewById(R.id.tipi_q9_options);
        question10Options = (Spinner) root.findViewById(R.id.tipi_q10_options);

        localcontroller = SQLiteController.getInstance(getContext());

        submitButton = (Button) root.findViewById(R.id.submit_tipi);
        information = (ImageButton) root.findViewById(R.id.info_TIPI);

        Spinner[] spinners = {question1Options, question2Options, question3Options, question4Options,
                question5Options, question6Options, question7Options, question8Options, question9Options, question10Options};

        for (Spinner s : spinners) {
            setupSpinnerOptions(s);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateSpinners()) {
                    ContentValues record = new ContentValues();
                    record.put(PersonalitySurveyTable.TIMESTAMP, System.currentTimeMillis());
                    record.put(PersonalitySurveyTable.QUESTION_1, Integer.parseInt(question1Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_2, Integer.parseInt(question2Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_3, Integer.parseInt(question3Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_4, Integer.parseInt(question4Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_5, Integer.parseInt(question5Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_6, Integer.parseInt(question6Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_7, Integer.parseInt(question7Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_8, Integer.parseInt(question8Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_9, Integer.parseInt(question9Options.getSelectedItem().toString()));
                    record.put(PersonalitySurveyTable.QUESTION_10, Integer.parseInt(question10Options.getSelectedItem().toString()));
                    localcontroller.insertRecord(PersonalitySurveyTable.TABLE_PERSONALITY_SURVEY, record);

                    Log.d("PERSONALITY SURVEYS", "Added record: ts: " + record.get(PersonalitySurveyTable.TIMESTAMP));


                    //Show thank you message
                    Toast.makeText(getContext(), "Thank you very much!", Toast.LENGTH_SHORT).show();

                    Fragment newFragment = new SWLSSurveyFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, newFragment);
                    ft.commit();

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

    private boolean validateSpinners() {
        boolean valid1 = isSpinnerValid(question1Options);
        boolean valid2 = isSpinnerValid(question2Options);
        boolean valid3 = isSpinnerValid(question3Options);
        boolean valid4 = isSpinnerValid(question4Options);
        boolean valid5 = isSpinnerValid(question5Options);
        boolean valid6 = isSpinnerValid(question6Options);
        boolean valid7 = isSpinnerValid(question7Options);
        boolean valid8 = isSpinnerValid(question8Options);
        boolean valid9 = isSpinnerValid(question9Options);
        boolean valid10 = isSpinnerValid(question10Options);
        return valid1 && valid2 && valid3 && valid4 && valid5 && valid6 && valid7
                &&valid8 && valid9 && valid10;
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


