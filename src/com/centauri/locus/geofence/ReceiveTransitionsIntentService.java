package com.centauri.locus.geofence;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.centauri.locus.MainActivity;
import com.centauri.locus.R;
import com.centauri.locus.TaskEditActivity;
import com.centauri.locus.provider.Locus;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;

/**
 * This class receives geofence transition events from Location Services, in the
 * form of an Intent containing the transition type and geofence id(s) that
 * triggered the event.
 */
public class ReceiveTransitionsIntentService extends IntentService {

    private static final String[] PROJECTION = { Locus.Task._ID, Locus.Task.COLUMN_TITLE,
        Locus.Task.COLUMN_DESCRIPTION, };

    /**
     * Sets an identifier for this class' background thread
     */
    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }

    /**
     * Handles incoming intents
     * 
     * @param intent
     *            The Intent sent by Location Services. This Intent is provided
     *            to Location Services (inside a PendingIntent) when you call
     *            addGeofences()
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        // Create a local broadcast Intent
        Intent broadcastIntent = new Intent();

        // Give it the category for all intents sent by the Intent Service
        broadcastIntent.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

        // First check for errors
        if (LocationClient.hasError(intent)) {

            // Get the error code
            int errorCode = LocationClient.getErrorCode(intent);

            // Get the error message
            String errorMessage = String.valueOf(errorCode);

            // Log the error
            Log.e(GeofenceUtils.APPTAG,
                    getString(R.string.geofence_transition_error_detail, errorMessage));

            // Set the action and error message for the broadcast intent
            broadcastIntent.setAction(GeofenceUtils.ACTION_GEOFENCE_ERROR).putExtra(
                    GeofenceUtils.EXTRA_GEOFENCE_STATUS, errorMessage);

            // Broadcast the error *locally* to other components in this app
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            // If there's no error, get the transition type and create a
            // notification
        } else {

            // Get the type of transition (entry or exit)
            int transition = LocationClient.getGeofenceTransition(intent);

            // Test that a valid transition was reported
            if ((transition == Geofence.GEOFENCE_TRANSITION_ENTER)
                    || (transition == Geofence.GEOFENCE_TRANSITION_EXIT)) {

                // Post a notification
                List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
                String[] geofenceIds = new String[geofences.size()];
                for (int index = 0; index < geofences.size(); index++) {
                    geofenceIds[index] = geofences.get(index).getRequestId();
                }
                String ids = TextUtils.join(GeofenceUtils.GEOFENCE_ID_DELIMITER, geofenceIds);
                String transitionType = getTransitionString(transition);

                sendNotification(transitionType, ids);

                // Log the transition type and a message
                Log.d(GeofenceUtils.APPTAG,
                        getString(R.string.geofence_transition_notification_title, transitionType,
                                ids));
                Log.d(GeofenceUtils.APPTAG,
                        getString(R.string.geofence_transition_notification_text));

                // An invalid transition was reported
            } else {
                // Always log as an error
                Log.e(GeofenceUtils.APPTAG,
                        getString(R.string.geofence_transition_invalid_type, transition));
            }
        }
    }

    /**
     * Posts a notification in the notification bar when a transition is
     * detected. If the user clicks the notification, control goes to the main
     * Activity.
     * 
     * @param transitionType
     *            The type of transition that occurred.
     *
     */
    private void sendNotification(String transitionType, String ids) {

        // // Create an explicit content Intent that starts the main Activity
        // Intent notificationIntent = new Intent(getApplicationContext(),
        // MainActivity.class);
        //
        // // Construct a task stack
        // TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //
        // // Adds the main Activity to the task stack as the parent
        // stackBuilder.addParentStack(MainActivity.class);
        //
        // // Push the content Intent onto the stack
        // stackBuilder.addNextIntent(notificationIntent);
        //
        // // Get a PendingIntent containing the entire back stack
        // PendingIntent notificationPendingIntent =
        // stackBuilder.getPendingIntent(0,
        // PendingIntent.FLAG_UPDATE_CURRENT);
        //
        // // Get a notification builder that's compatible with platform
        // versions
        // // >= 4
        // NotificationCompat.Builder builder = new
        // NotificationCompat.Builder(this);
        //
        // // Set the notification contents
        // builder.setSmallIcon(R.drawable.ic_notification)
        // .setContentTitle(
        // getString(R.string.geofence_transition_notification_title,
        // transitionType,
        // ids))
        // .setContentText(getString(R.string.geofence_transition_notification_text))
        // .setContentIntent(notificationPendingIntent);
        //
        // // Get an instance of the Notification manager
        // NotificationManager mNotificationManager = (NotificationManager)
        // getSystemService(Context.NOTIFICATION_SERVICE);
        //
        // // Issue the notification
        // mNotificationManager.notify(0, builder.build());

        String[] stringIds = ids.split(",");
        long id = Long.parseLong(stringIds[0]);
        Uri taskUri = ContentUris.withAppendedId(Locus.Task.CONTENT_URI, id);
        Cursor cursor = getApplicationContext().getContentResolver().query(taskUri, PROJECTION,
                null, null, null);
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(Locus.Task.COLUMN_TITLE));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(Locus.Task.COLUMN_DESCRIPTION));
        cursor.close();

        Intent notificationIntent = new Intent(getApplicationContext(), TaskEditActivity.class);
        notificationIntent.putExtra(MainActivity.KEY_TASK_ID, id);

        Intent snoozeIntent = new Intent(getApplicationContext(), TaskEditActivity.class);
        snoozeIntent.putExtra(MainActivity.KEY_TASK_ID, id);
        snoozeIntent.setAction(TaskEditActivity.ACTION_SNOOZE);

        Intent confirmIntent = new Intent(getApplicationContext(), TaskEditActivity.class);
        confirmIntent.putExtra(MainActivity.KEY_TASK_ID, id);
        confirmIntent.setAction(TaskEditActivity.ACTION_CONFIRM);

        PendingIntent notificationPIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);
        PendingIntent snoozePIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                snoozeIntent, 0);
        PendingIntent confirmationPIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                confirmIntent, 0);

        Notification notif = new Notification.Builder(getApplicationContext())
                .setTicker("Don't forget: " + title).setContentTitle(title).setContentText(desc)
                .setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .addAction(R.drawable.ic_action_volume_muted, "Snooze", snoozePIntent)
                .addAction(R.drawable.ic_action_accept, "Got It!", confirmationPIntent)
                .setContentIntent(notificationPIntent).build();

        notif.flags = Notification.FLAG_AUTO_CANCEL;

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notifManager.notify(0, notif);
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     * 
     * @param transitionType
     *            A transition type constant defined in Geofence
     * @return A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {

        case Geofence.GEOFENCE_TRANSITION_ENTER:
            return getString(R.string.geofence_transition_entered);

        case Geofence.GEOFENCE_TRANSITION_EXIT:
            return getString(R.string.geofence_transition_exited);

        default:
            return getString(R.string.geofence_transition_unknown);
        }
    }
}
