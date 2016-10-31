package com.izettle.localconfig.application.database.tables;


import com.izettle.localconfig.application.library.Application;

public class ApplicationTable {
    public static final String TABLE_NAME = "application";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME
            + "("
            + Application.Columns._ID + " INTEGER PRIMARY KEY, "
            + Application.Columns.APPLICATION_NAME + " TEXT "
            + ")";

}
