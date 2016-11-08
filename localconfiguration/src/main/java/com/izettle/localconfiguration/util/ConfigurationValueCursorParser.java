package com.izettle.localconfiguration.util;


import android.database.Cursor;
import android.provider.BaseColumns;

import com.izettle.localconfiguration.ConfigurationValue;

public class ConfigurationValueCursorParser implements ICursorParser<ConfigurationValue> {
    public static final String[] PROJECTION = new String[]{
            Columns._ID,
            Columns.CONFIGURATION_ID,
            Columns.VALUE,
    };

    public ConfigurationValue populateFromCursor(ConfigurationValue configurationValue, Cursor cursor) {
        configurationValue._id = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        configurationValue.configurationId = cursor.getLong(cursor.getColumnIndex(Columns.CONFIGURATION_ID));
        configurationValue.value = cursor.getString(cursor.getColumnIndex(Columns.VALUE));

        return configurationValue;
    }

    public interface Columns extends BaseColumns {
        String CONFIGURATION_ID = "configurationId";
        String VALUE = "value";
    }
}
