package usi.memotion.UI.views;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import usi.memotion.MainActivity;
import usi.memotion.R;
import usi.memotion.UI.fragments.PersonalitySurveyFragment;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.db.LocalSQLiteDBHelper;
import usi.memotion.local.database.tableHandlers.User;
import usi.memotion.local.database.tables.LectureSurveyTable;
import usi.memotion.local.database.tables.UserTable;
import usi.memotion.remote.database.controllers.SwitchDriveController;
import usi.memotion.remote.database.upload.Uploader;

/**
 * Created by shkurtagashi on 12/09/17.
 */

public class RegistrationView extends Fragment {
    private static final String TAG = "RegisterFragment";

    LocalSQLiteDBHelper dbHelper;
    private LocalStorageController localController;

    SwitchDriveController switchDriveController;
    SQLiteController sqlLiteController;
    String androidID;

    private EditText usernameEditText;
    private EditText emailAdress;
    private EditText empaticaIDEditText;
    private EditText switchTokenEditText;
    private EditText switchPasswordEditText;
    private Spinner ageOptions;
    private RadioGroup genderOptions;
    private Spinner statusOptions;

    private Button submitRegistrationFormButton;
    private Button cancelRegistrationFormButton;

    public String username;
    public String empaticaID;
    public String email;
    public String switchToken;
    public String switchPassword;


    private String genderSelection = "";
    private String ageSelection = "";
    private String statusSelection = "";


