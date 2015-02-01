package com.nispok.fitcoach.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.nispok.fitcoach.FitCoach;
import com.nispok.fitcoach.models.Goal;
import com.nispok.fitcoach.services.GoalService;
import com.nispok.fitcoach.services.NotificationService;

import java.util.Calendar;
import java.util.List;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@link com.nispok.fitcoach.services.NotificationService}
 * to do some work.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmManager;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    private Context context = FitCoach.getApplication().getApplicationContext();

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
         * If your receiver intent includes extras that need to be passed along to the
         * service, use setComponent() to indicate that the service should handle the
         * receiver's intent. For example:
         *
         * ComponentName comp = new ComponentName(context.getPackageName(),
         *      MyService.class.getName());
         *
         * // This intent passed in this call will include the wake lock extra as well as
         * // the receiver intent contents.
         * startWakefulService(context, (intent.setComponent(comp)));
         *
         * In this example, we simply create a new intent to deliver to the service.
         * This intent holds an extra identifying the wake lock.
         */
        Intent service = new Intent(context, NotificationService.class);
        service.setComponent(new ComponentName(context.getPackageName(),
                NotificationService.class.getName()));

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
    }

    public void setAlarms() {

        List<Goal> goals = GoalService.get();

        for (Goal goal : goals) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("GOAL", goal);
            // Might need to change requestCode with multiple alarms
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            // Set the alarm's trigger time from the goal
            calendar.set(Calendar.HOUR_OF_DAY, goal.getNotifications().get(0).getTime().getHour());
            calendar.set(Calendar.MINUTE, goal.getNotifications().get(0).getTime().getMinute());

            // Set the alarm to fire at approximately the time set from the goal,
            // according to the device's clock, and to repeat once a day.
            // TODO: Interval should be set from the goal
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }

        // Enable {@link BootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

}
