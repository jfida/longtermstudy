package usi.memotion.Reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import java.util.Random;

import usi.memotion.MainActivity;
import usi.memotion.R;
import usi.memotion.UI.fragments.LectureSurveysFragment;

/**
 *
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("I am in receiver!");

        String session = intent.getExtras().get("Session").toString();

        if(session.equals("Wednesday - Pre")){
            setLectureNotification(context, "Lecture Survey", "Please tell us how was your lecture - " + session + "!", session, 100090);
        }else if(session.equals("Wednesday - Break")){
            setLectureNotification(context, "Lecture Survey", "Please tell us how was your lecture - " + session + "!", session, 100091);
        }else if(session.equals("Wednesday - Post")){
            setLectureNotification(context, "Lecture Survey", "Please tell us how was your lecture - " + session + "!", session, 100092);
        }else if(session.equals("Friday - Pre")){
            setLectureNotification(context, "Lecture Survey", "Please tell us how was your lecture - " + session + "!", session, 100093);
        }else if(session.equals("Friday - Break")){
            setLectureNotification(context, "Lecture Survey", "Please tell us how was your lecture - " + session + "!", session, 100094);
        }else if(session.equals("Friday - Post")){
            setLectureNotification(context, "Lecture Survey", "Please tell us how was your lecture - " + session + "!", session, 100095);
        }else if(session.equals("E4")) {
            setE4Notification(context, "E4 charging time", "Please don't forget to charge E4 and upload the data", 100096);
        }else if(session.equals("early morning")){
            setDailyNotification(context, "Survey about " + session, "Please tell us how you feel during the " + session + "!", 100097);
        }else if(session.equals("morning")){
            setDailyNotification(context, "Survey about " + session, "Please tell us how you feel during the " + session + "!", 100098);
        }else if(session.equals("afternoon")){
            setDailyNotification(context, "Survey about " + session, "Please tell us how you feel during the " + session + "!", 100099);
        }
    }



    public void setLectureNotification(Context context, String title, String content, String session, final int notificationID){
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: "+code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);
//        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);


//        long[] pattern = {500,500,500,500,500,500};
//        builder.setVibrate(pattern);


        /*
        When notification clicked, open Lecture Surveys Fragment
         */
        Intent intent = new Intent(context, Questionnaire.class);
        intent.putExtra("LectureSession", session);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 30 * 60 * 1000; // 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }


    public void setE4Notification(Context context, String title, String content, final int notificationID){
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: "+code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);
//        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);


//        long[] pattern = {500,500,500,500,500,500};
//        builder.setVibrate(pattern);


        Intent intent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 5 * 60 * 60 * 1000; // 2 * 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }


    public void setDailyNotification(Context context, String title, String content, final int notificationID){
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: "+code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);
//        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);


//        long[] pattern = {500,500,500,500,500,500};
//        builder.setVibrate(pattern);


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("fragmentChoice", "dailySurveys");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 2 * 60 * 60 * 1000; // 3 * 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }
}