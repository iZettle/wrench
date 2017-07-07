package com.izettle.wrench.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.WrenchProviderContract;

public final class WrenchService extends IntentService {

    private static final String TAG = WrenchService.class.getSimpleName();

    public WrenchService() {
        super("WrenchService");
    }

    private static void updateInteger(ContentResolver contentResolver, String key, int value) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            Log.w(TAG, "provider not found");
            return;
        }

        if (bolt.id == 0) {
            bolt.key = key;
            bolt.type = Integer.class.getName();
            bolt.value = String.valueOf(value);
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.id = Long.parseLong(uri.getLastPathSegment());
        } else {
            bolt.value = String.valueOf(value);
            updateBolt(contentResolver, bolt);
        }
    }

    private static void updateBoolean(ContentResolver contentResolver, String key, boolean value) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            Log.w(TAG, "provider not found");
            return;
        }

        if (bolt.id == 0) {
            bolt.key = key;
            bolt.type = Boolean.class.getName();
            bolt.value = String.valueOf(value);
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.id = Long.parseLong(uri.getLastPathSegment());
        } else {
            bolt.value = String.valueOf(value);
            updateBolt(contentResolver, bolt);
        }
    }

    private static void updateString(ContentResolver contentResolver, String key, String value) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            Log.w(TAG, "provider not found");
            return;
        }

        if (bolt.id == 0) {
            bolt.key = key;
            bolt.type = String.class.getName();
            bolt.value = value;
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.id = Long.parseLong(uri.getLastPathSegment());
        } else {
            bolt.value = value;
            updateBolt(contentResolver, bolt);
        }
    }

    private static Uri insertBolt(ContentResolver contentResolver, Bolt bolt) {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues());
    }

    private static void updateBolt(ContentResolver contentResolver, Bolt bolt) {
        contentResolver.update(WrenchProviderContract.boltUri(bolt.id),
                bolt.toContentValues(),
                null,
                null);
    }

    @Nullable
    private static Bolt getBolt(ContentResolver contentResolver, String key) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(WrenchProviderContract.boltUri(key),
                    null,
                    null,
                    null,
                    null);
            if (cursor == null) {
                return null;
            }

            if (cursor.moveToFirst()) {
                return Bolt.fromCursor(cursor);
            }


        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return new Bolt();
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
}
