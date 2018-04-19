package com.izettle.wrench.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Bolt {
    @NonNull
    @BoltType
    private final String type;
    @NonNull
    private final String key;
    @Nullable
    private final String value;
    private long id;

    public Bolt(Long id, @NonNull String type, @NonNull String key, @Nullable String value) {
        this.id = id;
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public Bolt() {
        type = "";
        key = "";
        value = null;
    }

    public static Bolt fromContentValues(ContentValues values) {
        Long id = values.getAsLong(ColumnNames.Bolt.COL_ID);
        @BoltType
        String type = values.getAsString(ColumnNames.Bolt.COL_TYPE);
        String key = values.getAsString(ColumnNames.Bolt.COL_KEY);
        String value = values.getAsString(ColumnNames.Bolt.COL_VALUE);

        return new Bolt(id, type, key, value);
    }

    public static Bolt fromCursor(Cursor cursor) {
        long id = getNullsafeLong(cursor, ColumnNames.Bolt.COL_ID);
        @BoltType
        String type = getNullsafeString(cursor, ColumnNames.Bolt.COL_TYPE);
        String key = getNullsafeString(cursor, ColumnNames.Bolt.COL_KEY);
        String value = cursor.getString(cursor.getColumnIndexOrThrow(ColumnNames.Bolt.COL_VALUE));

        return new Bolt(id, type, key, value);
    }

    @NonNull
    private static Long getNullsafeLong(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            throw new IllegalStateException(columnName + " was null");
        }
        return cursor.getLong(columnIndex);
    }

    @NonNull
    private static String getNullsafeString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        if (cursor.isNull(columnIndex)) {
            throw new IllegalStateException(columnName + " was null");
        }
        return cursor.getString(columnIndex);
    }

    @NonNull
    public String getKey() {
        return key;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ColumnNames.Bolt.COL_ID, id);
        contentValues.put(ColumnNames.Bolt.COL_KEY, key);
        contentValues.put(ColumnNames.Bolt.COL_VALUE, value);
        contentValues.put(ColumnNames.Bolt.COL_TYPE, type);

        return contentValues;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public Bolt copy(Long id, String type, String key, String value) {
        return new Bolt(id, type, key, value);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE.BOOLEAN, TYPE.STRING, TYPE.INTEGER, TYPE.ENUM})
    public @interface BoltType {
    }

    public interface TYPE {
        String BOOLEAN = "boolean";
        String STRING = "string";
        String INTEGER = "integer";
        String ENUM = "enum";
    }
}
