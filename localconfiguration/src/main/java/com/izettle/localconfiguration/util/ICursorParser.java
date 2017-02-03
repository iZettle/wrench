package com.izettle.localconfiguration.util;

import android.database.Cursor;

public interface ICursorParser<T> {
    T populateFromCursor(T item, Cursor cursor);
}
