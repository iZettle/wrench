package se.eelde.localconfiguration.library.util;


import android.database.Cursor;
import android.provider.BaseColumns;

import se.eelde.localconfiguration.library.Configuration;

public class ConfigurationCursorParser implements ICursorParser<Configuration> {
    public final String[] PROJECTION = new String[]{
            Columns._ID,
            Columns.KEY,
            Columns.VALUE,
            Columns.TYPE
    };

    public Configuration populateFromCursor(Configuration configuration, Cursor cursor) {
        configuration._id = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        configuration.key = cursor.getString(cursor.getColumnIndex(Columns.KEY));
        configuration.value = cursor.getString(cursor.getColumnIndex(Columns.VALUE));
        configuration.type = cursor.getString(cursor.getColumnIndex(Columns.TYPE));

        return configuration;
    }

    public interface Columns extends BaseColumns {
        String KEY = "key";
        String VALUE = "value";
        String TYPE = "type";
    }
}
