package com.fab.rss.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fab.rss.RSSApp;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      11/2/16
 */
public class UtilsFunctions {

    /**
     * Check network availablity
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) RSSApp.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
