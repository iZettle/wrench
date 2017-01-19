package com.izettle.localconfigurationservice;

import android.database.Cursor;

public interface ICursorParser<T> {
    T populateFromCursor(T item, Cursor cursor);
}
