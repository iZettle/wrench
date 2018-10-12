package com.izettle.wrench.service;

public interface Provider {

    Object getValue(Class<?> type, final String key, final Object defaultValue) throws TypeNotSupportedException;
}
