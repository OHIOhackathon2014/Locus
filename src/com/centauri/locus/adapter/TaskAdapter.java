/**
 * 
 */
package com.centauri.locus.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.centauri.locus.R;
import com.centauri.locus.dto.GeoPoint;
import com.centauri.locus.provider.Locus;
import com.centauri.locus.util.BitmapCache;
import com.centauri.locus.util.StaticMapsLoader;

/**
 * @author mohitd2000
 * 
 */
public class TaskAdapter extends CursorAdapter {

    private BitmapCache cache;

    /**
     * @param context
     * @param c
     */
    public TaskAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cache = BitmapCache.getInstance();
    }

    /**
     * @see android.widget.CursorAdapter#newView(android.content.Context,
     *      android.database.Cursor, android.view.ViewGroup)
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_task_no_description, parent, false);

        return view;
    }

    /**
     * @see android.widget.CursorAdapter#bindView(android.view.View,
     *      android.content.Context, android.database.Cursor)
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Locus.Task._ID));
        String title = cursor.getString(cursor.getColumnIndex(Locus.Task.COLUMN_TITLE));
        double lat = cursor.getDouble(cursor.getColumnIndex(Locus.Task.COLUMN_LATITUDE));
        double lon = cursor.getDouble(cursor.getColumnIndex(Locus.Task.COLUMN_LONGITUDE));

        TextView titleTextView = (TextView) view.findViewById(R.id.title_textview);
        ImageView mapImageView = (ImageView) view.findViewById(R.id.location_imageview);

        titleTextView.setText(title);

        String key = 't' + 's' + String.valueOf(id);
        Bitmap image = cache.getBitmapFromCache(key);
        if (image != null) {
            mapImageView.setImageBitmap(image);
        } else {
            StaticMapsLoader task = new StaticMapsLoader(mapImageView, id,
                    StaticMapsLoader.SIZE_SMALL, StaticMapsLoader.TABLE_TASK);
            task.execute(new GeoPoint(lat, lon));
        }

    }
}
