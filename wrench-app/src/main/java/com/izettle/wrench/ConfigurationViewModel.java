package com.izettle.wrench;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
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

import javax.inject.Inject;

public class ConfigurationViewModel extends ViewModel {
    private final WrenchDatabase wrenchDatabase;
    @SuppressLint("UseSparseArrays")
    final private Map<Long, LiveData<List<WrenchConfigurationValue>>> configurationValues = new HashMap<>();
    private final MutableLiveData<String> queryLiveData;
    WrenchApplication wrenchApplication;
    private LiveData<WrenchApplication> wrenchApplicationLiveData;
    private long applicationId;
    private LiveData<WrenchScope> selectedScopeLiveData;
    private LiveData<WrenchScope> defaultScopeLiveData;

    @Inject
    ConfigurationViewModel(WrenchDatabase wrenchDatabase) {
        this.wrenchDatabase = wrenchDatabase;

        queryLiveData = new MutableLiveData<>();

        setQuery("");
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
        return Transformations.switchMap(queryLiveData, query -> {
            if (TextUtils.isEmpty(query)) {
                return wrenchDatabase.configurationDao().getApplicationConfigurations(applicationId);
            } else {
                return wrenchDatabase.configurationDao().getApplicationConfigurations(applicationId, "%" + query + "%");
            }
        });
    }

    public void setQuery(String query) {
        queryLiveData.setValue(query);
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
