/**
 * 
 */
package com.centauri.locus;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.centauri.locus.adapter.TaskAdapter;
import com.centauri.locus.provider.Locus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mohitd2000
 * 
 */
public class TaskListFragment extends ListFragment implements MultiChoiceModeListener,
        OnItemLongClickListener {

    private static final String[] PROJECTION = { Locus.Task._ID, Locus.Task.COLUMN_TITLE,
        Locus.Task.COLUMN_DESCRIPTION, Locus.Task.COLUMN_LATITUDE, Locus.Task.COLUMN_LONGITUDE, };

    public interface OnListItemClickedCallback {
        public void onListItemClicked(ListView listView, View view, int position, long id);
    }

    private OnListItemClickedCallback callbacks;
    private TaskAdapter adapter;

    private List<Long> ids;
    private ActionMode actionMode;

    /**
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getActivity().getContentResolver().query(Locus.Task.CONTENT_URI,
                PROJECTION, null, null, null);
        adapter = new TaskAdapter(getActivity(), cursor, 0);
        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    /**
     * @see android.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     *      android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    /**
     * @see android.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(getActivity(), GeofenceSelectorActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(this);
        getListView().setOnItemLongClickListener(this);
        getListView().setBackground(getResources().getDrawable(R.drawable.listitem_background));
    }

    /**
     * @see android.app.Fragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
        Cursor cursor = getActivity().getContentResolver().query(Locus.Task.CONTENT_URI,
                PROJECTION, null, null, null);
        adapter.changeCursor(cursor);
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

    /**
     * @see android.view.ActionMode.Callback#onCreateActionMode(android.view.ActionMode,
     *      android.view.Menu)
     */
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_view_cab, menu);
        ids = new ArrayList<Long>();
        return true;
    }

    /**
     * @see android.view.ActionMode.Callback#onPrepareActionMode(android.view.ActionMode,
     *      android.view.Menu)
     */
    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    /**
     * @see android.view.ActionMode.Callback#onActionItemClicked(android.view.ActionMode,
     *      android.view.MenuItem)
     */
    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
        case R.id.menu_delete:
            for (Long id : ids) {
                Uri uri = ContentUris.withAppendedId(Locus.Task.CONTENT_URI, id);
                getActivity().getContentResolver().delete(uri, null, null);
            }
            Cursor cursor = getActivity().getContentResolver().query(Locus.Task.CONTENT_URI,
                    PROJECTION, null, null, null);
            adapter.swapCursor(cursor);
            adapter.notifyDataSetChanged();
            ids.clear();
            actionMode.finish();
            return true;

        default:
            return false;
        }
    }

    /**
     * @see android.view.ActionMode.Callback#onDestroyActionMode(android.view.ActionMode)
     */
    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        actionMode = null;
    }

    /**
     * @see android.widget.AbsListView.MultiChoiceModeListener#onItemCheckedStateChanged(android.view.ActionMode,
     *      int, long, boolean)
     */
    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id,
            boolean checked) {
        if (checked) {
            ids.add(id);
        } else {
            ids.remove(id);
        }
    }

    /**
     * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView,
     *      android.view.View, int, long)
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (actionMode != null) {
            return false;
        }

        actionMode = getActivity().startActionMode(this);
        getListView().setItemChecked(position, true);
        return true;
    }
}
