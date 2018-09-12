package usi.memotion.local.database.tables;

import android.provider.BaseColumns;

/**
 * Created by shkurtagashi on 12/09/18.
 */
public class UserTable {


//    public static String[] getColumns() {
//        String[] columns = {KEY_USER_ID, KEY_USER_UID, KEY_USER_AGREED, KEY_USER_AGE, KEY_USER_GENDER, KEY_USER_EMAIL, KEY_USER_FACULTY, KEY_USER_ACADEMIC_STATUS, KEY_USER_CREATION_TS, KEY_USER_UPDATE_TS, KEY_USER_END_STUDY};
//        return columns;
//    }


    // Private constructor to prevent someone from accidentally instantiating the contract class,
    private UserTable() {}

    /* Inner class that defines the table contents */
    public static final class UserEntry implements BaseColumns {

        /*
        * Users - Table and Columns declaration
         */
        public final static String TABLE_USER = "usersTable";

        public final static String _ID = BaseColumns._ID;
        public final static String ANDROID_ID = "android_id";
        public final static String USERNAME = "username";
        public final static String EMPATICAID = "empatica_id";
        public final static String EMAIL = "email";
        public final static String SWITCH_TOKEN = "switch_token";
        public final static String SWITCH_PASSWORD = "switch_password";
        public final static String COLUMN_GENDER = "gender";
        public final static String COLUMN_AGE = "age";
        public final static String COLUMN_STATUS = "status";

        public static String getCreateQuery(){
            return "CREATE TABLE " + UserEntry.TABLE_USER + " ("
                    + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UserEntry.ANDROID_ID + " TEXT, "
                    + UserEntry.USERNAME + " TEXT, "
                    + UserEntry.EMPATICAID + " TEXT, "
                    + UserEntry.SWITCH_TOKEN + " TEXT, "
                    + UserEntry.SWITCH_PASSWORD + " TEXT, "
                    + UserEntry.EMAIL + " TEXT, "
                    + UserEntry.COLUMN_GENDER + " TEXT, "
                    + UserEntry.COLUMN_AGE + " TEXT, "
                    + UserEntry.COLUMN_STATUS + " TEXT);";
        }


        /**
         * Possible values for the age of the user
         */
        public static final String AGE_20_30 = "20-30";
        public static final String AGE_30_40 = "30-40";
        public static final String AGE_40_50 = "40-50";
        public static final String AGE_50_ABOVE = "50 and above";


        /**
         * Possible values for the gender of the user
         */
        public static final String GENDER_MALE = "M";
        public static final String GENDER_FEMALE = "F";


        public static final String ACADEMIA = "Academia";
        public static final String INDUSTRY = "Industry";

        public static final String STATUS_FULL_PROFESSOR = "Professor";
        public static final String STATUS_POST_DOC = "Post-doc";
        public static final String STATUS_PHD_STUDENT = "Ph.D. Student";
        public static final String RESEARCHER = "Researcher";
        public static final String STATUS_ASSISTANT = "Assistant";
        public static final String STUDENT = "Student";

        public static final String OTHER = "Other";


        public static String[] getColumns(){
            String[] columns = {_ID, ANDROID_ID, USERNAME, EMPATICAID, EMAIL, COLUMN_GENDER, COLUMN_AGE, COLUMN_STATUS};

            return columns;
        }



    }
}
