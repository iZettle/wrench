package com.izettle.wrench.database;

import com.izettle.wrench.database.tables.PredefinedConfigurationValueTable;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WrenchPredefinedConfigurationValueDao {

    @Query("SELECT * FROM " + PredefinedConfigurationValueTable.TABLE_NAME + " WHERE " + PredefinedConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId)")
    LiveData<List<WrenchPredefinedConfigurationValue>> getByConfigurationId(long configurationId);

    @Insert
    long insert(WrenchPredefinedConfigurationValue fullConfig);

}
