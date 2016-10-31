package se.izettle.localconfiguration.library.util;


import android.content.ContentValues;

public interface IContentValueProducer<T> {
    ContentValues toContentValues(T item);
}
