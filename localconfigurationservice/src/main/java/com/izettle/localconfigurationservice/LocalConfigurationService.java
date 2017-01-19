package com.izettle.localconfigurationservice;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * am startservice -n com.izettle.localconfig.sampleapplication.debug/com.izettle.localconfig.sampleapplication.ConfigurationService --ei key 1234
 * am startservice -n com.izettle.localconfig.sampleapplication.debug/com.izettle.localconfig.sampleapplication.ConfigurationService --ez key true|false
 * am startservice -n com.izettle.localconfig.sampleapplication.debug/com.izettle.localconfig.sampleapplication.ConfigurationService --es key "my string"
 */
public abstract class LocalConfigurationService extends IntentService {

    public LocalConfigurationService() {
        super("LocalConfigurationService");
    }

    private static void updateInteger(ContentResolver contentResolver, String key, int value) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            Log.w("LocalConfigurationServi", "provider not found");
            return;
        }

        if (configuration._id == 0) {
            configuration.key = key;
            configuration.value = String.valueOf(value);
            configuration.type = Integer.class.getName();
            insertConfiguration(contentResolver, configuration);
        } else {
            configuration.value = String.valueOf(value);
            updateConfiguration(contentResolver, configuration);
        }
    }

    /**
     * @return The configuration. Or null if provider doesn't exist. Empty Configuration if key did not exist in the provider.
     */
    @Nullable
    private static Configuration getConfiguration(ContentResolver contentResolver, String key) {
        ConfigurationCursorParser configurationCursorParser = new ConfigurationCursorParser();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ConfigProviderHelper.configurationUri(), configurationCursorParser.PROJECTION, ConfigurationCursorParser.Columns.KEY + " = ?", new String[]{key}, null);
            if (cursor == null) {
                return null;
            }

            if (cursor.moveToFirst()) {
                return configurationCursorParser.populateFromCursor(new Configuration(), cursor);
            }


        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return new Configuration();
    }

    private static Uri insertConfiguration(ContentResolver contentResolver, Configuration configuration) {
        return contentResolver.insert(ConfigProviderHelper.configurationUri(), new ConfigurationContentValueProducer().toContentValues(configuration));
    }

    private static int updateConfiguration(ContentResolver contentResolver, Configuration configuration) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigurationCursorParser.Columns.VALUE, configuration.value);
        return contentResolver.update(ConfigProviderHelper.configurationUri(configuration._id), contentValues,
                ConfigurationCursorParser.Columns.KEY + " = ? AND " + ConfigurationCursorParser.Columns.TYPE + " = ?", new String[]{configuration.key, configuration.type});
    }

    @Override
    protected final void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                if (value instanceof Integer) {
                    updateInteger(getContentResolver(), key, (int) value);
                } else if (value instanceof String) {
                    updateString(getContentResolver(), key, (String) value);
                } else if (value instanceof Boolean) {
                    updateBoolean(getContentResolver(), key, (boolean) value);
                }

            }
        }
    }

    private void updateBoolean(ContentResolver contentResolver, String key, boolean value) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            Log.w("LocalConfigurationServi", "provider not found");
            return;
        }

        if (configuration._id == 0) {
            configuration.key = key;
            configuration.value = String.valueOf(value);
            configuration.type = Boolean.class.getName();
            insertConfiguration(contentResolver, configuration);
        } else {
            configuration.value = String.valueOf(value);
            updateConfiguration(contentResolver, configuration);
        }
    }

    private void updateString(ContentResolver contentResolver, String key, String value) {
        Configuration configuration = getConfiguration(contentResolver, key);
        if (configuration == null) {
            Log.w("LocalConfigurationServi", "provider not found");
            return;
        }

        if (configuration._id == 0) {
            configuration.key = key;
            configuration.value = value;
            configuration.type = String.class.getName();
            insertConfiguration(contentResolver, configuration);
        } else {
            configuration.value = value;
            updateConfiguration(contentResolver, configuration);
        }
    }
}
