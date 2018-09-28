package usi.memotion.local.database.tables;

import android.provider.BaseColumns;

/**
 * Created by shkurtagashi on 9/11/18.
 */

public class AnxietySurveyTable {
    public final static String TABLE_ANXIETY_SURVEY = "anxietySurvey";

    public final static String _ID = BaseColumns._ID;
    public final static String TIMESTAMP = "timestamp";
    public final static String QUESTION_1 = "question1";
    public final static String QUESTION_2 = "question2";
    public final static String QUESTION_3 = "question3";
    public final static String QUESTION_4 = "question4";
    public final static String QUESTION_5 = "question5";
    public final static String QUESTION_6 = "question6";
    public final static String QUESTION_7 = "question7";



    /**
     * Possible values for the answers of the students
     */
    public static final int ANSWER_1 = 1;
    public static final int ANSWER_2 = 2;
    public static final int ANSWER_3 = 3;
    public static final int ANSWER_4 = 4;
    public static final int ANSWER_5 = 5;
    public static final int ANSWER_6 = 6;
    public static final int ANSWER_7 = 7;


    public static final int PART_1 = 1;
    public static final int PART_2 = 2;
    public static final int PART_3 = 3;

    // String that contains the SQL statement to create the General Survey data table
    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_ANXIETY_SURVEY + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIMESTAMP + " REAL, "
                + QUESTION_1 + " TEXT, "
                + QUESTION_2 + " TEXT, "
                + QUESTION_3 + " TEXT, "
                + QUESTION_4 + " TEXT, "
                + QUESTION_5 + " TEXT, "
                + QUESTION_6 + " TEXT, "
                + QUESTION_7 + " TEXT " + ")";

    }


    public static String[] getColumns(){
        String[] columns = {_ID, TIMESTAMP, QUESTION_1, QUESTION_2, QUESTION_3, QUESTION_4, QUESTION_5, QUESTION_6, QUESTION_7};

        return columns;
    }

}
