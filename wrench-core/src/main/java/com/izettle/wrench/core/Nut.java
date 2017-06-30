package com.izettle.wrench.core;

import android.content.ContentValues;

public class Nut {

    public long id;
    public long configurationId;
    public String value;

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
