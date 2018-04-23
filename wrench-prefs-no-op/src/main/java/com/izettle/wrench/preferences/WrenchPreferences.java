package com.izettle.wrench.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class WrenchPreferences {
    public WrenchPreferences(@NonNull Context context) {
    }

    @NonNull
    public <T extends Enum<T>> T getEnum(@NonNull String key, @NonNull Class<T> type, @NonNull T defValue) {
        return defValue;
    }

    @Nullable
    public String getString(@NonNull String key, @Nullable String defValue) {
        return defValue;
    }

    public boolean getBoolean(@NonNull String key, boolean defValue) {
        return defValue;
    }

    public int getInt(@NonNull String key, int defValue) {
        return defValue;
    }
}
