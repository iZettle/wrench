package com.izettle.localconfig.application.database.tables;

import com.izettle.localconfig.application.library.ApplicationCursorParser;

public class ApplicationTable {
    public static final String TABLE_NAME = "application";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME
            + "("
            + ApplicationCursorParser.Columns._ID + " INTEGER PRIMARY KEY, "
            + ApplicationCursorParser.Columns.APPLICATION_NAME + " TEXT "
            + ")";

}
