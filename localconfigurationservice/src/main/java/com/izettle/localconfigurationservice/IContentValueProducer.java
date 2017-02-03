package com.izettle.localconfigurationservice;


import android.content.ContentValues;

public interface IContentValueProducer<T> {
    ContentValues toContentValues(T item);
}
