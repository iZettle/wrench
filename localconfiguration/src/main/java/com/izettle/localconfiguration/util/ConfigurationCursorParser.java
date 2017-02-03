package com.izettle.localconfiguration.util;


import android.database.Cursor;
import android.provider.BaseColumns;

import com.izettle.localconfiguration.Configuration;

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
        int columnIndex = cursor.getColumnIndex(Columns.VALUE);
        configuration.value = cursor.isNull(columnIndex) ? null : cursor.getString(columnIndex);
        configuration.type = cursor.getString(cursor.getColumnIndex(Columns.TYPE));

        return configuration;
    }

    public interface Columns extends BaseColumns {
        String KEY = "key";
        String VALUE = "value";
        String TYPE = "type";
    }
}
