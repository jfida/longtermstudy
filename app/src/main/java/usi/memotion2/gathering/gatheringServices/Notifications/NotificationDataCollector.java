package usi.memotion2.gathering.gatheringServices.Notifications;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.PowerManager;
import android.service.notification.StatusBarNotification;

import usi.memotion2.gathering.gatheringServices.Notifications.Utils.APILevel;
import usi.memotion2.gathering.gatheringServices.Notifications.Utils.Log;
import usi.memotion2.gathering.gatheringServices.Notifications.Utils.SharedPref;
import usi.memotion2.local.database.controllers.LocalStorageController;
import usi.memotion2.local.database.controllers.SQLiteController;
import usi.memotion2.local.database.db.LocalDbUtility;
import usi.memotion2.local.database.db.LocalSQLiteDBHelper;
import usi.memotion2.local.database.tableHandlers.NotificationData;
import usi.memotion2.local.database.tables.NotificationsTable;
import usi.memotion2.local.database.tables.UploaderUtilityTable;

/**
 * Created by abhinavmerotra
 */

public class NotificationDataCollector 
{
	private LocalStorageController localStorageController;
	private final Log log;
	private final Context context;
	private final SharedPref sp;

	public NotificationDataCollector(Context context)
	{
		this.log = new Log();
		this.context = context;
		this.sp = new SharedPref(context);
        localStorageController = SQLiteController.getInstance(context);
    }


	@SuppressLint("NewApi")
	protected void onNotificationPosted(StatusBarNotification sbn) 
	{	
		try 
		{
			NotificationType nt =new NotificationType(sbn);		
			if(nt.isCallNotification() || nt.isOngoingNotification() 
					|| nt.isCollectionNotification())
			{
				return;
			}


			Notification n = sbn.getNotification(); 
			int n_id = sbn.getId();
			String tag = sbn.getTag();
			if(tag == null)
				tag = "unknown";
			String key = "unknown";
			if(new APILevel().getDeviceAPILevel() >= 20)
				key = sbn.getKey();
			int priority = n.priority;
			long arrivalTime = sbn.getPostTime();
			long removalTime = 0;
			int clicked = 0; //temporary
			boolean led = hasLED(n);
			boolean vibrate = hasVibration(n);
			boolean sound = hasSound(n);
			boolean unique_sound = hasUniqueSound(n);
			String app_name = getAppNameFromPackage(context, sbn.getPackageName());
			String app_package = sbn.getPackageName();
			String title = "";
			if(n.extras.getCharSequence(Notification.EXTRA_TITLE) != null)
				title = n.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
			if(title.equalsIgnoreCase(app_name) && n.extras.getCharSequence(Notification.EXTRA_TEXT) != null)
				title = n.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
			title = title.replace("\n", " ").trim();

            insertRecord(tag, key, priority, title, arrivalTime, removalTime, clicked, app_name, app_package);
			new Log().i("Saving notification in database: ");

		} 
		catch (Exception e) 
		{
			log.e("NotificationDataCollector: unable to save notification data in database " + e.toString());
		}

	}


	@SuppressLint("NewApi")
	protected void onNotificationRemoved(StatusBarNotification sbn)
	{

		NotificationType nt =new NotificationType(sbn);		
		if(nt.isCallNotification() || nt.isOngoingNotification() 
				|| nt.isCollectionNotification())
		{
			return;
		}

		final Notification n = sbn.getNotification();	

		// check for screen lock
		PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		@SuppressWarnings("deprecation")
		boolean isSceenAwake = (Build.VERSION.SDK_INT < 20 ? powerManager.isScreenOn():powerManager.isInteractive());
		new Log().e("Screen awake: " + isSceenAwake );

		final String app_name = getAppNameFromPackage(context, sbn.getPackageName());

        String title = "";

		if(n.extras.getCharSequence(Notification.EXTRA_TITLE) != null)
			title = n.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
		if(title.equalsIgnoreCase(app_name) && n.extras.getCharSequence(Notification.EXTRA_TEXT) != null)
			title = n.extras.getCharSequence(Notification.EXTRA_TEXT).toString().trim();

		NotificationData n_data = null;

        final String new_title = title;

		try {
			n_data = getRecord(app_name, title);
		}
		catch (Exception e){
			log.e("Error: unable to get notification data from shared pref!! " + e.toString());
		}

		if(n_data != null){
			n_data.setRemovalTime(Calendar.getInstance().getTimeInMillis());
			updateNotification(n_data);
		}
		else
		{
			log.d("Notification ignored because it was not saved in the SharedPref when posted!");
			log.d("Title: " + title);
		}

	}


	private void insertRecord(String tag, String key, int priority, String title, long arrival_time, long removal_time, int click, String app_n, String app_p) {
		ContentValues record = new ContentValues();

		record.put(NotificationsTable.KEY_NOTIF_TAG, tag);
		record.put(NotificationsTable.KEY_NOTIF_KEY, key);
        record.put(NotificationsTable.KEY_NOTIF_PRIORITY, priority);
        record.put(NotificationsTable.KEY_NOTIF_TITLE, title);
        record.put(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME, arrival_time);
        record.put(NotificationsTable.KEY_NOTIF_REMOVAL_TIME, removal_time);
        record.put(NotificationsTable.KEY_NOTIF_CLICKED, click);
        record.put(NotificationsTable.KEY_NOTIF_APP_NAME, app_n);
        record.put(NotificationsTable.KEY_NOTIF_APP_PACKAGE, app_p);


        localStorageController.insertRecord(NotificationsTable.TABLE_NOTIFICATIONS, record);
		android.util.Log.d("NOTIFICATIONS SERVICE", "Added record: ID: " + record.get(NotificationsTable.KEY_NOTIF_ID) + ", TAG: " + record.get(NotificationsTable.KEY_NOTIF_TAG)
                + ", KEY: " + record.get(NotificationsTable.KEY_NOTIF_KEY) + ", PRIORITY: " + record.get(NotificationsTable.KEY_NOTIF_PRIORITY) + ", TITLE: " + record.get(NotificationsTable.KEY_NOTIF_TITLE)
                + ", ARRIVAL_TIME: " + record.get(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME) + ", REMOVAL_TIME: " + record.get(NotificationsTable.KEY_NOTIF_REMOVAL_TIME)
                + ", CLICKED: " + record.get(NotificationsTable.KEY_NOTIF_CLICKED) + ", LED: "
                + ", APP_NAME: " + record.get(NotificationsTable.KEY_NOTIF_APP_NAME) + ", APP_Package: " + record.get(NotificationsTable.KEY_NOTIF_APP_PACKAGE));
	}

