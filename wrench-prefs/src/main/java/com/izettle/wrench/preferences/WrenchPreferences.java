package com.izettle.wrench.preferences;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.Nut;
import com.izettle.wrench.core.WrenchProviderContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WrenchPreferences {
    private final ContentResolver contentResolver;

    public WrenchPreferences(@NonNull Context context) {
        this.contentResolver = context.getContentResolver();
    }

    private static void insertNut(@NonNull ContentResolver contentResolver, @NonNull Nut nut) {
        contentResolver.insert(WrenchProviderContract.nutUri(), nut.toContentValues());
    }

    @Nullable
    private static Bolt getBolt(@NonNull ContentResolver contentResolver, @NonNull @Bolt.BoltType String boltType, @NonNull String key) {
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
        return new Bolt(0L, boltType, key, null);
    }

    private static Uri insertBolt(@NonNull ContentResolver contentResolver, @NonNull Bolt bolt) {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues());
    }

    @NonNull
    public <T extends Enum<T>> T getEnum(@NonNull String key, @NonNull Class<T> type, @NonNull T defValue) {
        Bolt bolt = getBolt(contentResolver, Bolt.TYPE.ENUM, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), bolt.getType(), bolt.getKey(), defValue.toString());
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));

            for (T enumConstant : type.getEnumConstants()) {
                insertNut(contentResolver, new Nut(bolt.getId(), enumConstant.toString()));
            }
        }

        if (bolt.getValue() == null) {
            throw new IllegalStateException("bolt value cannot be null for enum");
        }
        return Enum.valueOf(type, bolt.getValue());
    }

    @Nullable
    public String getString(@NonNull String key, @Nullable String defValue) {

        Bolt bolt = getBolt(contentResolver, Bolt.TYPE.STRING, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), bolt.getType(), bolt.getKey(), defValue);
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        }

        return bolt.getValue();
    }

    public boolean getBoolean(@NonNull String key, boolean defValue) {
        Bolt bolt = getBolt(contentResolver, Bolt.TYPE.BOOLEAN, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), bolt.getType(), bolt.getKey(), String.valueOf(defValue));
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        }

        return Boolean.valueOf(bolt.getValue());
    }

    public int getInt(@NonNull String key, int defValue) {
        Bolt bolt = getBolt(contentResolver, Bolt.TYPE.INTEGER, key);
        if (bolt == null) {
            return defValue;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), bolt.getType(), bolt.getKey(), String.valueOf(defValue));
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        }

        return Integer.valueOf(bolt.getValue());
    }
}