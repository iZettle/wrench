package com.izettle.localconfig.application.database.tables;

import com.izettle.localconfiguration.util.ConfigurationValueCursorParser;

public class ConfigurationValueTable {
    public static final String TABLE_NAME = "configurationValue";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME
            + "("
            + ConfigurationValueCursorParser.Columns._ID + " INTEGER PRIMARY KEY, "
            + ConfigurationValueCursorParser.Columns.CONFIGURATION_ID + " INTEGER, "
            + ConfigurationValueCursorParser.Columns.VALUE + " TEXT "
            + ")";


}
