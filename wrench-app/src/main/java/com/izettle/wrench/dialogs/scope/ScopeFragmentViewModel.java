package com.izettle.wrench.dialogs.scope;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.database.WrenchScopeDao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ScopeFragmentViewModel extends ViewModel {
    private long applicationId;
    private LiveData<WrenchScope> selectedScopeLiveData;
    private WrenchScope selectedScope;
    private WrenchScopeDao scopeDao;

    @SuppressWarnings("WeakerAccess")
    @Inject
    public ScopeFragmentViewModel(WrenchScopeDao scopeDao) {
        this.scopeDao = scopeDao;
    }

    public void init(long applicationId) {
        this.applicationId = applicationId;
    }

    LiveData<List<WrenchScope>> getScopes() {
        return scopeDao.getScopes(applicationId);
    }

    void selectScope(WrenchScope wrenchScope) {
        wrenchScope.setTimeStamp(new Date());
        scopeDao.update(wrenchScope);
    }

    @WorkerThread
    WrenchScope createScope(String scopeName) throws SQLiteException {
        WrenchScope wrenchScope = new WrenchScope();
        wrenchScope.setName(scopeName);
        wrenchScope.setApplicationId(applicationId);
        wrenchScope.setId(scopeDao.insert(wrenchScope));

        return wrenchScope;
    }

    @WorkerThread
    void removeScope(WrenchScope scope) {
        scopeDao.delete(scope);
    }

    LiveData<WrenchScope> getSelectedScopeLiveData() {
        if (selectedScopeLiveData == null) {
            selectedScopeLiveData = scopeDao.getSelectedScopeLiveData(applicationId);
        }
        return selectedScopeLiveData;
    }

    @Nullable
    WrenchScope getSelectedScope() {
        return selectedScope;
    }

    void setSelectedScope(WrenchScope scope) {
        this.selectedScope = scope;
    }

}
