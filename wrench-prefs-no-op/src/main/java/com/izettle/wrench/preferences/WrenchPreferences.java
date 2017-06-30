package com.izettle.wrench.preferences;


import android.content.Context;

@SuppressWarnings("unused")
public final class WrenchPreferences {

    public WrenchPreferences(Context context) {
    }

    public final <T extends Enum<T>> T getEnum(String key, Class<T> type, T defValue) {
        return defValue;
    }

    public final String getString(String key, String defValue) {
        return defValue;
    }

    public final boolean getBoolean(String key, boolean defValue) {
        return defValue;
    }

    public final int getInt(String key, int defValue) {
        return defValue;
    }
}
