/**
 * 
 */
package com.centauri.locus;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.centauri.locus.provider.Locus;

/**
 * @author mohitd2000
 *
 */
public class TaskEditActivity extends Activity {
    public static final String ACTION_SNOOZE = "com.centauri.locus.action.SNOOZE";
    public static final String ACTION_CONFIRM = "com.centauri.locus.action.CONFIRM";

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getAction() != null) {

            long id = getIntent().getLongExtra(MainActivity.KEY_TASK_ID, 0);
            Uri taskUri = ContentUris.withAppendedId(Locus.Task.CONTENT_URI, id);
            ContentValues contentValues = new ContentValues();

            if (getIntent().getAction().equals(ACTION_CONFIRM)) {
                contentValues.put(Locus.Task.COLUMN_COMPLETED, 1);
            } else if (getIntent().getAction().equals(ACTION_SNOOZE)) {
                Cursor cursor = getContentResolver().query(taskUri,
                        new String[] { Locus.Task._ID, Locus.Task.COLUMN_DUE }, null, null, null);
                cursor.moveToFirst();
                long due = cursor.getLong(cursor.getColumnIndexOrThrow(Locus.Task.COLUMN_DUE));
                contentValues.put(Locus.Task.COLUMN_DUE, due + 3600000);
            }

            getContentResolver().update(taskUri, contentValues, null, null);
        }

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                ContentValues values = new ContentValues();
                values.put(Locus.Task.COLUMN_TITLE, "Task name");
                values.put(Locus.Task.COLUMN_DESCRIPTION, "");
                values.put(Locus.Task.COLUMN_LATITUDE, 0);
                values.put(Locus.Task.COLUMN_LONGITUDE, 0);
                Uri uri = getContentResolver().insert(Locus.Task.CONTENT_URI, values);
                long id = ContentUris.parseId(uri);
                bundle = new Bundle();
                bundle.putLong(MainActivity.KEY_TASK_ID, id);
            }

            TaskEditFragment fragment = new TaskEditFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(android.R.id.content, fragment)
                    .commit();
        }

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            break;

        }
        return super.onOptionsItemSelected(item);
    }
}
