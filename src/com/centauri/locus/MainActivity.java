package com.centauri.locus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.centauri.locus.geofence.GeofenceUtils;

public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        TaskListFragment.OnListItemClickedCallback {

    public static final String KEY_TASK_ID = "task_id";

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence title;

    private GeofenceReceiver geofenceReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        geofenceReceiver = new GeofenceReceiver();

        // Create an intent filter for the broadcast receiver
        intentFilter = new IntentFilter();

        // Action for broadcast Intents that report successful addition of
        // geofences
        intentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);

        // Action for broadcast Intents that report successful removal of
        // geofences
        intentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);

        // Action for broadcast Intents containing various types of geofencing
        // errors
        intentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);

        // All Location Services sample apps use this category
        intentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
        case 0:
            fragmentManager.beginTransaction().replace(R.id.container, new TaskListFragment())
                    .commit();
            break;
        case 1:
            startActivity(new Intent(this, GeofenceSelectorActivity.class));
            break;
        case 2:
            fragmentManager.beginTransaction().replace(R.id.container, new TaskMapFragment())
                    .commit();
            break;
        case 3:
            fragmentManager.beginTransaction().replace(R.id.container, new PlaceListFragment())
                    .commit();
            break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
        case 1:
            title = getString(R.string.title_ongoing);
            break;
        case 2:
            title = getString(R.string.title_nearby);
            break;
        case 3:
            title = getString(R.string.title_completed);
            break;
        case 4:
            title = getString(R.string.title_map);
            break;
        case 5:
            title = getString(R.string.title_places);
            break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @see com.centauri.locus.TaskListFragment.OnListItemClickedCallback#onListItemClicked(android.widget.ListView,
     *      android.view.View, int, long)
     */
    @Override
    public void onListItemClicked(ListView listView, View view, int position, long id) {
        Intent intent = new Intent(this, TaskEditActivity.class);
        intent.putExtra(KEY_TASK_ID, id);
        startActivity(intent);
    }

    /**
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(geofenceReceiver, intentFilter);
    }

    public class GeofenceReceiver extends BroadcastReceiver {

        /**
         * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
         *      android.content.Intent)
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check the action code and determine what to do
            String action = intent.getAction();

            // Intent contains information about errors in adding or removing
            // geofences
            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

                handleGeofenceError(context, intent);

                // Intent contains information about successful addition or
                // removal of geofences
            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
                    || TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

                handleGeofenceStatus(context, intent);

                // Intent contains information about a geofence transition
            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

                handleGeofenceTransition(context, intent);

                // The Intent contained an invalid action
            } else {
                Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * If you want to display a UI message about adding or removing
         * geofences, put it here.
         *
         * @param context
         *            A Context for this component
         * @param intent
         *            The received broadcast Intent
         */
        private void handleGeofenceStatus(Context context, Intent intent) {

        }

        /**
         * Report geofence transitions to the UI
         *
         * @param context
         *            A Context for this component
         * @param intent
         *            The Intent containing the transition
         */
        private void handleGeofenceTransition(Context context, Intent intent) {
            /*
             * If you want to change the UI when a transition occurs, put the
             * code here. The current design of the app uses a notification to
             * inform the user that a transition has occurred.
             */
        }

        /**
         * Report addition or removal errors to the UI, using a Toast
         *
         * @param intent
         *            A broadcast Intent sent by ReceiveTransitionsIntentService
         */
        private void handleGeofenceError(Context context, Intent intent) {
            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
            Log.e(GeofenceUtils.APPTAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }

    }

}
