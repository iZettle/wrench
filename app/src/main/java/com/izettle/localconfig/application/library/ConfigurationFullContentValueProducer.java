package com.izettle.localconfig.application.library;


import android.content.ContentValues;

import com.izettle.localconfiguration.util.IContentValueProducer;


public class ConfigurationFullContentValueProducer implements IContentValueProducer<ConfigurationFull> {
    @Override
    public ContentValues toContentValues(ConfigurationFull item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigurationFullCursorParser.Columns.KEY, item.key);
        contentValues.put(ConfigurationFullCursorParser.Columns.APPLICATION_ID, item.applicationId);
        contentValues.put(ConfigurationFullCursorParser.Columns.KEY, item.key);
        contentValues.put(ConfigurationFullCursorParser.Columns.VALUE, item.value);
        contentValues.put(ConfigurationFullCursorParser.Columns.TYPE, item.type);
        return contentValues;

    }
}
