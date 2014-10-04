/**
 * 
 */
package com.centauri.locus.dto;

import static com.centauri.locus.provider.Locus.Task.COLUMN_COMPLETED;
import static com.centauri.locus.provider.Locus.Task.COLUMN_DESCRIPTION;
import static com.centauri.locus.provider.Locus.Task.COLUMN_DUE;
import static com.centauri.locus.provider.Locus.Task.COLUMN_LATITUDE;
import static com.centauri.locus.provider.Locus.Task.COLUMN_LONGITUDE;
import static com.centauri.locus.provider.Locus.Task.COLUMN_RADIUS;
import static com.centauri.locus.provider.Locus.Task.COLUMN_TITLE;

import android.content.ContentValues;

/**
 * @author mohitd2000
 * 
 */
public class TaskDTO {

    private String title;
    private String description;
    private GeoPoint location;
    private int radius;
    private String due;
    private boolean completed;

    public TaskDTO() {
        super();
    }

    /**
     * @param title
     * @param description
     * @param location
     * @param radius
     * @param due
     * @param completed
     */
    public TaskDTO(String title, String description, GeoPoint location, int radius, String due,
            boolean completed) {
        super();
        this.title = title;
        this.description = description;
        this.location = location;
        this.radius = radius;
        this.due = due;
        this.completed = completed;
    }

    public static TaskDTO fromContentValues(ContentValues values) {
        String title = values.getAsString(COLUMN_TITLE);
        String description = values.getAsString(COLUMN_DESCRIPTION);
        double lat = values.getAsDouble(COLUMN_LATITUDE);
        double lon = values.getAsDouble(COLUMN_LONGITUDE);
        int radius = values.getAsInteger(COLUMN_RADIUS);
        String due = values.getAsString(COLUMN_DUE);
        int completed = values.getAsInteger(COLUMN_COMPLETED);

        GeoPoint point = new GeoPoint(lat, lon);
        boolean comp = completed == 0 ? false : true;

        return new TaskDTO(title, description, point, radius, due, comp);
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, this.title);
        values.put(COLUMN_DESCRIPTION, this.description);
        values.put(COLUMN_LATITUDE, this.location.getLatitude());
        values.put(COLUMN_LONGITUDE, this.location.getLongitude());
        values.put(COLUMN_RADIUS, this.radius);
        values.put(COLUMN_DUE, this.due);
        values.put(COLUMN_COMPLETED, this.completed ? 1 : 0);

        return values;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public GeoPoint getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    /**
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @param radius
     *            the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * @return the due
     */
    public String getDue() {
        return due;
    }

    /**
     * @param due
     *            the due to set
     */
    public void setDue(String due) {
        this.due = due;
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed
     *            the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
