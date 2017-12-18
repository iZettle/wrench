package com.izettle.wrench.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.Cursor;

import com.izettle.wrench.database.tables.ConfigurationTable;
import com.izettle.wrench.database.tables.ConfigurationValueTable;

import java.util.List;

@Dao
public interface WrenchConfigurationDao {

    @Query("SELECT configuration.id, " +
            " configuration.configurationKey, " +
            " configuration.configurationType," +
            " configurationValue.value" +
            " FROM " + ConfigurationTable.TABLE_NAME +
            " INNER JOIN " + ConfigurationValueTable.TABLE_NAME + " ON configuration.id = configurationValue.configurationId " +
            " WHERE configuration.id = (:configurationId) AND configurationValue.scope = (:scopeId)")
    Cursor getBolt(long configurationId, long scopeId);

    @Query("SELECT configuration.id, " +
            " configuration.configurationKey, " +
            " configuration.configurationType," +
            " configurationValue.value" +
            " FROM " + ConfigurationTable.TABLE_NAME +
            " INNER JOIN " + ConfigurationValueTable.TABLE_NAME + " ON configuration.id = configurationValue.configurationId " +
            " WHERE configuration.configurationKey = (:configurationKey) AND configurationValue.scope = (:scopeId)")
    Cursor getBolt(String configurationKey, long scopeId);

    @Query("SELECT * " +
            " FROM " + ConfigurationTable.TABLE_NAME +
            " WHERE configuration.applicationId = (:applicationId) AND configuration.configurationKey = (:configurationKey)")
    WrenchConfiguration getWrenchConfiguration(long applicationId, String configurationKey);

    @Query("SELECT * FROM " + ConfigurationTable.TABLE_NAME + " WHERE " + ConfigurationTable.COL_ID + " = :configurationId")
    LiveData<WrenchConfiguration> getConfiguration(long configurationId);

    @Transaction
    @Query("SELECT * FROM " + ConfigurationTable.TABLE_NAME + " WHERE " + ConfigurationTable.COL_APP_ID + " = :applicationId")
    LiveData<List<WrenchConfigurationWithValues>> getApplicationConfigurations(long applicationId);

    @Transaction
    @Query("SELECT * FROM " + ConfigurationTable.TABLE_NAME + " WHERE " + ConfigurationTable.COL_APP_ID + " = :applicationId AND " + ConfigurationTable.COL_KEY + " LIKE :query")
    LiveData<List<WrenchConfigurationWithValues>> getApplicationConfigurations(long applicationId, String query);

    @Insert
    long insert(WrenchConfiguration wrenchConfiguration);

}