    public RegistrationView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_register, container, false);

        dbHelper = new LocalSQLiteDBHelper(getContext());
        localController = SQLiteController.getInstance(getContext());


        usernameEditText = (EditText) rootview.findViewById(R.id.username_value);
        empaticaIDEditText = (EditText) rootview.findViewById(R.id.empatica_value);
        emailAdress = (EditText) rootview.findViewById(R.id.email);
        switchTokenEditText = (EditText) rootview.findViewById(R.id.switch_token_value);
        switchPasswordEditText = (EditText) rootview.findViewById(R.id.switch_pass_value);
        ageOptions = (Spinner) rootview.findViewById(R.id.age_range);
        genderOptions = (RadioGroup) rootview.findViewById(R.id.genderRadioButtons);
        statusOptions = (Spinner) rootview.findViewById(R.id.status_choices);

        setUpGenderRadioButtons();
        setUpAgeSpinner();
        setUpStatusSpinner();

        submitRegistrationFormButton = (Button)rootview.findViewById(R.id.submit_button);
        submitRegistrationFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        cancelRegistrationFormButton = (Button)rootview.findViewById(R.id.cancel_button);
        cancelRegistrationFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootview;
    }

    private void registerUser() {
        username = usernameEditText.getText().toString();
        email = emailAdress.getText().toString();
        empaticaID = empaticaIDEditText.getText().toString();
        switchToken = switchTokenEditText.getText().toString();
        switchPassword = switchPasswordEditText.getText().toString();


        if(genderSelection.equals("")){
            Toast.makeText(getContext(), "Please select your Gender!", Toast.LENGTH_LONG).show();
            return;
        }

        if(ageSelection.equals("")){
            Toast.makeText(getContext(), "Please select your Age!", Toast.LENGTH_LONG).show();
            return;
        }


        if(statusSelection.equals("")){
            Toast.makeText(getContext(), "Please select your Status!", Toast.LENGTH_LONG).show();
            return;
        }

        long time = System.currentTimeMillis()/1000;
        ContentValues record = new ContentValues();

        record.put(UserTable.ANDROID_ID, Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        record.put(UserTable.USERNAME, username);
        record.put(UserTable.EMPATICAID, empaticaID);
        record.put(UserTable.SWITCH_TOKEN, switchToken);
        record.put(UserTable.SWITCH_PASSWORD, switchPassword);
        record.put(UserTable.EMAIL, email);
        record.put(UserTable.COLUMN_AGE, ageSelection);
        record.put(UserTable.COLUMN_GENDER, genderSelection);
        record.put(UserTable.COLUMN_STATUS, statusSelection);

        localController.insertRecord(UserTable.TABLE_USER, record);
        Log.d("REGISTRATION FORM", "Added record: ts: " + "username: " + record.get(UserTable.USERNAME) + ", e4 id: " + record.get(UserTable.EMPATICAID)  +
                ", Email: " + record.get(UserTable.EMAIL) + ", Switch Token: " + record.get(UserTable.SWITCH_TOKEN) + ", Switch pass: " + record.get(UserTable.SWITCH_PASSWORD)
                + ", Age: " + record.get(UserTable.COLUMN_AGE) + ", Gender: " + record.get(UserTable.COLUMN_GENDER) + ", Status: " + record.get(UserTable.COLUMN_STATUS));

        uploadRemotely();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("New User Account");
        alertDialog.setMessage("You have successfully created account with: \n \n" + "Username: " + username + "\nEmpaticaID: " + empaticaID +  "\nSwitchToken: " + switchToken
                + "\nSwitchPass: " + switchPassword +"\nAge: " + ageSelection + "\nGender: " + genderSelection + "\nStatus: " + statusSelection + "\n \n \n Do you want to proceed?");

        alertDialog.setIcon(R.drawable.account);

        alertDialog.setNegativeButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Thank you!", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent (getContext(), PersonalitySurveyFragment.class);
//                        startActivity(i);
//                        getActivity().finish();

                        Fragment newFragment = new PersonalitySurveyFragment();
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

        alertDialog.show();
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



    /*
   *   Gender Radio Buttons
    */
    private void setUpGenderRadioButtons(){

        genderOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radiobutton_female:
                        genderSelection = UserTable.GENDER_FEMALE;
                        break;
                    case R.id.radiobutton_male:
                        genderSelection = UserTable.GENDER_MALE;
                        break;
                    default:
                        genderSelection = "";
                }
            }
        });
    }

    /*
    * Age range spinner
    */
    private void setUpAgeSpinner(){

        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.age_range_choices, android.R.layout.simple_spinner_item);

        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageOptions.setAdapter(ageAdapter);


        // Set the integer ageSelection to the constant values
        ageOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if(selection.equals("Select Item")){
                        ageSelection = "";
                    } else if (selection.equals(getString(R.string.range20to30))) {
                        ageSelection = UserTable.AGE_20_30;
                    } else if(selection.equals(getString(R.string.range30to40))){
                        ageSelection = UserTable.AGE_30_40;
                    } else if (selection.equals(getString(R.string.range40to50))) {
                        ageSelection = UserTable.AGE_40_50;
                    } else {
                        ageSelection = UserTable.AGE_50_ABOVE;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ageSelection = ""; // Unknown
            }
        });
    }



    /*
    * Status spinner
    */
    private void setUpStatusSpinner(){

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_choices, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusOptions.setAdapter(statusAdapter);

        // Set the integer statusSelection to the constant values
        statusOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if(selection.equals("Select Item")) {
                        statusSelection = "";
                    } else if (selection.equals(getString(R.string.professor))) {
                        statusSelection = UserTable.STATUS_FULL_PROFESSOR;
                    } else if(selection.equals(getString(R.string.researcher))) {
                        statusSelection = UserTable.RESEARCHER;
                    } else if(selection.equals(getString(R.string.post_doc))){
                        statusSelection = UserTable.STATUS_POST_DOC;
                    } else if(selection.equals(getString(R.string.phd_student))){
                        statusSelection = UserTable.STATUS_PHD_STUDENT;
                    } else if(selection.equals(getString(R.string.assistant))){
                        statusSelection = UserTable.STATUS_ASSISTANT;
                    }else if(selection.equals(getString(R.string.student))){
                        statusSelection = UserTable.STUDENT;
                    }else if(selection.equals(getString(R.string.other))){
                        statusSelection = UserTable.OTHER;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) { statusSelection = ""; // Unknown
            }

        });

    }

}
