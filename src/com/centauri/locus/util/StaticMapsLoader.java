/**
 * 
 */
package com.centauri.locus.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.centauri.locus.dto.GeoPoint;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author mohitd2000
 * 
 */
public class StaticMapsLoader extends AsyncTask<GeoPoint, Void, Bitmap> {

    public static final int SIZE_SMALL = 1;
    public static final int SIZE_LARGE = 2;
    public static final int TABLE_TASK = 3;
    public static final int TABLE_PLACE = 4;

    private static final String API_KEY = "AIzaSyBMaDUE0ST0ajbW6F0hbTUkH6RP_zNE04Y";

    private ImageView imageView;
    private int id, size, table;
    private BitmapCache cache;

    public StaticMapsLoader(ImageView imageView, int id, int size, int table) {
        this.imageView = imageView;
        this.id = id;
        this.size = size;
        this.table = table;
        cache = BitmapCache.getInstance();
    }

    /**
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Bitmap doInBackground(GeoPoint... params) {
        double lat = params[0].getLatitude();
        double lon = params[0].getLongitude();
        String baseURL = "http://maps.googleapis.com/maps/api/staticmap?";
        String center = "center=" + lat + "," + lon;
        String size;
        if (this.size == SIZE_LARGE) {
            size = "size="; // TODO: Find a large size!
        } else {
            size = "size=90x90";
        }
        String scale = "scale=2";
        String marker = "markers=color:red%7C" + lat + "," + lon;
        String URL = baseURL + '&' + center + '&' + size + '&' + scale + '&' + marker + "&key="
                + API_KEY;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        Bitmap bitmap = null;
        try {
            InputStream is = client.execute(request).getEntity().getContent();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        storeBitmapInCache(id, this.size, this.table, bitmap);
        return bitmap;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null) {
            imageView.setImageBitmap(result);
        }
    }

    private void storeBitmapInCache(int id, int size, int table, Bitmap bitmap) {
        char sizePrefix, tablePrefix;
        if (this.size == SIZE_LARGE) {
            sizePrefix = 'l';
        } else {
            sizePrefix = 's';
        }

        if (this.table == TABLE_TASK) {
            tablePrefix = 't';
        } else if (this.table == TABLE_PLACE) {
            tablePrefix = 'p';
        } else {
            // IT SHOULD NEVER COME TO THIS!
            tablePrefix = 'x';
        }

        String key = tablePrefix + sizePrefix + String.valueOf(id);
        cache.addBitmapToCache(key, bitmap);
    }

}
