package me.jasonclement.c196.ui;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import me.jasonclement.c196.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AppReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_TITLE = "me.jasonclement.c196.id.notification.title";
    public static final String NOTIFICATION_TEXT = "me.jasonclement.c196.id.notification.text";
    public static final String CHANNEL_ID = "me.jasonclement.c196";

    public static final int ASSESSMENT_DUE_ID = 0;
    public static final int ASSESSMENT_GOAL_ID = 1;
    public static final int COURSE_START_ID = 2;
    public static final int COURSE_END_ID = 3;

    private static int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra(NOTIFICATION_TITLE))
                .setContentText(intent.getStringExtra(NOTIFICATION_TEXT))
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notificationId++, notification);
    }

    public static void createAlarm(Context context, String title, String text, Date date, int id) {
        Calendar given = Calendar.getInstance();
        given.setTime(date);
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        if (given.getTimeInMillis() >= now.getTimeInMillis()) {
            Intent intent = new Intent(context, AppReceiver.class);
            intent.putExtra(NOTIFICATION_TITLE, title);
            intent.putExtra(NOTIFICATION_TEXT, text);
            PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
        }
    }
}
