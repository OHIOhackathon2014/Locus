/**
 * 
 */
package com.centauri.locus.dto;

/**
 * @author mohitd2000
 * 
 */
public class GeoPoint {
    private double latitude, longitude;

    /**
     * @param latitude
     * @param longitude
     */
    public GeoPoint(double latitude, double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
