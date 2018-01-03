package com.example.wrench.BoltLiveData;

import android.arch.lifecycle.LiveData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.WrenchProviderContract;

public abstract class BoltLiveData extends LiveData<Bolt> {
    private final String key;
    private final String type;
    private final Context context;
    private BoltContentObserver boltContentObserver;

    BoltLiveData(Context context, String key, @Bolt.BoltType String type) {
        this.context = context;
        this.key = key;
        this.type = type;
        boltContentObserver = new BoltContentObserver(new Handler());
    }

    public static BoltLiveData create(Context context, String key, String def) {
        return new StringBoltLiveData(context, key, def, Bolt.TYPE.STRING);
    }

    public static BoltLiveData create(Context context, String key, boolean def) {
        return new StringBoltLiveData(context, key, String.valueOf(def), Bolt.TYPE.BOOLEAN);
    }

    public static BoltLiveData create(Context context, String key, int def) {
        return new StringBoltLiveData(context, key, String.valueOf(def), Bolt.TYPE.INTEGER);
    }

    public static <T extends Enum> BoltLiveData create(Context context, String key, Class<T> enumClass, T def) {
        return new EnumBoltLiveData<>(context, key, enumClass, def);
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

    String getType() {
        return type;
    }

    Context getContext() {
        return context;
    }

    String getKey() {
        return key;
    }

    @Override
    protected void onActive() {
        ProviderInfo providerInfo = context.getPackageManager().resolveContentProvider(WrenchProviderContract.WRENCH_AUTHORITY, 0);
        if (providerInfo != null) {
            context.getContentResolver()
                    .registerContentObserver(WrenchProviderContract.boltUri(), true, boltContentObserver);

        }
        onChange();
    }

    @Override
    protected void onInactive() {
        context.getContentResolver().unregisterContentObserver(boltContentObserver);
    }

    private void onChange() {
        boltChanged(getBolt(context.getContentResolver(), key));
    }

    abstract void boltChanged(@Nullable Bolt bolt);

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
            BoltLiveData.this.onChange();
        }
    }
}