/**
 * 
 */
package com.centauri.locus;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * @author mohitd2000
 *
 */
public class TaskEditActivity extends Activity {
    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            TaskEditFragment fragment = new TaskEditFragment();
            fragment.setArguments(getIntent().getExtras());
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
            onBackPressed();
            break;

        }
        return super.onOptionsItemSelected(item);
    }
}
