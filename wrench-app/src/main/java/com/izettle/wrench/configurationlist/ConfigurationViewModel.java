package com.izettle.wrench.configurationlist;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchApplicationDao;
import com.izettle.wrench.database.WrenchConfigurationDao;
import com.izettle.wrench.database.WrenchConfigurationWithValues;
import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.database.WrenchScopeDao;

import java.util.List;

import javax.inject.Inject;

public class ConfigurationViewModel extends ViewModel {
    @SuppressLint("UseSparseArrays")
    private final MutableLiveData<String> queryLiveData;
    private final WrenchApplicationDao applicationDao;
    WrenchApplication wrenchApplication;
    private WrenchScopeDao scopeDao;
    private LiveData<WrenchApplication> wrenchApplicationLiveData;
    private long applicationId;
    private LiveData<WrenchScope> selectedScopeLiveData;
    private LiveData<WrenchScope> defaultScopeLiveData;
    private WrenchConfigurationDao configurationDao;

    @Inject
    ConfigurationViewModel(WrenchApplicationDao applicationDao, WrenchConfigurationDao configurationDao, WrenchScopeDao scopeDao) {
        this.applicationDao = applicationDao;
        this.configurationDao = configurationDao;
        this.scopeDao = scopeDao;

        queryLiveData = new MutableLiveData<>();

        setQuery("");
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    LiveData<WrenchApplication> getWrenchApplication() {
        if (wrenchApplicationLiveData == null) {
            wrenchApplicationLiveData = applicationDao.get(applicationId);
        }
        return wrenchApplicationLiveData;
    }

    LiveData<List<WrenchConfigurationWithValues>> getConfigurations() {
        return Transformations.switchMap(queryLiveData, query -> {
            if (TextUtils.isEmpty(query)) {
                return configurationDao.getApplicationConfigurations(applicationId);
            } else {
                return configurationDao.getApplicationConfigurations(applicationId, "%" + query + "%");
            }
        });
    }

    public void setQuery(String query) {
        queryLiveData.setValue(query);
    }

    void deleteApplication(WrenchApplication wrenchApplication) {
        applicationDao.delete(wrenchApplication);
    }

    LiveData<List<WrenchScope>> getScopes() {
        return scopeDao.getScopes(applicationId);
    }

    @WorkerThread
    WrenchScope createScope(String scopeName) {
        WrenchScope wrenchScope = new WrenchScope();
        wrenchScope.setName(scopeName);
        wrenchScope.setApplicationId(applicationId);
        wrenchScope.setId(scopeDao.insert(wrenchScope));

        return wrenchScope;
    }

    LiveData<WrenchScope> getSelectedScopeLiveData() {
        if (selectedScopeLiveData == null) {
            selectedScopeLiveData = scopeDao.getSelectedScopeLiveData(applicationId);
        }
        return selectedScopeLiveData;
    }

    LiveData<WrenchScope> getDefaultScopeLiveData() {
        if (defaultScopeLiveData == null) {
            defaultScopeLiveData = scopeDao.getDefaultScopeLiveData(applicationId);
        }
        return defaultScopeLiveData;
    }
}
