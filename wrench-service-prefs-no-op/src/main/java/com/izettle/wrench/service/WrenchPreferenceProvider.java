package com.izettle.wrench.service;

import com.izettle.wrench.preferences.WrenchPreferences;

public class WrenchPreferenceProvider implements Provider {

    public WrenchPreferenceProvider(final WrenchPreferences wrenchPreferences) {
    }

    @Override
    public Object getValue(final Class<?> type, final String key, final Object defaultValue) throws TypeNotSupportedException {
        return defaultValue;
    }
}
