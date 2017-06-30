package com.izettle.wrench.database.tables;

import com.izettle.wrench.core.ColumnNames;

public interface ConfigurationValueTable {
    String TABLE_NAME = "configurationValue";
    String COL_ID = ColumnNames.Bolt.COL_ID;
    String COL_CONFIG_ID = "configurationId";
    String COL_VALUE = ColumnNames.Bolt.COL_VALUE;
    String COL_SCOPE = "scope";
}
