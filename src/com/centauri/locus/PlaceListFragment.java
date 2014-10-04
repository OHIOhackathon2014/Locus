/**
 * 
 */
package com.centauri.locus;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;

import com.centauri.locus.adapter.PlaceAdapter;
import com.centauri.locus.provider.Locus;

/**
 * @author mohitd2000
 * 
 */
public class PlaceListFragment extends ListFragment {
    private static final String[] PROJECTION = { Locus.Place._ID, Locus.Place.COLUMN_TITLE,
        Locus.Place.COLUMN_LATITUDE, Locus.Place.COLUMN_LONGITUDE, };

    /**
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getActivity().getContentResolver().query(Locus.Place.CONTENT_URI,
                PROJECTION, null, null, null);
        PlaceAdapter adapter = new PlaceAdapter(getActivity(), cursor, 0);
        setListAdapter(adapter);
        setRetainInstance(true);
    }
}
