package se.eelde.localconfiguration.library;


import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.Map;

import se.eelde.localconfiguration.library.util.ConfigurationContentValueProducer;
import se.eelde.localconfiguration.library.util.ConfigurationCursorParser;

public class LocalConfiguration {
    private final ContentResolver contentResolver;

    public LocalConfiguration(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    public static String getString(ContentResolver contentResolver, String key, String defValue) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            configuration = new Configuration();
            configuration.key = key;
            configuration.value = defValue;
            configuration.type = String.class.getName();
            insertConfiguration(contentResolver, configuration);
        }
        return configuration.value;
    }

    public static boolean getBoolean(ContentResolver contentResolver, String key, boolean defValue) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            configuration = new Configuration();
            configuration.key = key;
            configuration.value = String.valueOf(defValue);
            configuration.type = Boolean.class.getName();
            insertConfiguration(contentResolver, configuration);
        }
        return Boolean.valueOf(configuration.value);
    }

    public static int getInt(ContentResolver contentResolver, String key, int defValue) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            configuration = new Configuration();
            configuration.key = key;
            configuration.value = String.valueOf(defValue);
            configuration.type = Integer.class.getName();
            insertConfiguration(contentResolver, configuration);
        }
        return Integer.valueOf(configuration.value);
    }

    public static Map<String, ?> getAll(ContentResolver contentResolver) {
        Map<String, Object> values = new ArrayMap<>();
        ArrayList<Configuration> configurations = getConfigurations(contentResolver);
        for (Configuration configuration : configurations) {
            values.put(configuration.key, configuration.value);
        }
        return values;
    }

    public static boolean exists(PackageManager packageManager) {
        ProviderInfo providerInfo = packageManager.resolveContentProvider(ConfigProviderHelper.AUTHORITY, 0);
        return providerInfo != null;
    }

    private static void insertConfiguration(ContentResolver contentResolver, Configuration configuration) {
        contentResolver.insert(ConfigProviderHelper.configurationUri(), new ConfigurationContentValueProducer().toContentValues(configuration));
    }

    private static ArrayList<Configuration> getConfigurations(ContentResolver contentResolver) {
        ConfigurationCursorParser configurationCursorParser = new ConfigurationCursorParser();
        ArrayList<Configuration> configurations = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ConfigProviderHelper.configurationUri(), configurationCursorParser.PROJECTION, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                configurations.add(configurationCursorParser.populateFromCursor(new Configuration(), cursor));
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return configurations;
    }

    @Nullable
    private static Configuration getConfiguration(ContentResolver contentResolver, String key) {
        ConfigurationCursorParser configurationCursorParser = new ConfigurationCursorParser();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ConfigProviderHelper.configurationUri(), configurationCursorParser.PROJECTION, ConfigurationCursorParser.Columns.KEY + " = ?", new String[]{key}, null);
            if (cursor != null && cursor.moveToFirst()) {
                return configurationCursorParser.populateFromCursor(new Configuration(), cursor);
            }

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public String getString(String key, String defValue) {
        return getString(contentResolver, key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getBoolean(contentResolver, key, defValue);
    }

    public int getInt(String key, int defValue) {
        return getInt(contentResolver, key, defValue);
    }

}
