package com.izettle.wrench.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.izettle.wrench.database.tables.PredefinedConfigurationValueTable;

import java.util.List;

@Dao
public interface WrenchPredefinedConfigurationValueDao {

    @Query("SELECT * FROM " + PredefinedConfigurationValueTable.TABLE_NAME + " WHERE " + PredefinedConfigurationValueTable.COL_CONFIG_ID + " = (:configurationId)")
    LiveData<List<WrenchPredefinedConfigurationValue>> getByConfigurationId(long configurationId);

    @Insert
    long insert(WrenchPredefinedConfigurationValue fullConfig);

}
