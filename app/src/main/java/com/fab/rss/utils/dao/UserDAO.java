package com.fab.rss.utils.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fab.rss.utils.models.AuthUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/28/17
 */

@SuppressWarnings("UnusedDeclaration")
public final class UserDAO extends DAOBase {

    private static final String TABLE_NAME = "user";
    private static final String COLUMN_USERNAME = "name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TOKEN = "token";
    private static final String COLUMN_RSS = "rss";

    static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " TEXT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_TOKEN + " TEXT );";


    static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public UserDAO(Context context) {
        super(context);
    }

    private AuthUser populateModel(Cursor cursor) {

        return new AuthUser(
                cursor.getString(cursor.getColumnIndex(UserDAO.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(UserDAO.COLUMN_USERNAME)),
                cursor.getString(cursor.getColumnIndex(UserDAO.COLUMN_TOKEN))
        );
    }

    private ContentValues populateContent(AuthUser user) {
        ContentValues values = new ContentValues();
        values.put(UserDAO.COLUMN_ID, user.getId() != null ? user.getId() : "");
        values.put(UserDAO.COLUMN_USERNAME, user.getName() != null ? user.getName() : "");
        values.put(UserDAO.COLUMN_TOKEN, user.getToken() != null ? user.getToken() : "");
        return values;
    }

    public long createUser(AuthUser _user) {

        if (!getUsers().isEmpty()) {
            for (AuthUser user : getUsers()) {
                deleteUser(user.getId());
            }
        }

        ContentValues values = populateContent(_user);
        return getDb().insert(UserDAO.TABLE_NAME, null, values);

    }

    public long updateUser(AuthUser user) {
        ContentValues values = populateContent(user);
        return getDb().update(UserDAO.TABLE_NAME, values, UserDAO.COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public AuthUser getUser() {
        String select = "SELECT * FROM " + UserDAO.TABLE_NAME;
        Cursor cursor = getDb().rawQuery(select, null);

        if (cursor.moveToNext()) {
            return populateModel(cursor);
        }

        cursor.close();
        this.close();
        return null;
    }

    public List<AuthUser> getUsers() {
        String select = "SELECT * FROM " + UserDAO.TABLE_NAME;
        Cursor cursor = getDb().rawQuery(select, null);
        List<AuthUser> userList = new ArrayList<>();

        while (cursor.moveToNext()) {
            userList.add(populateModel(cursor));
        }

        cursor.close();
        this.close();
        return userList;
    }

    public int deleteUser(String id) {
        return getDb().delete(UserDAO.TABLE_NAME, UserDAO.COLUMN_ID + " = ?", new String[]{id});
    }

}