package com.centauri.locus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

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
            TaskEditFragment editTaskFragment = new TaskEditFragment();
            Bundle args = new Bundle();
            args.putLong(KEY_TASK_ID, 1);
            editTaskFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.container, editTaskFragment).commit();
            break;
        case 3:
            fragmentManager.beginTransaction().replace(R.id.container, new TaskMapFragment())
                    .commit();
            break;
        case 4:
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
            getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
