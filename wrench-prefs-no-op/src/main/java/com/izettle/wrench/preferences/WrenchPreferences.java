package com.izettle.wrench.preferences;


import android.content.Context;

@SuppressWarnings("unused")
public class WrenchPreferences {

    public WrenchPreferences(Context context) {
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> type, T defValue) {
        return defValue;
    }

    public String getString(String key, String defValue) {
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return defValue;
    }

    public int getInt(String key, int defValue) {
        return defValue;
    }
}
