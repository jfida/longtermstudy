package usi.memotion.local.database.db;

import usi.memotion.local.database.tableHandlers.LectureSurvey;
import usi.memotion.local.database.tableHandlers.PAMSurvey;
import usi.memotion.local.database.tableHandlers.TableHandler;
import usi.memotion.local.database.tableHandlers.User;
import usi.memotion.local.database.tables.AccelerometerTable;
import usi.memotion.local.database.tables.BlueToothTable;
import usi.memotion.local.database.tables.LectureSurveyTable;
import usi.memotion.local.database.tables.LocationTable;
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

public enum LocalTables {
    TABLE_BLUETOOTH(BlueToothTable.class),
    TABLE_LOCATION(LocationTable.class),
    TABLE_CALL_LOG(PhoneCallLogTable.class),
    TABLE_PHONELOCK(PhoneLockTable.class),
    TABLE_SMS(SMSTable.class),
    TABLE_WIFI(WiFiTable.class),
    TABLE_PAM(PAMTable.class),
    TABLE_SIMPLE_MOOD(SimpleMoodTable.class),
    TABLE_USER(UserTable.class),
    TABLE_ACCELEROMETER(AccelerometerTable.class),
    TABLE_LECTURE_SURVEY(LectureSurveyTable.class);

    LocalTables(Class a) {
    }

    public static TableHandler getTableHandler(LocalTables table) {
        switch(table) {
            case TABLE_PAM:
                return new PAMSurvey(true);
            case TABLE_USER:
                return new User(true);
            case TABLE_LECTURE_SURVEY:
                return new LectureSurvey(true);
            default:
                throw new IllegalArgumentException("Table not found!");
        }
    }

    public String getTableName() {
        switch(this) {
            case TABLE_PAM:
                return PAMTable.TABLE_PAM;
            case TABLE_USER:
                return UserTable.UserEntry.TABLE_USER;
            case TABLE_LECTURE_SURVEY:
                return LectureSurveyTable.TABLE_LECTURE_SURVEY;
            default:
                throw new IllegalArgumentException("Table not found!");
        }
    }

}
