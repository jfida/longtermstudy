package usi.memotion.Reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
//import android.icu.util.Calendar;

import java.util.Calendar;
import java.util.Random;


/**
 * Created by shkurtagashi on 11.02.17.
 */

public class FinalScheduler {

    /********** Mobile Computing - Day 1 ***********/
    private Weekday preLecture1 = new Weekday(15, 20, Calendar.WEDNESDAY, "Wednesday - Pre");
    private Weekday breakLecture1 = new Weekday(16, 15, Calendar.WEDNESDAY, "Wednesday - Break");
    private Weekday postLecture1 = new Weekday(17, 15, Calendar.WEDNESDAY, "Wednesday - Post");

    /********** Mobile Computing - Day 2 ***********/
    private Weekday preLecture2 = new Weekday(13, 20, Calendar.FRIDAY, "Friday - Pre");
    private Weekday breakLecture2 = new Weekday(14, 15, Calendar.FRIDAY, "Friday - Break");
    private Weekday postLecture2 = new Weekday(15, 15, Calendar.FRIDAY, "Friday - Post");

    /********** Daily Reminders *********/
    private Reminder morningSurvey = new Reminder(7, 15, "early morning"); //7:15
    private Reminder afternoonSurvey = new Reminder(12, 30, "morning");
    private Reminder eveningSurvey = new Reminder(19, 0, "afternoon");
    private Reminder e4 = new Reminder(21, 15, "E4"); //21:15

    private Calendar createCalendar(int day, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    private Calendar createCalendar2(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

      /* Creation of the reminder for lecture and daily surveys */
    public void createReminder(Context context) {

        Weekday [] lectureReminders = {preLecture1, breakLecture1, postLecture1, preLecture2, breakLecture2, postLecture2};

        Reminder [] dailyReminders = {morningSurvey, afternoonSurvey, eveningSurvey, e4};

        Random rand = new Random();
        int code;

        //Lecture reminders
        for (Weekday w: lectureReminders) {
            code = rand.nextInt(100000000);
            setLectureAlarm(context, code, w.getDescription(), w);
            Log.v("Scheduler", "Session: "+ w.getDescription() + "code: "+code);
        }

        //E4 and Daily surveys
        for (Reminder w: dailyReminders) {
            code = rand.nextInt(100000000);
            setDailyAlarms(context, code, w.getDescription(), w);
            Log.v("Scheduler", "Session: "+ w.getDescription() + "code: "+code);
        }


    }

    /*
        Setting the alarm for lecture related surveys, repeated every week
     */
    public void setLectureAlarm(Context context, int requestCode, String session, Weekday weekday){
        Intent myIntent = new Intent(context, AlarmNotificationReceiver.class);
        myIntent.putExtra("Session", session);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, requestCode, myIntent, PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar1 = createCalendar(weekday.getDay2(),weekday.getHour(), weekday.getMinute());

        //If the time already past, set it for next week
        if (calendar1.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        } else {
            calendar1.add(Calendar.DAY_OF_MONTH, 7);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        }

    }

    /*
        Setting the alarm for daily surveys and reminders, repeated every day
     */
    public void setDailyAlarms(Context context, int requestCode, String session, Reminder weekday){
        Intent myIntent = new Intent(context, AlarmNotificationReceiver.class);
        myIntent.putExtra("Session", session);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, requestCode, myIntent, PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar1 = createCalendar2(weekday.getHour(), weekday.getMinute());

        //If the time already past, set it for next day
        if (calendar1.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        } else {
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        }

    }
}

