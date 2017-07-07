package com.izettle.wrench.preferences;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.Nut;
import com.izettle.wrench.core.WrenchProviderContract;

public class WrenchPreferences {
    private final ContentResolver contentResolver;

    public WrenchPreferences(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    private static void insertNut(ContentResolver contentResolver, Nut nut) {
        contentResolver.insert(WrenchProviderContract.nutUri(), nut.toContentValues());
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

    private static Uri insertBolt(ContentResolver contentResolver, Bolt bolt) {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues());
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> type, T defValue) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.id == 0) {
            bolt.key = key;
            bolt.type = Enum.class.getName();
            bolt.value = defValue.toString();
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.id = Long.parseLong(uri.getLastPathSegment());

            for (T enumConstant : type.getEnumConstants()) {
                Nut nut = new Nut();
                nut.configurationId = bolt.id;
                nut.value = enumConstant.toString();
                insertNut(contentResolver, nut);
            }
        }

        return Enum.valueOf(type, bolt.value);
    }

    public String getString(String key, String defValue) {

        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.id == 0) {
            bolt.key = key;
            bolt.type = String.class.getName();
            bolt.value = defValue;
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.id = Long.parseLong(uri.getLastPathSegment());
        }

        return bolt.value;
    }

    public boolean getBoolean(String key, boolean defValue) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.id == 0) {
            bolt.key = key;
            bolt.type = Boolean.class.getName();
            bolt.value = String.valueOf(defValue);
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.id = Long.parseLong(uri.getLastPathSegment());
        }

        return Boolean.valueOf(bolt.value);
    }

    public int getInt(String key, int defValue) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.id == 0) {
            bolt.key = key;
            bolt.type = Integer.class.getName();
            bolt.value = String.valueOf(defValue);
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.id = Long.parseLong(uri.getLastPathSegment());
        }

        return Integer.valueOf(bolt.value);
    }
}
