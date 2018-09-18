package usi.memotion.local.database.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import usi.memotion.PostLectureSurvey;
import usi.memotion.local.database.tables.AccelerometerTable;
import usi.memotion.local.database.tables.BlueToothTable;
import usi.memotion.local.database.tables.LectureSurveyTable;
import usi.memotion.local.database.tables.PAMTable;
import usi.memotion.local.database.tables.PhoneCallLogTable;
import usi.memotion.local.database.tables.LocationTable;
import usi.memotion.local.database.tables.PhoneLockTable;
import usi.memotion.local.database.tables.SMSTable;
import usi.memotion.local.database.tables.SimpleMoodTable;
import usi.memotion.local.database.tables.UploaderUtilityTable;
import usi.memotion.local.database.tables.UserTable;
import usi.memotion.local.database.tables.WiFiTable;

/**
 * Created by shkurtagashi on 29/12/16.
 */

public class LocalSQLiteDBHelper extends SQLiteOpenHelper {
    //db information
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Memotion";

    private SQLiteDatabase db;


    public LocalSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create actual data table
        db.execSQL(LocationTable.getCreateQuery());
        db.execSQL(AccelerometerTable.getCreateQuery());
        db.execSQL(PhoneCallLogTable.getCreateQuery());
        db.execSQL(SMSTable.getCreateQuery());
        db.execSQL(BlueToothTable.getCreateQuery());
        db.execSQL(PhoneLockTable.getCreateQuery());
        db.execSQL(WiFiTable.getCreateQuery());
        db.execSQL(UploaderUtilityTable.getCreateQuery());
        db.execSQL(UserTable.UserEntry.getCreateQuery());
        db.execSQL(SimpleMoodTable.getCreateQuery());
        db.execSQL(PAMTable.getCreateQuery());
        db.execSQL(LectureSurveyTable.getCreateQuery());


        //insert init data to uploader_utility table
        insertRecords(db, UploaderUtilityTable.TABLE_UPLOADER_UTILITY, UploaderUtilityTable.getRecords());

        Log.d("DATABASE HELPER", "Db created");
    }

    /**
     * Utility function to insert the given records in the given table.
     *
     * @param db
     * @param tableName
     * @param records
     */
    private void insertRecords(SQLiteDatabase db, String tableName, List<ContentValues> records) {
        for(ContentValues record: records) {
            Log.d("DBHelper", "Inserting into table " + tableName + " record " + record.toString());
            db.insert(tableName, null, record);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "Upgrading db. Truncating accelerometer and used apps");
        db.execSQL("delete from "+ AccelerometerTable.TABLE_ACCELEROMETER);
        return;
    }

    public List<PostLectureSurvey> getAllPostlectureSurveys(){
        List<PostLectureSurvey> postLectureSurveyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ AccelerometerTable.TABLE_ACCELEROMETER;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        try{
            if(cursor.moveToPosition(0)){
                do{
                    PostLectureSurvey postlectureSurvey = new PostLectureSurvey();

                    postlectureSurvey._id = cursor.getInt(cursor.getColumnIndex("id_used_app"));
                    postlectureSurvey._timestamp= cursor.getDouble(cursor.getColumnIndex("ts"));
                    postlectureSurvey._question1= cursor.getString(cursor.getColumnIndex("type"));
                    postlectureSurvey._question2 = cursor.getString(cursor.getColumnIndex("name"));
//                    postlectureSurvey._question3 = cursor.getString(cursor.getColumnIndex("duration"));



                    postLectureSurveyList.add(postlectureSurvey);
                }while(cursor.moveToNext());

            }
        }catch (Exception e){
            Log.d("DB HELPER", "Error while trying to get posts from accelereometer table");
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return postLectureSurveyList;
    }

    /*
    Add a SURVEY into Surveys table
*/
//    public void addSurvey(Survey survey, Context context) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//
//        try {
//            ContentValues values = new ContentValues();
//
//            values.put(SurveyEntry.TIMESTAMP, survey.getTimestamp());
//            values.put(SurveyEntry.PAPER_ID, survey.getPaperId());
//            values.put(SurveyEntry.QUESTION_1, survey.getQuestion1());
//            values.put(SurveyEntry.QUESTION_2, survey.getQuestion2());
//            values.put(SurveyEntry.QUESTION_3, survey.getQuestion3());
//            values.put(SurveyEntry.QUESTION_4, survey.getQuestion4());
//            values.put(SurveyEntry.QUESTION_5, survey.getQuestion5());
//            values.put(SurveyEntry.QUESTION_6, survey.getQuestion6());
//            values.put(SurveyEntry.QUESTION_7, survey.getQuestion7());
//            values.put(SurveyEntry.QUESTION_8, survey.getQuestion8());
//            values.put(SurveyEntry.QUESTION_9, survey.getQuestion9());
//            values.put(SurveyEntry.QUESTION_10, survey.getQuestion10());
//
//            db.insertOrThrow(SurveyEntry.TABLE_NAME_SURVEY, null, values);
//            db.setTransactionSuccessful();
//
//            System.out.println("Survey DATA INSERTED: "+ values);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            Log.d("DBHelper", "Error while trying to add SURVEY to database");
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
}
