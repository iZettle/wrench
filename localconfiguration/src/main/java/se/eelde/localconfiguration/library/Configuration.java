package se.eelde.localconfiguration.library;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public class Configuration {
    public static final String[] PROJECTION = new String[]{
            Columns._ID,
            Columns.APPLICATION_ID,
            Columns.KEY,
            Columns.VALUE,
            Columns.TYPE
    };

    public long _id;
    public long applicationId;
    public String type;
    public String key;
    public String value;

    public Configuration() {
    }

    public static Configuration configurationFromCursor(Cursor cursor) {
        Configuration configuration = new Configuration();

        configuration._id = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        configuration.applicationId = cursor.getLong(cursor.getColumnIndex(Columns.APPLICATION_ID));
        configuration.key = cursor.getString(cursor.getColumnIndex(Columns.KEY));
        configuration.value = cursor.getString(cursor.getColumnIndex(Columns.VALUE));
        configuration.type = cursor.getString(cursor.getColumnIndex(Columns.TYPE));

        return configuration;
    }

    public static ContentValues toContentValues(Configuration configuration) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.APPLICATION_ID, configuration.applicationId);
        contentValues.put(Columns.KEY, configuration.key);
        contentValues.put(Columns.VALUE, configuration.value);
        contentValues.put(Columns.TYPE, configuration.type);
        return contentValues;
    }


    public interface Columns extends BaseColumns {
        String APPLICATION_ID = "applicationName";
        String KEY = "key";
        String VALUE = "value";
        String TYPE = "type";
    }
}
