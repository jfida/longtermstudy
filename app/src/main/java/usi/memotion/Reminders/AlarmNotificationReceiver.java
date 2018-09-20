package usi.memotion.Reminders;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
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

        if(session.equals("Wednesday - Pre") || session.equals("Friday - Pre") || session.equals("Wednesday - Break") || session.equals("Friday - Break") || session.equals("Wednesday - Post") || session.equals("Friday - Post")){
            setNotification2(context, "Lecture Survey", "Please tell us how was your lecture - " + session + "!", session, 1000097);
        }else if(session.equals("E4")) {
            setE4Notification(context, "E4 charging time", "Please don't forget to charge E4 and upload the data", 1000098);
        }else if(session.equals("morning") || session.equals("afternoon") || session.equals("evening")){
            setDailyNotification(context, "Daily Survey", "Please tell us how you feel in the " + session + "!", 1000099);
        }
    }



    public void setNotification2(Context context, String title, String content, String session, int notificationID){
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: "+code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);


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

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }


    public void setE4Notification(Context context, String title, String content, int notificationID){
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: "+code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(false);
        builder.setOngoing(true);


        Intent intent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }


    public void setDailyNotification(Context context, String title, String content, int notificationID){
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: "+code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);


        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("fragmentChoice", "dailySurveys");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());
    }
}