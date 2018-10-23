package usi.memotion.local.database.db;

import usi.memotion.UI.fragments.PSQISurveyFragment;
import usi.memotion.UI.fragments.PSSSurveyFragment;
import usi.memotion.UI.fragments.PersonalitySurveyFragment;
import usi.memotion.UI.fragments.SWLSSurveyFragment;
import usi.memotion.local.database.tableHandlers.LectureSurvey;
import usi.memotion.local.database.tableHandlers.PAMSurvey;
import usi.memotion.local.database.tableHandlers.TableHandler;
import usi.memotion.local.database.tableHandlers.User;
import usi.memotion.local.database.tables.AccelerometerTable;
import usi.memotion.local.database.tables.ActivityRecognitionTable;
import usi.memotion.local.database.tables.ApplicationLogsTable;
import usi.memotion.local.database.tables.BlueToothTable;
import usi.memotion.local.database.tables.FatigueSurveyTable;
import usi.memotion.local.database.tables.LectureSurveyTable;
import usi.memotion.local.database.tables.LocationTable;
import usi.memotion.local.database.tables.OverallSurveyTable;
import usi.memotion.local.database.tables.PAMTable;
import usi.memotion.local.database.tables.PSQISurveyTable;
import usi.memotion.local.database.tables.PSSSurveyTable;
import usi.memotion.local.database.tables.PersonalitySurveyTable;
import usi.memotion.local.database.tables.PhoneCallLogTable;
import usi.memotion.local.database.tables.PhoneLockTable;
import usi.memotion.local.database.tables.SMSTable;
import usi.memotion.local.database.tables.SWLSSurveyTable;
import usi.memotion.local.database.tables.SimpleMoodTable;
import usi.memotion.local.database.tables.SleepQualityTable;
import usi.memotion.local.database.tables.UserTable;
import usi.memotion.local.database.tables.WiFiTable;
import usi.memotion.local.database.tables.NotificationsTable;


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
    TABLE_LECTURE_SURVEY(LectureSurveyTable.class),
    TABLE_NOTIFICATIONS(NotificationsTable.class),
    TABLE_APPLICATION_LOGS(ApplicationLogsTable.class),
    TABLE_ACTIVITY_RECOGNITION(ActivityRecognitionTable.class),
    TABLE_PERSONALITY(PersonalitySurveyTable.class),
    TABLE_PSQI(PSQISurveyTable.class),
    TABLE_SWLS(SWLSSurveyTable.class),
    TABLE_PSSS(PSSSurveyTable.class),
    TABLE_SLEEP_QUALITY(SleepQualityTable.class),
    TABLE_FATIGUE_SURVEY(FatigueSurveyTable.class),
    TABLE_OVERALL_SURVEY(OverallSurveyTable.class);



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
                return UserTable.TABLE_USER;
            case TABLE_LECTURE_SURVEY:
                return LectureSurveyTable.TABLE_LECTURE_SURVEY;
            case TABLE_APPLICATION_LOGS:
                return ApplicationLogsTable.TABLE_APPLICATION_LOGS;
            case TABLE_NOTIFICATIONS:
                return NotificationsTable.TABLE_NOTIFICATIONS;
            case TABLE_LOCATION:
                return LocationTable.TABLE_LOCATION;
            case TABLE_CALL_LOG:
                return PhoneCallLogTable.TABLE_CALL_LOG;
            case TABLE_SMS:
                return SMSTable.TABLE_SMS;
            case TABLE_PHONELOCK:
                return PhoneLockTable.TABLE_PHONELOCK;
            case TABLE_SIMPLE_MOOD:
                return SimpleMoodTable.TABLE_SIMPLE_MOOD;
            case TABLE_PERSONALITY:
                return PersonalitySurveyTable.TABLE_PERSONALITY_SURVEY;
            case TABLE_PSSS:
                return PSSSurveyTable.TABLE_PSS_SURVEY;
            case TABLE_PSQI:
                return PSQISurveyTable.TABLE_PSQUI_SURVEY;
            case TABLE_SWLS:
                return SWLSSurveyTable.TABLE_SWLSS_SURVEY;
            case TABLE_ACTIVITY_RECOGNITION:
                return ActivityRecognitionTable.TABLE_ACTIVITY_RECOGNITION;
            case TABLE_SLEEP_QUALITY:
                return SleepQualityTable.TABLE_SLEEP_QUALITY_SURVEY;
            case TABLE_FATIGUE_SURVEY:
                return FatigueSurveyTable.TABLE_FATIGUE_SURVEY;
            case TABLE_OVERALL_SURVEY:
                return OverallSurveyTable.TABLE_OVERALL_SURVEY;
            default:
                throw new IllegalArgumentException("Table not found!");
        }
    }

}
