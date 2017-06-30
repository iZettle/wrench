package com.izettle.wrench.core;

import android.content.ContentValues;
import android.database.Cursor;

public class Bolt {
    public long id;
    public String type;
    public String key;
    public String value;


    public static Bolt fromContentValues(ContentValues values) {
        Bolt boltValue = new Bolt();
        if (values.containsKey(ColumnNames.Bolt.COL_ID)) {
            boltValue.id = values.getAsLong(ColumnNames.Bolt.COL_ID);
        }
        boltValue.key = values.getAsString(ColumnNames.Bolt.COL_KEY);
        boltValue.value = values.getAsString(ColumnNames.Bolt.COL_VALUE);
        boltValue.type = values.getAsString(ColumnNames.Bolt.COL_TYPE);

        return boltValue;
    }

    public static Bolt fromCursor(Cursor cursor) {
        Bolt bolt = new Bolt();
        bolt.id = cursor.getLong(cursor.getColumnIndex(ColumnNames.Bolt.COL_ID));
        bolt.key = cursor.getString(cursor.getColumnIndex(ColumnNames.Bolt.COL_KEY));
        bolt.value = cursor.getString(cursor.getColumnIndex(ColumnNames.Bolt.COL_VALUE));
        bolt.type = cursor.getString(cursor.getColumnIndex(ColumnNames.Bolt.COL_TYPE));

        return bolt;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ColumnNames.Bolt.COL_ID, id);
        contentValues.put(ColumnNames.Bolt.COL_KEY, key);
        contentValues.put(ColumnNames.Bolt.COL_VALUE, value);
        contentValues.put(ColumnNames.Bolt.COL_TYPE, type);

        return contentValues;
    }

}
