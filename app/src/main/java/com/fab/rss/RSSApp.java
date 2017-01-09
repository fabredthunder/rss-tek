package com.fab.rss;

import android.app.Application;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      11/6/16
 */

public class RSSApp extends Application {

    /**************
     * ATTRIBUTES *
     **************/

    private static final String TAG = "RSSApp";

    private static RSSApp instance = null;

    /***********
     * METHODS *
     ***********/

    @Override public void onCreate() {
        super.onCreate();

        instance = this;
    }


    /**
     * @return the application's instance
     */
    public static synchronized RSSApp getInstance() {
        return instance;
    }
}
