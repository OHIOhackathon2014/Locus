/**
 * 
 */
package com.centauri.locus.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.centauri.locus.provider.Locus.Place;
import com.centauri.locus.provider.Locus.Task;

import java.util.HashMap;

/**
 * @author mohitd2000
 * 
 */
public class LocusProvider extends ContentProvider {

    private LocusDatabaseHelper databaseHelper;

    private static final int TASKS = 100;
    private static final int TASK_ID = 110;

    private static final int PLACES = 200;
    private static final int PLACE_ID = 210;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final HashMap<String, String> tasksProjectionMap;

    static {
        uriMatcher.addURI(Locus.AUTHORITY, "tasks", TASKS);
        uriMatcher.addURI(Locus.AUTHORITY, "tasks/*", TASK_ID);
        uriMatcher.addURI(Locus.AUTHORITY, "places", PLACES);
        uriMatcher.addURI(Locus.AUTHORITY, "places/*", PLACE_ID);

        tasksProjectionMap = new HashMap<String, String>();
        tasksProjectionMap.put(Locus.Task._ID, Locus.Task._ID);
        tasksProjectionMap.put(Locus.Task.COLUMN_TITLE, Locus.Task.COLUMN_TITLE);
        tasksProjectionMap.put(Locus.Task.COLUMN_DESCRIPTION, Locus.Task.COLUMN_DESCRIPTION);
        tasksProjectionMap.put(Locus.Task.COLUMN_LATITUDE, Locus.Task.COLUMN_LATITUDE);
        tasksProjectionMap.put(Locus.Task.COLUMN_LONGITUDE, Locus.Task.COLUMN_LONGITUDE);
        tasksProjectionMap.put(Locus.Task.COLUMN_RADIUS, Locus.Task.COLUMN_RADIUS);
        tasksProjectionMap.put(Locus.Task.COLUMN_DUE, Locus.Task.COLUMN_DUE);
        tasksProjectionMap.put(Locus.Task.COLUMN_COMPLETED, Locus.Task.COLUMN_COMPLETED);
    }

    /**
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
        databaseHelper = new LocusDatabaseHelper(getContext());
        return true;
    }

    /**
     * @see android.content.ContentProvider#query(android.net.Uri,
     *      java.lang.String[], java.lang.String, java.lang.String[],
     *      java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        final int match = uriMatcher.match(uri);
        switch (match) {
        case TASKS:
            builder.setTables(Task.TABLE_NAME);
            builder.setProjectionMap(tasksProjectionMap);
            break;

        case TASK_ID:
            builder.setTables(Task.TABLE_NAME);
            builder.appendWhere(Task._ID + "=" + uri.getLastPathSegment());
            break;

        case PLACES:
            builder.setTables(Place.TABLE_NAME);
            break;

        case PLACE_ID:
            builder.setTables(Place.TABLE_NAME);
            builder.appendWhere(Place._ID + "=" + uri.getLastPathSegment());
            break;

        default:
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /**
     * @see android.content.ContentProvider#insert(android.net.Uri,
     *      android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = 0;

        Uri result = null;
        final int match = uriMatcher.match(uri);

        switch (match) {
        case TASKS:
            id = db.insertOrThrow(Task.TABLE_NAME, null, values);
            result = ContentUris.withAppendedId(Task.CONTENT_URI, id);
            break;

        case PLACES:
            id = db.insertOrThrow(Place.TABLE_NAME, null, values);
            result = ContentUris.withAppendedId(Place.CONTENT_URI, id);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        if (id > 0 && result != null) {
            getContext().getContentResolver().notifyChange(uri, null);
            return result;
        }

        throw new SQLiteException("Cannot insert row into " + uri);
    }

    /**
     * @see android.content.ContentProvider#update(android.net.Uri,
     *      android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rows = 0;

        final int match = uriMatcher.match(uri);
        switch (match) {
        case TASKS:
            rows = db.update(Task.TABLE_NAME, values, selection, selectionArgs);
            break;

        case TASK_ID:
            String taskId = uri.getLastPathSegment();
            rows = db.update(Task.TABLE_NAME, values,
                    Task._ID + "=" + taskId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            break;

        case PLACES:
            rows = db.update(Place.TABLE_NAME, values, selection, selectionArgs);
            break;

        case PLACE_ID:
            String placeId = uri.getLastPathSegment();
            rows = db.update(Place.TABLE_NAME, values,
                    Task._ID + "=" + placeId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    /**
     * @see android.content.ContentProvider#delete(android.net.Uri,
     *      java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rows = 0;

        final int match = uriMatcher.match(uri);
        switch (match) {
        case TASKS:
            rows = db.delete(Task.TABLE_NAME, selection, selectionArgs);
            break;

        case TASK_ID:
            String taskId = uri.getLastPathSegment();
            rows = db.delete(Task.TABLE_NAME,
                    Task._ID + "=" + taskId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            break;

        case PLACES:
            rows = db.delete(Place.TABLE_NAME, selection, selectionArgs);
            break;

        case PLACE_ID:
            String placeId = uri.getLastPathSegment();
            rows = db.delete(Place.TABLE_NAME,
                    Task._ID + "=" + placeId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    /**
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
        case TASKS:
            return Task.CONTENT_TYPE;

        case TASK_ID:
            return Task.CONTENT_ITEM_TYPE;

        case PLACES:
            return Place.CONTENT_TYPE;

        case PLACE_ID:
            return Place.CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }
    }

}
