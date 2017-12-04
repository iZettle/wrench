package com.izettle.wrench.database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.izettle.wrench.database.tables.ApplicationTable;

@Dao
public interface WrenchApplicationDao {

    @Query("SELECT * FROM " + ApplicationTable.TABLE_NAME + " WHERE id = (:id)")
    LiveData<WrenchApplication> get(long id);

    @Query("SELECT * FROM " + ApplicationTable.TABLE_NAME)
    LivePagedListProvider<Integer, WrenchApplication> getApplications();


    @Query("SELECT * FROM " + ApplicationTable.TABLE_NAME + " WHERE packageName IN (:packageName)")
    WrenchApplication loadByPackageName(String packageName);

    @Insert
    long insert(WrenchApplication application);

    @Delete
    void delete(WrenchApplication application);
}
