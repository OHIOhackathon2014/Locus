/**
 * 
 */
package com.centauri.locus;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.centauri.locus.provider.Locus;

/**
 * @author mohitd2000
 * 
 */
public class TaskEditFragment extends Fragment implements OnClickListener, OnDateSetListener,
        OnTimeSetListener {
    private static final String TAG = TaskEditFragment.class.getSimpleName();
    private static final String[] PROJECTION = { Locus.Task._ID, Locus.Task.COLUMN_TITLE,
        Locus.Task.COLUMN_DESCRIPTION };

    private Cursor taskCursor;
    private Uri taskUri;

    private EditText titleEditText;
    private EditText descEditText;
    private TextView dateTextView;
    private TextView timeTextView;

    /**
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long taskId = getArguments().getLong(MainActivity.KEY_TASK_ID);
            taskUri = ContentUris.withAppendedId(Locus.Task.CONTENT_URI, taskId);
            taskCursor = getActivity().getContentResolver().query(taskUri, PROJECTION, null, null,
                    null);
        }

    }

    /**
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
     *      android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);

        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        titleEditText = (EditText) view.findViewById(R.id.titleEditText);
        descEditText = (EditText) view.findViewById(R.id.descriptionEditText);

        dateTextView.setOnClickListener(this);
        timeTextView.setOnClickListener(this);

        ((ImageButton) view.findViewById(R.id.clearButton)).setOnClickListener(this);

        return view;
    }

    /**
     * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (taskCursor.getCount() > 0) {
            taskCursor.moveToFirst();
            String taskTitle = taskCursor.getString(taskCursor
                    .getColumnIndexOrThrow(Locus.Task.COLUMN_TITLE));
            String taskDescription = taskCursor.getString(taskCursor
                    .getColumnIndexOrThrow(Locus.Task.COLUMN_DESCRIPTION));
            taskCursor.close();
            EditText titleEditText = (EditText) getActivity().findViewById(R.id.titleEditText);
            EditText descEditText = (EditText) getActivity().findViewById(R.id.descriptionEditText);
            titleEditText.setText(taskTitle);
            descEditText.setText(taskDescription);
        }
    }

    /**
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
        case R.id.dateTextView:
            DatePickerDialog dateDialog = new DatePickerDialog();
            dateDialog.setOnDateSetListener(this);
            dateDialog.show(getFragmentManager(), "date");
            break;
        case R.id.timeTextView:
            TimePickerDialog timeDialog = new TimePickerDialog();
            timeDialog.setOnTimeSetListener(this);
            timeDialog.show(getFragmentManager(), "time");
            break;
        case R.id.clearButton:
            dateTextView.setText("Set date");
            timeTextView.setText("Off");
            break;
        }*/
    	if(view.getId() == R.id.dateTextView)
    	{
    		DatePickerDialog dateDialog = new DatePickerDialog();
    		dateDialog.setOnDateSetListener(this);
    		dateDialog.show(getFragmentManager(), "date");
    	}
    	else if(view.getId() == R.id.timeTextView)
    	{
    		TimePickerDialog timeDialog = new TimePickerDialog();
            timeDialog.setOnTimeSetListener(this);
            timeDialog.show(getFragmentManager(), "time");
    	}
    	else
    	{
    		dateTextView.setText("Set date");
            timeTextView.setText("Off");
    	}
    }

    /**
     * @see android.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    /**
     * @see com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener#onTimeSet(com.android.datetimepicker.time.RadialPickerLayout,
     *      int, int)
     */
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String AMPM = hourOfDay <= 12 ? "AM" : "PM";
        int hour = hourOfDay <= 12 ? hourOfDay : hourOfDay - 12;

        timeTextView.setText(hour + ":" + String.format("%02d", minute) + " " + AMPM);
    }

    /**
     * @see com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener#onDateSet(com.android.datetimepicker.date.DatePickerDialog,
     *      int, int, int)
     */
    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = new GregorianCalendar(tz);
        cal.set(year, monthOfYear, dayOfMonth);

        String day = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

        dateTextView.setText(day + ", " + month + " " + dayOfMonth + ", " + year);
    }

    private void saveData() {
        String title = titleEditText.getText().toString();
        String desc = descEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(Locus.Task.COLUMN_TITLE, title);
        values.put(Locus.Task.COLUMN_DESCRIPTION, desc);

        getActivity().getContentResolver().update(taskUri, values, null, null);
    }
}
