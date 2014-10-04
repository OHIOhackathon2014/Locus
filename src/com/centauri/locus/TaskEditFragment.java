/**
 * 
 */
package com.centauri.locus;

import android.app.Fragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.centauri.locus.provider.Locus;

/**
 * @author mohitd2000
 * 
 */
public class TaskEditFragment extends Fragment {
    private static final String[] PROJECTION = { Locus.Task._ID, Locus.Task.COLUMN_TITLE,
        Locus.Task.COLUMN_DESCRIPTION };

    private long taskId;
    private Cursor taskCursor;

    /**
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.taskId = getArguments().getLong(MainActivity.KEY_TASK_ID);
        }

        Uri taskUri = ContentUris.withAppendedId(Locus.Task.CONTENT_URI, taskId);
        taskCursor = getActivity().getContentResolver()
                .query(taskUri, PROJECTION, null, null, null);

    }

    /**
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
     *      android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        return view;
    }

    /**
     * @see android.app.Fragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
        String taskTitle = taskCursor.getString(taskCursor
                .getColumnIndexOrThrow(Locus.Task.COLUMN_TITLE));
        String taskDescription = taskCursor.getString(taskCursor
                .getColumnIndexOrThrow(Locus.Task.COLUMN_TITLE));

        EditText titleEditText = (EditText) getActivity().findViewById(R.id.titleEditText);
        EditText descEditText = (EditText) getActivity().findViewById(R.id.descriptionEditText);

        titleEditText.setText(taskTitle);
        descEditText.setText(taskDescription);
    }
}
