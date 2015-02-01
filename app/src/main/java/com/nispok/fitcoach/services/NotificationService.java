package com.nispok.fitcoach.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.nispok.fitcoach.models.Goal;

/**
 * This {@code IntentService} does the app's actual work.
 * {@link com.nispok.fitcoach.receivers.AlarmReceiver}
 * (a {@link android.support.v4.content.WakefulBroadcastReceiver}) holds a partial wake lock for
 * this service while the service does its work. When the service is finished, it calls
 * {@link com.nispok.fitcoach.receivers.AlarmReceiver#completeWakefulIntent(android.content.Intent)}
 * to release the wake lock.
 */
public class NotificationService extends IntentService {

    private static final String TAG = NotificationService.class.getSimpleName();

    public static final String EXTRA_GOAL = "EXTRA_GOAL";

    /**
     * Creates an IntentService. Invoked by your subclass's constructor. Class name is used to name
     * the worker thread, important only for debugging.
     */
    public NotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Goal goal = (Goal) intent.getExtras().getSerializable(EXTRA_GOAL);
        Log.d(TAG, goal.toString());
    }
}
