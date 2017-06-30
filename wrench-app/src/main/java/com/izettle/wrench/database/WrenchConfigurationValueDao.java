package com.izettle.wrench.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.izettle.wrench.database.tables.ConfigurationValueTable;

import java.util.List;

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

    @Query("UPDATE " + ConfigurationValueTable.TABLE_NAME +
            " SET " + ConfigurationValueTable.COL_VALUE + " = (:value)" +
            " WHERE " + ConfigurationValueTable.COL_ID + " = (:id)")
    void setConfigurationValue(long id, String value);

    @Delete
    void delete(WrenchConfigurationValue selectedConfigurationValue);
}
