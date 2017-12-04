package com.example.wrench;

import android.arch.lifecycle.LiveData;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.WrenchProviderContract;

public class BoltLiveData extends LiveData<Bolt> {
    private final String key;
    private final String type;
    private final String defValue;
    private final Context context;
    private BoltContentObserver boltContentObserver;

    BoltLiveData(Context context, String key, String defValue, @Bolt.BoltType String type) {
        this.context = context;
        this.key = key;
        this.type = type;
        this.defValue = defValue;
        boltContentObserver = new BoltContentObserver(new Handler());
    }

    public static BoltLiveData string(Context context, String key, String def) {
        return new BoltLiveData(context, key, def, Bolt.TYPE.STRING);
    }

    public static BoltLiveData bool(Context context, String key, boolean def) {
        return new BoltLiveData(context, key, String.valueOf(def), Bolt.TYPE.BOOLEAN);
    }

    public static BoltLiveData integer(Context context, String key, int def) {
        return new BoltLiveData(context, key, String.valueOf(def), Bolt.TYPE.INTEGER);
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

    @Override
    protected void onActive() {
        context.getContentResolver()
                .registerContentObserver(WrenchProviderContract.boltUri(), true, boltContentObserver);

        boltChanged();
    }

    @Override
    protected void onInactive() {
        context.getContentResolver().unregisterContentObserver(boltContentObserver);
    }

    private void boltChanged() {
        Bolt bolt = getBolt(context.getContentResolver(), key);

        if (bolt == null) {
            setValue(null);
            return;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), type, key, defValue);
            Uri uri = insertBolt(context.getContentResolver(), bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        }
        setValue(bolt);
    }

    class BoltContentObserver extends ContentObserver {
        BoltContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            BoltLiveData.this.boltChanged();
        }
    }
}