package com.izettle.wrench.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.izettle.wrench.database.tables.ScopeTable;

import java.util.List;

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

    @Query("SELECT * FROM " + ScopeTable.TABLE_NAME + " WHERE " + ScopeTable.COL_ID + " = (:scopeId)")
    LiveData<WrenchScope> getScope(long scopeId);

    @Update
    void update(WrenchScope scope);
}
