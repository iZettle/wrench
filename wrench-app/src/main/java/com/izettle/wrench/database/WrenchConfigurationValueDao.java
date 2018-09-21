package com.izettle.wrench.database;

import com.izettle.wrench.database.tables.ConfigurationValueTable;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WrenchConfigurationValueDao {

    @Query("SELECT * FROM " + ConfigurationValueTable.TABLE_NAME +
            " WHERE " + ConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId)")
    LiveData<List<WrenchConfigurationValue>> getConfigurationValue(long configurationId);

    @Query("SELECT * FROM " + ConfigurationValueTable.TABLE_NAME +
            " WHERE " + ConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId) AND " + ConfigurationValueTable.COL_SCOPE + " = (:scopeId)")
    LiveData<WrenchConfigurationValue> getConfigurationValue(long configurationId, long scopeId);

    @Query("UPDATE " + ConfigurationValueTable.TABLE_NAME +
            " SET " + ConfigurationValueTable.COL_VALUE + " = (:value)" +
            " WHERE " + ConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId) AND " + ConfigurationValueTable.COL_SCOPE + " = (:scopeId) ")
    int updateConfigurationValue(long configurationId, long scopeId, String value);

    @Insert
    long insert(WrenchConfigurationValue wrenchConfigurationValue);

    @Update
    int update(WrenchConfigurationValue wrenchConfigurationValue);

    @Delete
    void delete(WrenchConfigurationValue selectedConfigurationValue);
}
