package com.izettle.wrench.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;

import com.izettle.wrench.database.tables.ConfigurationTable;
import com.izettle.wrench.database.tables.ConfigurationValueTable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = ConfigurationValueTable.TABLE_NAME,
        indices = {@Index(value = {ConfigurationValueTable.COL_CONFIG_ID, ConfigurationValueTable.COL_VALUE, ConfigurationValueTable.COL_SCOPE}, unique = true)},
        foreignKeys = @ForeignKey(entity = WrenchConfiguration.class,
                parentColumns = ConfigurationTable.COL_ID,
                childColumns = ConfigurationValueTable.COL_CONFIG_ID,
                onDelete = CASCADE))
public class WrenchConfigurationValue {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConfigurationValueTable.COL_ID)
    private long id;

    @ColumnInfo(name = ConfigurationValueTable.COL_CONFIG_ID)
    private long configurationId;

    @ColumnInfo(name = ConfigurationValueTable.COL_VALUE)
    private String value;

    @ColumnInfo(name = ConfigurationValueTable.COL_SCOPE)
    private long scope;


    public static WrenchConfigurationValue fromContentValues(ContentValues values) {
        WrenchConfigurationValue wrenchConfigurationValue = new WrenchConfigurationValue();
        if (values.containsKey(ConfigurationValueTable.COL_ID)) {
            wrenchConfigurationValue.id = values.getAsLong(ConfigurationValueTable.COL_ID);
        }
        wrenchConfigurationValue.configurationId = values.getAsLong(ConfigurationValueTable.COL_CONFIG_ID);
        wrenchConfigurationValue.value = values.getAsString(ConfigurationValueTable.COL_VALUE);
        wrenchConfigurationValue.scope = values.getAsLong(ConfigurationValueTable.COL_SCOPE);

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

    public long getScope() {
        return scope;
    }

    public void setScope(long scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return value;
    }
}
