/**
 * 
 */
package com.centauri.locus.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.centauri.locus.provider.Locus.Place;
import com.centauri.locus.provider.Locus.Task;

/**
 * @author mohitd2000
 * 
 */
public class LocusDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "locus.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public LocusDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Task.TABLE_NAME + "(" + Locus.Task._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Locus.Task.COLUMN_TITLE
                + " TEXT NOT NULL," + Locus.Task.COLUMN_DESCRIPTION + " TEXT,"
                + Locus.Task.COLUMN_LATITUDE + " REAL," + Locus.Task.COLUMN_LONGITUDE + " REAL,"
                + Locus.Task.COLUMN_RADIUS + " INTEGER," + Locus.Task.COLUMN_DUE + " REAL,"
                + Locus.Task.COLUMN_COMPLETED + " INTEGER" + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Place.TABLE_NAME + "(" + Locus.Place._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Locus.Place.COLUMN_TITLE
                + " TEXT NOT NULL," + Locus.Place.COLUMN_LATITUDE + " REAL,"
                + Locus.Place.COLUMN_LONGITUDE + " REAL" + ");");

        db.execSQL("INSERT INTO task (title, description, latitude, longitude) VALUES('Do some task', 'This is some description that is hopefully much much much much longer than the description box', "
                + "40.7127, -74.0059);");
        db.execSQL("INSERT INTO task (title, latitude, longitude) VALUES('Do some task that is hopefully much longer than the space provided for the title', "
                + "40.0000, -83.0145);");

        db.execSQL("INSERT INTO place (title, latitude, longitude) VALUES('New York City',"
                + "40.7127, -74.0059);");
        db.execSQL("INSERT INTO place (title, latitude, longitude) VALUES('The Ohio State University', "
                + "40.000, -83.0145);");

    }

    /**
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
     *      int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
