package com.fab.rss.utils.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/28/17
 */

@SuppressWarnings("UnusedDeclaration")
abstract class DAOBase extends SQLiteOpenHelper {

    private final static String DB_NAME = "database.db";
    private final static int DB_VERSION = 1;

    private SQLiteDatabase mDb = null;

    DAOBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(getClass().getSimpleName(), "Creating database");
            db.execSQL(UserDAO.TABLE_CREATE);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(getClass().getSimpleName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(UserDAO.TABLE_DROP);
        onCreate(db);
    }

    public SQLiteDatabase open() {
        mDb = super.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    protected SQLiteDatabase getDb() {
        return open();
    }

}