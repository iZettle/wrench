package com.izettle.wrench.service.storage;

import android.content.Context;

import com.izettle.wrench.preferences.WrenchPreferences;
import com.izettle.wrench.service.Storage;
import com.izettle.wrench.service.internal.Factory;

public class StorageFactory implements Factory<Storage> {

    private final Context mContext;

    public StorageFactory(final Context context) {
        mContext = context;
    }

    @Override
    public Storage create() {
        final WrenchPreferences preferences = new WrenchPreferences(mContext);
        return new WrenchPreferenceStorage(preferences);
    }
}
