package com.izettle.wrench.dialogs.scope;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.izettle.wrench.database.WrenchDatabase;
import com.izettle.wrench.database.WrenchScope;

import java.util.Date;
import java.util.List;

public class ScopeFragmentViewModel extends AndroidViewModel {
    private final WrenchDatabase wrenchDatabase;
    private long applicationId;
    private LiveData<WrenchScope> selectedScopeLiveData;
    private WrenchScope selectedScope;

    @SuppressWarnings("WeakerAccess")
    public ScopeFragmentViewModel(Application application) {
        super(application);
        wrenchDatabase = WrenchDatabase.getDatabase(application);
    }

    public void init(long applicationId) {
        this.applicationId = applicationId;
    }

    LiveData<List<WrenchScope>> getScopes() {
        return wrenchDatabase.scopeDao().getScopes(applicationId);
    }

    void selectScope(WrenchScope wrenchScope) {
        wrenchScope.setTimeStamp(new Date());
        wrenchDatabase.scopeDao().update(wrenchScope);
    }

    @WorkerThread
    WrenchScope createScope(String scopeName) throws SQLiteException {
        WrenchScope wrenchScope = new WrenchScope();
        wrenchScope.setName(scopeName);
        wrenchScope.setApplicationId(applicationId);
        wrenchScope.setId(wrenchDatabase.scopeDao().insert(wrenchScope));

        return wrenchScope;
    }

    @WorkerThread
    void removeScope(WrenchScope scope) {
        wrenchDatabase.scopeDao().delete(scope);
    }

    LiveData<WrenchScope> getSelectedScopeLiveData() {
        if (selectedScopeLiveData == null) {
            selectedScopeLiveData = wrenchDatabase.scopeDao().getSelectedScopeLiveData(applicationId);
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
