package usi.memotion.local.database.tables;

import android.provider.BaseColumns;

public class FatigueSurveyTable {
    public final static String TABLE_FATIGUE_SURVEY = "FATIGUE";

    public final static String _ID = BaseColumns._ID;
    public final static String TIMESTAMP = "timestamp";
    public final static String QUESTION_1 = "question1";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_FATIGUE_SURVEY + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIMESTAMP + " REAL, "
                + QUESTION_1 + " TEXT)";
    }


    public static String[] getColumns() {
        String[] columns = {_ID, TIMESTAMP, QUESTION_1};
        return columns;
    }
}