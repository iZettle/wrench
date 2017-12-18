package com.izettle.wrench.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Relation;

import com.izettle.wrench.database.tables.ConfigurationTable;

import java.util.List;

public class WrenchConfigurationWithValues {

    private long id;

    private long applicationId;

    @ColumnInfo(name = ConfigurationTable.COL_KEY)
    private String key;

    @ColumnInfo(name = ConfigurationTable.COL_TYPE)
    private String type;

    @Relation(parentColumn = "id", entityColumn = "configurationId", entity = WrenchConfigurationValue.class)
    private List<WrenchConfigurationValue> configurationValues;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<WrenchConfigurationValue> getConfigurationValues() {
        return configurationValues;
    }

    void setConfigurationValues(List<WrenchConfigurationValue> configurationValues) {
        this.configurationValues = configurationValues;
    }
}
