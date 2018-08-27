package com.izettle.wrench.database;

import com.izettle.wrench.database.tables.ConfigurationTable;
import com.izettle.wrench.database.tables.ConfigurationValueTable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


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

    @Nullable
    @ColumnInfo(name = ConfigurationValueTable.COL_VALUE)
    private String value;

    @ColumnInfo(name = ConfigurationValueTable.COL_SCOPE)
    private long scope;

    public WrenchConfigurationValue(long id, long configurationId, @Nullable String value, long scope) {
        this.id = id;
        this.configurationId = configurationId;
        this.value = value;
        this.scope = scope;
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
