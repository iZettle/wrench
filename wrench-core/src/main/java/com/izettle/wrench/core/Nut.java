package com.izettle.wrench.core;

import android.content.ContentValues;

public class Nut {
    private final long id;
    private final long configurationId;
    private final String value;

    public Nut(long configurationId, String value) {
        this.id = 0;
        this.configurationId = configurationId;
        this.value = value;
    }

    public Nut(long id, long configurationId, String value) {
        this.id = id;
        this.configurationId = configurationId;
        this.value = value;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        if (id > 0) {
            contentValues.put(ColumnNames.Nut.COL_ID, id);
        }

        contentValues.put(ColumnNames.Nut.COL_CONFIG_ID, configurationId);
        contentValues.put(ColumnNames.Nut.COL_VALUE, value);

        return contentValues;
    }
}
