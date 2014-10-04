/**
 * 
 */
package com.centauri.locus.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author mohitd2000
 * 
 */
public final class Locus {

    private Locus() {
    }

    public static final String AUTHORITY = "com.centauri.locus.provider.LocusProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String PATH_TASKS = "tasks";
    private static final String PATH_PLACES = "places";

    public static final class Task implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS)
                .build();

        public static final String TABLE_NAME = "task";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_RADIUS = "radius";
        public static final String COLUMN_DUE = "due";
        public static final String COLUMN_COMPLETED = "completed";

        public static final String DEFAULT_SORT_ORDER = COLUMN_TITLE + " DESC";

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.centauri.task";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.centauri.task";
    }

    public static final class Place implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES)
                .build();

        public static final String TABLE_NAME = "place";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";

        public static final String DEFAULT_SORT_ORDER = COLUMN_TITLE + " DESC";

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.centauri.place";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.centauri.place";
    }

}
