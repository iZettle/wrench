package com.izettle.localconfiguration;


import android.content.Context;

import java.util.Collections;
import java.util.Map;

@SuppressWarnings("unused")
public class LocalConfiguration {

    public LocalConfiguration(Context context) {
    }

    public Map<String, ?> getAll() {
        return Collections.emptyMap();
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
