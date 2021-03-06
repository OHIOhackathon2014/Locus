/**
 * 
 */
package com.centauri.locus;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author mohitd2000
 * 
 */
public class GeofenceSelectorActivity extends FragmentActivity implements OnMapClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap map;
    private LocationClient locationClient;
    private LatLng markerLoc;
    private Marker marker;
    private Circle geofenceCircle;
    private boolean hasPlacedMarker = false;
    private boolean hasPlacedGeofence = false;

    /**
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_selector);

        locationClient = new LocationClient(this, this, this);
        locationClient.connect();

        setupIfNeeded();
        map.setOnMapClickListener(this);
        map.setMyLocationEnabled(true);

    }

    /**
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        setupIfNeeded();
    }

    /**
     * @see com.google.android.gms.maps.GoogleMap.OnMapClickListener#onMapClick(com.google.android.gms.maps.model.LatLng)
     */
    @Override
    public void onMapClick(LatLng location) {
        if (!hasPlacedMarker) {
            this.markerLoc = location;
            marker = map.addMarker(new MarkerOptions().position(markerLoc));
            Log.v("TAG", "markerLoc:" + markerLoc.latitude + "," + markerLoc.longitude);
            hasPlacedMarker = true;
        } else if (hasPlacedMarker && !hasPlacedGeofence) {
            CircleOptions circle = new CircleOptions();
            float[] result = new float[3];
            Location.distanceBetween(location.latitude, location.longitude, markerLoc.latitude,
                    markerLoc.longitude, result);

            if (markerLoc != null) {
                circle.center(markerLoc);
                circle.radius(result[0]);
                circle.fillColor(Color.argb(50, 51, 181, 229));
                circle.strokeColor(Color.argb(100, 0, 153, 204));
                circle.strokeWidth(5.0f);
            }

            geofenceCircle = map.addCircle(circle);
            hasPlacedGeofence = true;
        } else if (hasPlacedMarker && hasPlacedGeofence) {
            marker.remove();
            geofenceCircle.remove();
            hasPlacedMarker = false;
            hasPlacedGeofence = false;
        }
    }

    private void setupIfNeeded() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
    }

    /**
     * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO Auto-generated method stub

    }

    /**
     * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        Location loc = locationClient.getLastLocation();
        LatLng latlon = new LatLng(loc.getLatitude(), loc.getLongitude());

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlon, 16.0f));
    }

    /**
     * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
     */
    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub

    }

}
