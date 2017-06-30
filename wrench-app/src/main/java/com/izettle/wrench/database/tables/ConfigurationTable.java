package com.izettle.wrench.database.tables;

import com.izettle.wrench.core.ColumnNames;

public interface ConfigurationTable {
    String TABLE_NAME = "configuration";
    String COL_ID = ColumnNames.Bolt.COL_ID;
    String COL_APP_ID = "applicationId";
    String COL_KEY = ColumnNames.Bolt.COL_KEY;
    String COL_TYPE = ColumnNames.Bolt.COL_TYPE;
}

