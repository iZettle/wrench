package com.izettle.localconfig.application.library;


import android.database.Cursor;
import android.provider.BaseColumns;

import com.izettle.localconfiguration.util.ICursorParser;

public class ApplicationCursorParser implements ICursorParser<Application> {

    public static String[] PROJECTION = new String[]{
            Columns._ID,
            Columns.APPLICATION_ID,
            Columns.APPLICATION_LABEL
    };

    public ApplicationCursorParser() {
    }

    public Application populateFromCursor(Application application, Cursor cursor) {

        application._id = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        application.applicationId = cursor.getString(cursor.getColumnIndex(Columns.APPLICATION_ID));
        application.applicationLabel = cursor.getString(cursor.getColumnIndex(Columns.APPLICATION_LABEL));

        return application;
    }

    public interface Columns extends BaseColumns {
        String APPLICATION_ID = "applicationId";
        String APPLICATION_LABEL = "applicationLabel";
    }
}
