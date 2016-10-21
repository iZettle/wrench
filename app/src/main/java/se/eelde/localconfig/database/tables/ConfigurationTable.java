package se.eelde.localconfig.database.tables;


import se.eelde.localconfiguration.library.Configuration;

public class ConfigurationTable {
    public static final String TABLE_NAME = "configuration";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME
            + "("
            + Configuration.Columns._ID + " INTEGER PRIMARY KEY, "
            + Configuration.Columns.KEY + " INTEGER, "
            + Configuration.Columns.APPLICATION_ID + " TEXT, "
            + Configuration.Columns.VALUE + " TEXT, "
            + Configuration.Columns.TYPE + " TEXT "
            + ")";

}
