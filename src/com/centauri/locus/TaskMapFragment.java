/**
 * 
 */
package com.centauri.locus;

import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centauri.locus.geofence.GeofenceRemover;
import com.centauri.locus.geofence.GeofenceRequester;
import com.centauri.locus.geofence.SimpleGeofence;
import com.centauri.locus.provider.Locus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
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
public class TaskMapFragment extends MapFragment implements ConnectionCallbacks,
        OnConnectionFailedListener {

    private GeofenceRequester geofenceRequester;
    private GeofenceRemover geofenceRemover;

    private LocationClient locationClient;

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
        locationClient = new LocationClient(getActivity(), this, this);
        locationClient.connect();
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

    /**
     * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    /**
     * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
     */
    @Override
    public void onConnected(Bundle args) {
        Location loc = locationClient.getLastLocation();
        LatLng latlon = new LatLng(loc.getLatitude(), loc.getLongitude());

        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latlon, 16.0f));
    }

    /**
     * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
     */
    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub

    }

}
