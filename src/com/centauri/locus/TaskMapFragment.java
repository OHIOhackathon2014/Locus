/**
 * 
 */
package com.centauri.locus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

/**
 * @author mohitd2000
 * 
 */
public class TaskMapFragment extends MapFragment {

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
    }

}
