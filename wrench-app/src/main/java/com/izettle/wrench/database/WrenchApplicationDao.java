package com.izettle.wrench.database;

import com.izettle.wrench.database.tables.ApplicationTable;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WrenchApplicationDao {

    @Query("SELECT * FROM " + ApplicationTable.TABLE_NAME + " WHERE id = (:id)")
    LiveData<WrenchApplication> get(long id);

    @Query("SELECT * FROM " + ApplicationTable.TABLE_NAME)
    DataSource.Factory<Integer, WrenchApplication> getApplications();


    @Query("SELECT * FROM " + ApplicationTable.TABLE_NAME + " WHERE packageName IN (:packageName)")
    WrenchApplication loadByPackageName(String packageName);

    @Insert
    long insert(WrenchApplication application);

    @Delete
    void delete(WrenchApplication application);
}
