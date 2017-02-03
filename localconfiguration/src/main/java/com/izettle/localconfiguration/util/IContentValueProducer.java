package com.izettle.localconfiguration.util;


import android.content.ContentValues;

public interface IContentValueProducer<T> {
    ContentValues toContentValues(T item);
}
