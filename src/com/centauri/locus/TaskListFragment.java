/**
 * 
 */
package com.centauri.locus;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;

import com.centauri.locus.adapter.TaskAdapter;
import com.centauri.locus.provider.Locus;

/**
 * @author mohitd2000
 * 
 */
public class TaskListFragment extends ListFragment {

    private static final String[] PROJECTION = { Locus.Task._ID, Locus.Task.COLUMN_TITLE,
        Locus.Task.COLUMN_DESCRIPTION, Locus.Task.COLUMN_LATITUDE, Locus.Task.COLUMN_LONGITUDE, };

    /**
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getActivity().getContentResolver().query(Locus.Task.CONTENT_URI,
                PROJECTION, null, null, null);
        TaskAdapter adapter = new TaskAdapter(getActivity(), cursor, 0);
        setListAdapter(adapter);
        setRetainInstance(true);
    }
}
