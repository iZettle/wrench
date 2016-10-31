package com.izettle.localconfig.application.database.tables;

import com.izettle.localconfig.application.library.ConfigurationFullCursorParser;

public class ConfigurationTable {
    public static final String TABLE_NAME = "configuration";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME
            + "("
            + ConfigurationFullCursorParser.Columns._ID + " INTEGER PRIMARY KEY, "
            + ConfigurationFullCursorParser.Columns.APPLICATION_ID + " INTEGER, "
            + ConfigurationFullCursorParser.Columns.KEY + " TEXT, "
            + ConfigurationFullCursorParser.Columns.VALUE + " TEXT, "
            + ConfigurationFullCursorParser.Columns.TYPE + " TEXT "
            + ")";


}
