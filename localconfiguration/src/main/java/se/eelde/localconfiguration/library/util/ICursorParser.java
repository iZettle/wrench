package se.eelde.localconfiguration.library.util;

import android.database.Cursor;

public interface ICursorParser<T> {
    T populateFromCursor(T item, Cursor cursor);
}
