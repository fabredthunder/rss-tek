package com.fab.rss;

import android.app.Application;

import com.fab.rss.utils.dao.UserDAO;
import com.fab.rss.utils.models.AuthUser;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      11/6/16
 */

@SuppressWarnings("UnusedDeclaration")
public class RSSApp extends Application {

    /**************
     * ATTRIBUTES *
     **************/

    private static final String TAG = "RSSApp";

    private static RSSApp instance = null;

    private static AuthUser mUser = null;

    /***********
     * METHODS *
     ***********/

    @Override public void onCreate() {
        super.onCreate();

        instance = this;

        UserDAO userDAO = new UserDAO(this);
        mUser = userDAO.getUser();
    }


    /**
     * @return the application's instance
     */
    public static synchronized RSSApp getInstance() {
        return instance;
    }

    /**
     * Users related functions
     */

    public static AuthUser getUser() {
        return mUser;
    }

    public static void updateUser(final AuthUser user) {
        UserDAO userDAO = new UserDAO(instance);
        userDAO.updateUser(user);
        mUser = user;
    }

    public static void deleteUser() {
        if (mUser != null) {
            UserDAO userDAO = new UserDAO(instance);
            userDAO.deleteUser(mUser.getId());
        }
    }

    public static void createUser(final AuthUser user) {
        UserDAO userDAO = new UserDAO(instance);
        userDAO.createUser(user);
        mUser = user;
    }

    public static String getToken() {
        return mUser.getToken();
    }

    public static boolean userIsConnected() {
        boolean connected = false;
        UserDAO userDAO = new UserDAO(instance);
        if (!userDAO.getUsers().isEmpty())
            connected = true;
        return connected;
    }
}
