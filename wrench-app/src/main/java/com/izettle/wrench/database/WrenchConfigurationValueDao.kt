package com.izettle.wrench.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.izettle.wrench.database.tables.ConfigurationValueTable

@Dao
interface WrenchConfigurationValueDao {

    @Query("SELECT * FROM " + ConfigurationValueTable.TABLE_NAME +
            " WHERE " + ConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId)")
    fun getConfigurationValue(configurationId: Long): LiveData<List<WrenchConfigurationValue>>

    @Query("SELECT * FROM " + ConfigurationValueTable.TABLE_NAME +
            " WHERE " + ConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId) AND " + ConfigurationValueTable.COL_SCOPE + " = (:scopeId)")
    fun getConfigurationValue(configurationId: Long, scopeId: Long): LiveData<WrenchConfigurationValue>

    @Query("UPDATE " + ConfigurationValueTable.TABLE_NAME +
            " SET " + ConfigurationValueTable.COL_VALUE + " = (:value)" +
            " WHERE " + ConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId) AND " + ConfigurationValueTable.COL_SCOPE + " = (:scopeId) ")
    fun updateConfigurationValueSync(configurationId: Long, scopeId: Long, value: String): Int

    @Query("UPDATE " + ConfigurationValueTable.TABLE_NAME +
            " SET " + ConfigurationValueTable.COL_VALUE + " = (:value)" +
            " WHERE " + ConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId) AND " + ConfigurationValueTable.COL_SCOPE + " = (:scopeId) ")
    suspend fun updateConfigurationValue(configurationId: Long, scopeId: Long, value: String): Int

    @Insert
    fun insertSync(wrenchConfigurationValue: WrenchConfigurationValue): Long

    @Insert
    suspend fun insert(wrenchConfigurationValue: WrenchConfigurationValue): Long

    @Update
    fun update(wrenchConfigurationValue: WrenchConfigurationValue): Int

    @Delete
    suspend fun delete(selectedConfigurationValue: WrenchConfigurationValue)
}
