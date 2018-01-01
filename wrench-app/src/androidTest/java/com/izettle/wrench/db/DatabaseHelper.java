package com.izettle.wrench.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.izettle.wrench.database.tables.ApplicationTable;
import com.izettle.wrench.database.tables.ConfigurationTable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;
import static junit.framework.Assert.assertNotNull;

public class DatabaseHelper {
    public static long insertWrenchConfiguration(SupportSQLiteDatabase db, long applicationId, String key, String type) {
        ContentValues configurationValues = new ContentValues();
        configurationValues.put(ConfigurationTable.COL_APP_ID, applicationId);
        configurationValues.put(ConfigurationTable.COL_KEY, key);
        configurationValues.put(ConfigurationTable.COL_TYPE, type);
        return db.insert(ConfigurationTable.TABLE_NAME, CONFLICT_FAIL, configurationValues);

    }

    public static @NonNull
    Cursor getWrenchConfigurationByKey(SupportSQLiteDatabase db, String key) {
        Cursor query = db.query("SELECT * FROM " + ConfigurationTable.TABLE_NAME + " WHERE " + ConfigurationTable.COL_KEY + "=?", new Object[]{key});
        assertNotNull(query);
        return query;
    }

    public static long insertWrenchApplication(SupportSQLiteDatabase db, String applicationLabel, String packageName) {
        ContentValues applicationValues = new ContentValues();
        applicationValues.put(ApplicationTable.COL_APP_LABEL, applicationLabel);
        applicationValues.put(ApplicationTable.COL_PACK_NAME, packageName);
        return db.insert(ApplicationTable.TABLE_NAME, CONFLICT_FAIL, applicationValues);
    }
}
