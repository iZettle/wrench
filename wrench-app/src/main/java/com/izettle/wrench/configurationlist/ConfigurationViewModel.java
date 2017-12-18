package com.izettle.wrench.configurationlist;

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
    private final MutableLiveData<String> queryLiveData;
    private final WrenchApplicationDao applicationDao;
    private final LiveData<List<WrenchScope>> scopesLiveData;
    private final LiveData<List<WrenchConfigurationWithValues>> configurationListLiveData;
    private WrenchScopeDao scopeDao;
    private LiveData<WrenchApplication> wrenchApplicationLiveData;
    private MutableLiveData<Long> applicationIdLiveData;
    private LiveData<WrenchScope> selectedScopeLiveData;
    private LiveData<WrenchScope> defaultScopeLiveData;

    @Inject
    ConfigurationViewModel(WrenchApplicationDao applicationDao, WrenchConfigurationDao configurationDao, WrenchScopeDao scopeDao) {
        this.applicationDao = applicationDao;
        this.scopeDao = scopeDao;

        applicationIdLiveData = new MutableLiveData<>();

        wrenchApplicationLiveData = Transformations.switchMap(applicationIdLiveData, applicationDao::get);
        selectedScopeLiveData = Transformations.switchMap(applicationIdLiveData, scopeDao::getSelectedScopeLiveData);
        defaultScopeLiveData = Transformations.switchMap(applicationIdLiveData, scopeDao::getDefaultScopeLiveData);
        scopesLiveData = Transformations.switchMap(applicationIdLiveData, scopeDao::getScopes);

        queryLiveData = new MutableLiveData<>();

        setQuery("");

        configurationListLiveData = Transformations.switchMap(queryLiveData, query -> {
            if (TextUtils.isEmpty(query)) {
                return configurationDao.getApplicationConfigurations(applicationIdLiveData.getValue());
            } else {
                return configurationDao.getApplicationConfigurations(applicationIdLiveData.getValue(), "%" + query + "%");
            }
        });

    }

    void setApplicationId(long applicationIdLiveData) {
        this.applicationIdLiveData.setValue(applicationIdLiveData);
    }

    LiveData<WrenchApplication> getWrenchApplication() {
        return wrenchApplicationLiveData;
    }

    LiveData<List<WrenchConfigurationWithValues>> getConfigurations() {
        return configurationListLiveData;
    }

    public void setQuery(String query) {
        queryLiveData.setValue(query);
    }

    void deleteApplication(WrenchApplication wrenchApplication) {
        applicationDao.delete(wrenchApplication);
    }

    @WorkerThread
    WrenchScope createScope(String scopeName) {
        WrenchScope wrenchScope = new WrenchScope();
        wrenchScope.setName(scopeName);
        wrenchScope.setApplicationId(applicationIdLiveData.getValue());
        wrenchScope.setId(scopeDao.insert(wrenchScope));

        return wrenchScope;
    }

    LiveData<List<WrenchScope>> getScopes() {
        return scopesLiveData;
    }

    LiveData<WrenchScope> getSelectedScopeLiveData() {
        return selectedScopeLiveData;
    }

    LiveData<WrenchScope> getDefaultScopeLiveData() {
        return defaultScopeLiveData;
    }
}
