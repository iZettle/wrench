package com.izettle.wrench;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchDatabase;
import com.izettle.wrench.database.WrenchScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ConfigurationViewModel extends AndroidViewModel {
    private final WrenchDatabase wrenchDatabase;
    final private Map<Long, LiveData<List<WrenchConfigurationValue>>> configurationValues = new HashMap<>();
    WrenchApplication wrenchApplication;
    private LiveData<WrenchApplication> wrenchApplicationLiveData;
    private LiveData<List<WrenchConfiguration>> configurations;
    private long applicationId;
    private String query;
    private LiveData<WrenchScope> selectedScopeLiveData;
    private LiveData<WrenchScope> defaultScopeLiveData;

    public ConfigurationViewModel(Application application) {
        super(application);

        wrenchDatabase = WrenchDatabase.getDatabase(application);
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    LiveData<WrenchApplication> getWrenchApplication() {
        if (wrenchApplicationLiveData == null) {
            wrenchApplicationLiveData = wrenchDatabase.applicationDao().get(applicationId);
        }
        return wrenchApplicationLiveData;
    }

    LiveData<List<WrenchConfigurationValue>> getConfigurationValues(long id) {
        if (!configurationValues.containsKey(id)) {
            configurationValues.put(id, wrenchDatabase.configurationValueDao().getConfigurationValue(id));
        }
        return configurationValues.get(id);
    }

    LiveData<List<WrenchConfiguration>> getConfigurations() {
        if (TextUtils.isEmpty(query)) {
            configurations = wrenchDatabase.configurationDao().getApplicationConfigurations(applicationId);
        } else {
            configurations = wrenchDatabase.configurationDao().getApplicationConfigurations(applicationId, "%" + query + "%");
        }
        return configurations;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    void deleteApplication(WrenchApplication wrenchApplication) {
        wrenchDatabase.applicationDao().delete(wrenchApplication);
    }

    LiveData<List<WrenchScope>> getScopes() {
        return wrenchDatabase.scopeDao().getScopes(applicationId);
    }

    @WorkerThread
    WrenchScope createScope(String scopeName) {
        WrenchScope wrenchScope = new WrenchScope();
        wrenchScope.setName(scopeName);
        wrenchScope.setApplicationId(applicationId);
        wrenchScope.setId(wrenchDatabase.scopeDao().insert(wrenchScope));

        return wrenchScope;
    }

    LiveData<WrenchScope> getSelectedScopeLiveData() {
        if (selectedScopeLiveData == null) {
            selectedScopeLiveData = wrenchDatabase.scopeDao().getSelectedScopeLiveData(applicationId);
        }
        return selectedScopeLiveData;
    }

    LiveData<WrenchScope> getDefaultScopeLiveData() {
        if (defaultScopeLiveData == null) {
            defaultScopeLiveData = wrenchDatabase.scopeDao().getDefaultScopeLiveData(applicationId);
        }
        return defaultScopeLiveData;
    }
}
