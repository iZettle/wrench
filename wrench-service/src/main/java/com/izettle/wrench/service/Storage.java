package com.izettle.wrench.service;

public interface Storage {

    Object getValue(Class<?> type, final String key, final Object defaultValue);
}
