package se.eelde.localconfiguration.library;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocalConfiguration {
    private LocalConfiguration() {
    }

    public static String getString(ContentResolver contentResolver, String key) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            configuration = new Configuration();
            configuration.key = key;
            configuration.value = "";
            configuration.type = "string";
            insertConfiguration(contentResolver, configuration);
        }
        return configuration.value;
    }

    public static boolean getBoolean(ContentResolver contentResolver, String key) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            configuration = new Configuration();
            configuration.key = key;
            configuration.value = String.valueOf(false);
            configuration.type = "boolean";
            insertConfiguration(contentResolver, configuration);
        }
        return Boolean.valueOf(configuration.value);
    }

    public static int getInt(ContentResolver contentResolver, String key) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            configuration = new Configuration();
            configuration.key = key;
            configuration.value = String.valueOf(0);
            configuration.type = "int";
            insertConfiguration(contentResolver, configuration);
        }
        return Integer.valueOf(configuration.value);
    }

    public static boolean exists(PackageManager packageManager) {
        ProviderInfo providerInfo = packageManager.resolveContentProvider(ConfigProviderHelper.AUTHORITY, 0);
        return providerInfo != null;
    }

    private static void insertConfiguration(ContentResolver contentResolver, Configuration configuration) {
        contentResolver.insert(ConfigProviderHelper.configurationUri(), Configuration.toContentValues(configuration));
    }

    private static void updateConfiguration(ContentResolver contentResolver, Configuration configuration) {
        ContentValues contentValues = Configuration.toContentValues(configuration);
        contentResolver.update(ConfigProviderHelper.configurationUri(configuration._id), contentValues, null, null);
    }

    @Nullable
    private static Configuration getConfiguration(ContentResolver contentResolver, String key) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ConfigProviderHelper.configurationUri(), Configuration.PROJECTION, Configuration.Columns.KEY + " = ?", new String[]{key}, null);
            if (cursor != null && cursor.moveToFirst()) {
                return Configuration.configurationFromCursor(cursor);
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    private static List<Configuration> getConfigurations(ContentResolver contentResolver, long id) {
        ArrayList<Configuration> configurations = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ConfigProviderHelper.configurationUri(id), Configuration.PROJECTION, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    configurations.add(Configuration.configurationFromCursor(cursor));
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return configurations;
    }
}
