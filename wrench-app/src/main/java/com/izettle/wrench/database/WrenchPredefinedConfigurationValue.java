package com.izettle.wrench.database;

import android.content.ContentValues;

import com.izettle.wrench.database.tables.ConfigurationTable;
import com.izettle.wrench.database.tables.PredefinedConfigurationValueTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = PredefinedConfigurationValueTable.TABLE_NAME,
        indices = {@Index(value = {PredefinedConfigurationValueTable.COL_CONFIG_ID})},
        foreignKeys = @ForeignKey(entity = WrenchConfiguration.class,
                parentColumns = ConfigurationTable.COL_ID,
                childColumns = PredefinedConfigurationValueTable.COL_CONFIG_ID,
                onDelete = CASCADE))
public class WrenchPredefinedConfigurationValue {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PredefinedConfigurationValueTable.COL_ID)
    private long id;

    @ColumnInfo(name = PredefinedConfigurationValueTable.COL_CONFIG_ID)
    private long configurationId;

    @ColumnInfo(name = PredefinedConfigurationValueTable.COL_VALUE)
    private String value;

    public static WrenchPredefinedConfigurationValue fromContentValues(ContentValues values) {
        WrenchPredefinedConfigurationValue wrenchConfigurationValue = new WrenchPredefinedConfigurationValue();
        if (values.containsKey(PredefinedConfigurationValueTable.COL_ID)) {
            wrenchConfigurationValue.id = values.getAsLong(PredefinedConfigurationValueTable.COL_ID);
        }
        wrenchConfigurationValue.configurationId = values.getAsLong(PredefinedConfigurationValueTable.COL_CONFIG_ID);
        wrenchConfigurationValue.value = values.getAsString(PredefinedConfigurationValueTable.COL_VALUE);

        return wrenchConfigurationValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(long configurationId) {
        this.configurationId = configurationId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
