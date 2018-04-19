package com.izettle.wrench.core;

public final class ColumnNames {
    public interface Bolt {
        String COL_KEY = "configurationKey";
        String COL_ID = "id";
        String COL_VALUE = "value";
        String COL_TYPE = "configurationType";
    }

    public interface Nut {
        String COL_ID = "id";
        String COL_VALUE = "value";
        String COL_CONFIG_ID = "configurationId";
    }
}
