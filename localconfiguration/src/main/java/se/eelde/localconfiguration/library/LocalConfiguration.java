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

    public static boolean exists(PackageManager packageManager) {
        ProviderInfo providerInfo = packageManager.resolveContentProvider(ConfigProviderHelper.AUTHORITY, 0);
        return providerInfo != null;
    }

    public static void insertConfiguration(ContentResolver contentResolver, Configuration configuration) {
        contentResolver.insert(ConfigProviderHelper.configurationUri(), Configuration.toContentValues(configuration));
    }

    public static void updateConfiguration(ContentResolver contentResolver, Configuration configuration) {
        ContentValues contentValues = Configuration.toContentValues(configuration);
        contentResolver.update(ConfigProviderHelper.configurationUri(configuration._id), contentValues, null, null);
    }

    @Nullable
    public static Configuration getConfiguration(ContentResolver contentResolver, long id) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ConfigProviderHelper.configurationUri(id), Configuration.PROJECTION, null, null, null);
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

    public static List<Configuration> getConfigurations(ContentResolver contentResolver, long id) {
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
