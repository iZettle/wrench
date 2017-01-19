package com.izettle.localconfigurationservice;


import android.content.ContentValues;


public class ConfigurationContentValueProducer implements IContentValueProducer<Configuration> {
    @Override
    public ContentValues toContentValues(Configuration item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigurationCursorParser.Columns.KEY, item.key);
        contentValues.put(ConfigurationCursorParser.Columns.VALUE, item.value);
        contentValues.put(ConfigurationCursorParser.Columns.TYPE, item.type);
        return contentValues;

    }
}
