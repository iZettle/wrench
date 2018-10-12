package com.izettle.wrench.service;

public class WrenchPreferenceProvider implements Provider {

    @Override
    public Object getValue(final Class<?> type, final String key, final Object defaultValue) throws TypeNotSupportedException{
        return defaultValue;
    }
}
