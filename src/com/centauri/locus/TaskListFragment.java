/**
 * 
 */
package com.centauri.locus;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.centauri.locus.adapter.TaskAdapter;
import com.centauri.locus.provider.Locus;

/**
 * @author mohitd2000
 * 
 */
public class TaskListFragment extends ListFragment {

    private static final String[] PROJECTION = { Locus.Task._ID, Locus.Task.COLUMN_TITLE,
        Locus.Task.COLUMN_DESCRIPTION, Locus.Task.COLUMN_LATITUDE, Locus.Task.COLUMN_LONGITUDE, };

    public interface OnListItemClickedCallback {
        public void onListItemClicked(ListView listView, View view, int position, long id);
    }

    private OnListItemClickedCallback callbacks;

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

    /**
     * @see android.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (OnListItemClickedCallback) activity;
        } catch (ClassCastException e) {
            // TODO: handle exception
        }
    }

    /**
     * @see android.app.ListFragment#onListItemClick(android.widget.ListView,
     *      android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (callbacks != null) {
            callbacks.onListItemClicked(l, v, position, id);
        }
    }
}
