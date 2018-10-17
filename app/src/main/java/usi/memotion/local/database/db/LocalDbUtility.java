package usi.memotion.local.database.db;

import usi.memotion.local.database.tables.AccelerometerTable;
import usi.memotion.local.database.tables.ActivityRecognitionTable;
import usi.memotion.local.database.tables.ApplicationLogsTable;
import usi.memotion.local.database.tables.LectureSurveyTable;
import usi.memotion.local.database.tables.LocationTable;
import usi.memotion.local.database.tables.NotificationsTable;
import usi.memotion.local.database.tables.PAMTable;
import usi.memotion.local.database.tables.PhoneCallLogTable;
import usi.memotion.local.database.tables.PhoneLockTable;
import usi.memotion.local.database.tables.SMSTable;
import usi.memotion.local.database.tables.SimpleMoodTable;
import usi.memotion.local.database.tables.UserTable;
import usi.memotion.local.database.tables.WiFiTable;

/**
 * Created by usi on 18/01/17.
 */

public class LocalDbUtility {
    private final static int DATA_TABLES_COUNT = 17;


    public static String getTableName(LocalTables table) {
        switch (table) {
//            case TABLE_ACCELEROMETER:
//                return AccelerometerTable.TABLE_ACCELEROMETER;
            case TABLE_CALL_LOG:
                return PhoneCallLogTable.TABLE_CALL_LOG;
            case TABLE_LOCATION:
                return LocationTable.TABLE_LOCATION;
            case TABLE_PHONELOCK:
                return PhoneLockTable.TABLE_PHONELOCK;
            case TABLE_SMS:
                return SMSTable.TABLE_SMS;
//            case TABLE_WIFI:
//                return WiFiTable.TABLE_WIFI;
            case TABLE_PAM:
                return PAMTable.TABLE_PAM;
            case TABLE_SIMPLE_MOOD:
                return SimpleMoodTable.TABLE_SIMPLE_MOOD;
            case TABLE_NOTIFICATIONS:
                return NotificationsTable.TABLE_NOTIFICATIONS;
            case TABLE_USER:
                return UserTable.TABLE_USER;
            case TABLE_LECTURE_SURVEY:
                return LectureSurveyTable.TABLE_LECTURE_SURVEY;
//            case TABLE_ONE_TIME_SURVEY:
//                return
//            case TABLE_DAILY_SURVEY:
//                return
            case TABLE_APPLICATION_LOGS:
                return ApplicationLogsTable.TABLE_APPLICATION_LOGS;
            case TABLE_ACTIVITY_RECOGNITION:
                return ActivityRecognitionTable.TABLE_ACTIVITY_RECOGNITION;
            default:
                return null;
        }
    }

    public static String[] getTableColumns(LocalTables table) {
        switch (table) {
            case TABLE_ACCELEROMETER:
                return AccelerometerTable.getColumns();
            case TABLE_CALL_LOG:
                return PhoneCallLogTable.getColumns();
            case TABLE_LOCATION:
                return LocationTable.getColumns();
            case TABLE_PHONELOCK:
                return PhoneLockTable.getColumns();
            case TABLE_SMS:
                return SMSTable.getColumns();
            case TABLE_WIFI:
                return WiFiTable.getColumns();
            case TABLE_PAM:
                return PAMTable.getColumns();
            case TABLE_SIMPLE_MOOD:
                return SimpleMoodTable.getColumns();
            case TABLE_NOTIFICATIONS:
                return NotificationsTable.getColumns();
            case TABLE_APPLICATION_LOGS:
                return ApplicationLogsTable.getColumns();
            case TABLE_ACTIVITY_RECOGNITION:
                return ActivityRecognitionTable.getColumns();
            case TABLE_LECTURE_SURVEY:
                return LectureSurveyTable.getColumns();
            case TABLE_USER:
                return UserTable.getColumns();
            default:
                return null;
        }
    }
}
