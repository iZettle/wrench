package com.izettle.localconfiguration.util;


import android.content.ContentValues;

import com.izettle.localconfiguration.ConfigurationValue;


public class ConfigurationValueContentValueProducer implements IContentValueProducer<ConfigurationValue> {
    @Override
    public ContentValues toContentValues(ConfigurationValue item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigurationValueCursorParser.Columns.CONFIGURATION_ID, item.configurationId);
        contentValues.put(ConfigurationValueCursorParser.Columns.VALUE, item.value);
        return contentValues;

    }
}