    private void updateNotification(NotificationData data){
        String clause = NotificationsTable.KEY_NOTIF_APP_NAME + " = \"" + data.getAppName()  + "\" AND " + NotificationsTable.KEY_NOTIF_TITLE + " = \"" + data.getTitle()  + "\"";

		ContentValues val = new ContentValues();
        val.put(NotificationsTable.KEY_NOTIF_APP_NAME, data.getAppName());
        val.put(NotificationsTable.KEY_NOTIF_APP_PACKAGE, data.getAppPackageName());
        val.put(NotificationsTable.KEY_NOTIF_TAG, data.getTag());
        val.put(NotificationsTable.KEY_NOTIF_KEY, data.getKey());
        val.put(NotificationsTable.KEY_NOTIF_PRIORITY, data.getPriority());
        val.put(NotificationsTable.KEY_NOTIF_TITLE, " ");
        val.put(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME, data.getArrivalTime());
        val.put(NotificationsTable.KEY_NOTIF_REMOVAL_TIME, data.getRemovalTime());
        val.put(NotificationsTable.KEY_NOTIF_CLICKED, data.getClicked());

        localStorageController.update(NotificationsTable.TABLE_NOTIFICATIONS, val, clause);

        android.util.Log.d("NOTIFICATIONS SERVICE", "Updated record: APP: " + val.get(NotificationsTable.KEY_NOTIF_APP_NAME) + ", TITLE: " + val.get(NotificationsTable.KEY_NOTIF_TITLE)
                + ", ARRIVAL TIME: " + val.get(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME) + ", REMOVAL TIME: " + val.get(NotificationsTable.KEY_NOTIF_REMOVAL_TIME));
    }

    private NotificationData getRecord(String app_name, String title){

        LocalStorageController localController = SQLiteController.getInstance(this.context);
        String query = "SELECT * FROM " + NotificationsTable.TABLE_NOTIFICATIONS + " WHERE " + NotificationsTable.KEY_NOTIF_APP_NAME + " = \"" + app_name  + "\" AND " + NotificationsTable.KEY_NOTIF_TITLE + " = \"" + title  + "\"";

        Cursor records = localController.rawQuery(query, null);
        records.moveToFirst();

        NotificationData data = new NotificationData(false);
        data.setTag(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_TAG)));
        data.setKey(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_KEY)));
        data.setPriority(records.getInt(records.getColumnIndex(NotificationsTable.KEY_NOTIF_PRIORITY)));
        data.setTitle(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_TITLE)));
        data.setArrivalTime(records.getLong(records.getColumnIndex(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME)));
        data.setRemovalTime(records.getLong(records.getColumnIndex(NotificationsTable.KEY_NOTIF_REMOVAL_TIME)));
        data.setClicked(records.getInt(records.getColumnIndex(NotificationsTable.KEY_NOTIF_CLICKED)));
        data.setAppName(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_APP_NAME)));
        data.setAppPackage(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_APP_PACKAGE)));

        records.close();

        log.e("The TITLE of the notification was: " + data.getTitle());
        return data;

    }


	private boolean hasLED(Notification n)
	{
		if(n.defaults == Notification.DEFAULT_ALL  || n.defaults == Notification.DEFAULT_LIGHTS 
				|| n.defaults == (Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
				|| n.defaults == (Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
				|| n.ledOnMS > 0)
			return true;
		return false;
	}

	private boolean hasVibration(Notification n)
	{
		long[] v_pattern = n.vibrate; 
		if(n.defaults == Notification.DEFAULT_ALL  || n.defaults == Notification.DEFAULT_VIBRATE
				|| n.defaults == (Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
				|| n.defaults == (Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				|| v_pattern != null)
			return true;
		return false;
	}

	private boolean hasSound(Notification n)
	{
		if(n.defaults == Notification.DEFAULT_ALL  || n.defaults == Notification.DEFAULT_SOUND
				|| n.defaults == (Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
				|| n.defaults == (Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
				|| n.sound != null)
			return true;
		return false;
	}

	private boolean hasUniqueSound(Notification n)
	{
		return n.sound != null;
	}


	private String getAppNameFromPackage(Context context, String package_name)
	{
		try 
		{
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(package_name, 0);
			if(applicationInfo == null)
			{
				return "UNKNOWN";
			}
			else
			{
				String app_name = packageManager.getApplicationLabel(applicationInfo).toString();
				if(app_name.toLowerCase(Locale.getDefault()).contains("google search"))
					app_name = "Google Services";
				return app_name;
			}
		} 
		catch (NameNotFoundException e) 
		{
			return "UNKNOWN";			
		}		
	}

}
