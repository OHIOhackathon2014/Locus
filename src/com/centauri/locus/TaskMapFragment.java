/**
 * 
 */
package com.centauri.locus;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centauri.locus.geofence.GeofenceRemover;
import com.centauri.locus.geofence.GeofenceRequester;
import com.centauri.locus.geofence.SimpleGeofence;
import com.centauri.locus.provider.Locus;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mohitd2000
 * 
 */
public class TaskMapFragment extends MapFragment {

    private GeofenceRequester geofenceRequester;
    private GeofenceRemover geofenceRemover;

    private List<Geofence> geofences;
    private List<SimpleGeofence> simpleGeofences;

    private static final String[] PROJECTION = { Locus.Task._ID, Locus.Task.COLUMN_TITLE,
        Locus.Task.COLUMN_DESCRIPTION, Locus.Task.COLUMN_LATITUDE, Locus.Task.COLUMN_LONGITUDE,
        Locus.Task.COLUMN_RADIUS, Locus.Task.COLUMN_DUE };

    public TaskMapFragment() {
        super();
    }

    /**
     * @see com.google.android.gms.maps.MapFragment#onCreateView(android.view.LayoutInflater,
     *      android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * @see com.google.android.gms.maps.MapFragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @see android.app.Fragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
        getMap().setMyLocationEnabled(true);
        getMap().getUiSettings().setMyLocationButtonEnabled(true);
        geofences = new ArrayList<Geofence>();
        simpleGeofences = new ArrayList<SimpleGeofence>();
        geofenceRequester = new GeofenceRequester(getActivity());
        geofenceRemover = new GeofenceRemover(getActivity());

        Cursor cursor = getActivity().getContentResolver().query(Locus.Task.CONTENT_URI,
                PROJECTION, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(Locus.Task._ID));
            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(Locus.Task.COLUMN_LATITUDE));
            double lon = cursor
                    .getDouble(cursor.getColumnIndexOrThrow(Locus.Task.COLUMN_LONGITUDE));
            int radius = cursor.getInt(cursor.getColumnIndexOrThrow(Locus.Task.COLUMN_RADIUS));
            long due = cursor.getLong(cursor.getColumnIndexOrThrow(Locus.Task.COLUMN_DUE));

            Log.i("FEWAFPIWEJOIFJAWEIFOIAEWJIFJAEW", "id: " + id + " | lat: " + lat + " | lon: "
                    + lon + " | radius: " + radius + " | due: " + due);

            SimpleGeofence geofence = new SimpleGeofence(String.valueOf(id), lat, lon, radius, due,
                    Geofence.GEOFENCE_TRANSITION_ENTER);
            simpleGeofences.add(geofence);
            geofences.add(geofence.toGeofence());
        }

        geofenceRequester.addGeofences(geofences);
        showGeofences(simpleGeofences);
    }

    private void showGeofences(List<SimpleGeofence> geofences) {
        for (SimpleGeofence geofence : geofences) {
            GoogleMap map = getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(geofence.getLatitude(), geofence
                    .getLongitude())));
            CircleOptions circle = new CircleOptions();
            circle.center(new LatLng(geofence.getLatitude(), geofence.getLongitude()));
            circle.radius(geofence.getRadius());
            circle.fillColor(Color.argb(50, 51, 181, 229));
            circle.strokeColor(Color.argb(100, 0, 153, 204));
            circle.strokeWidth(5.0f);
            map.addCircle(circle);
        }
    }

}
