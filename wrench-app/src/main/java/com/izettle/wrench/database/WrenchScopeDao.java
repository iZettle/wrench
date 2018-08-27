package com.izettle.wrench.database;

import com.izettle.wrench.database.tables.ScopeTable;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WrenchScopeDao {

    @Query("SELECT * FROM " + ScopeTable.TABLE_NAME + " WHERE " + ScopeTable.COL_APP_ID + " = (:applicationId) AND " + ScopeTable.COL_NAME + " != '" + WrenchScope.SCOPE_DEFAULT + "'")
    LiveData<List<WrenchScope>> getScopes(long applicationId);

    @Insert
    long insert(WrenchScope scope);

    @Delete
    void delete(WrenchScope scope);

    @Query("SELECT * FROM " + ScopeTable.TABLE_NAME + " WHERE " + ScopeTable.COL_APP_ID + " = (:applicationId) ORDER BY " + ScopeTable.COL_SELECTED_TIMESTAMP + " DESC LIMIT 1")
    WrenchScope getSelectedScope(long applicationId);

    @Query("SELECT * FROM " + ScopeTable.TABLE_NAME + " WHERE " + ScopeTable.COL_APP_ID + " = (:applicationId) ORDER BY " + ScopeTable.COL_SELECTED_TIMESTAMP + " DESC LIMIT 1")
    LiveData<WrenchScope> getSelectedScopeLiveData(long applicationId);

    @Query("SELECT * FROM " + ScopeTable.TABLE_NAME + " WHERE " + ScopeTable.COL_APP_ID + " = (:applicationId) AND " + ScopeTable.COL_NAME + " = '" + WrenchScope.SCOPE_DEFAULT + "'")
    LiveData<WrenchScope> getDefaultScopeLiveData(long applicationId);

    @Query("SELECT * FROM " + ScopeTable.TABLE_NAME + " WHERE " + ScopeTable.COL_APP_ID + " = (:applicationId) AND " + ScopeTable.COL_NAME + " = '" + WrenchScope.SCOPE_DEFAULT + "'")
    WrenchScope getDefaultScope(long applicationId);

    @Update
    void update(WrenchScope scope);
}
