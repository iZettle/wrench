package com.izettle.localconfig.application.library;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;


public class Application {
    public static final String[] PROJECTION = new String[]{
            Columns._ID,
            Columns.APPLICATION_NAME
    };
    public long _id;
    public String applicationName;

    public static Application applicationFromCursor(Cursor cursor) {
        Application application = new Application();

        application._id = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        application.applicationName = cursor.getString(cursor.getColumnIndex(Columns.APPLICATION_NAME));

        return application;
    }

    public static ContentValues toContentValues(Application application) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.APPLICATION_NAME, application.applicationName);
        return contentValues;
    }

    public interface Columns extends BaseColumns {
        String APPLICATION_NAME = "applicationName";
    }
}
