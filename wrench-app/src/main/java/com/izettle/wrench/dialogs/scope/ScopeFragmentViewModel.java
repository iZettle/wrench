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

    public ScopeFragmentViewModel(Application application) {
        super(application);
        wrenchDatabase = WrenchDatabase.getDatabase(application);
    }

    public void init(long applicationId) {
        this.applicationId = applicationId;
    }

    public LiveData<List<WrenchScope>> getScopes() {
        return wrenchDatabase.scopeDao().getScopes(applicationId);
    }

    public void selectScope(WrenchScope wrenchScope) {
        wrenchScope.setTimeStamp(new Date());
        wrenchDatabase.scopeDao().update(wrenchScope);
    }

    @WorkerThread
    public WrenchScope createScope(String scopeName) throws SQLiteException {
        WrenchScope wrenchScope = new WrenchScope();
        wrenchScope.setName(scopeName);
        wrenchScope.setApplicationId(applicationId);
        wrenchScope.setId(wrenchDatabase.scopeDao().insert(wrenchScope));

        return wrenchScope;
    }

    @WorkerThread
    public void removeScope(WrenchScope scope) {
        wrenchDatabase.scopeDao().delete(scope);
    }

    public LiveData<WrenchScope> getSelectedScopeLiveData() {
        if (selectedScopeLiveData == null) {
            selectedScopeLiveData = wrenchDatabase.scopeDao().getSelectedScopeLiveData(applicationId);
        }
        return selectedScopeLiveData;
    }

    @Nullable
    public WrenchScope getSelectedScope() {
        return selectedScope;
    }

    public void setSelectedScope(WrenchScope scope) {
        this.selectedScope = scope;
    }

}
