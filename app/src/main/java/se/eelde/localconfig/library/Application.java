package se.eelde.localconfig.library;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import se.eelde.localconfiguration.library.util.ConfigurationCursorParser;

public class Application {
    public static final String[] PROJECTION = new String[]{
            Columns._ID,
            Columns.APPLICATION_NAME
    };
    public long _id;
    public String applicationName;

    public static Application applicationFromCursor(Cursor cursor) {
        Application application = new Application();

        application._id = cursor.getLong(cursor.getColumnIndex(ConfigurationCursorParser.Columns._ID));
        application.applicationName = cursor.getString(cursor.getColumnIndex(ConfigurationFullCursorParser.Columns.APPLICATION_ID));

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
