/**
 * 
 */
package com.centauri.locus.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * A singleton class used to access the cache of bitmaps
 * 
 * @author mohitd2000
 * 
 */
public class BitmapCache {
    private static final String TAG = BitmapCache.class.getSimpleName();

    private static volatile BitmapCache instance = null;

    private LruCache<String, Bitmap> cache;
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory / 8;

    private BitmapCache() {
        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // Cache size in KB, not number of items
                return value.getByteCount() / 1024;
            }
        };
    }

    /**
     * A thread-safe (double-checked locking) way to get an the instance of this
     * singleton class
     * 
     * @return An instance of <code>BitmapCache</code>
     */
    public static BitmapCache getInstance() {
        if (instance == null) {
            synchronized (BitmapCache.class) {
                if (instance == null) {
                    instance = new BitmapCache();
                }
            }
        }

        return instance;
    }

    public Bitmap getBitmapFromCache(String key) {
        Log.i(TAG, "Retrieving bitmap: " + key);
        return cache.get(key);
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            Log.i(TAG, "Adding bitmap: " + key);
            cache.put(key, bitmap);
        }
    }
}
