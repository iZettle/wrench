package com.izettle.wrench.configurationlist;

import android.text.TextUtils;

import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchApplicationDao;
import com.izettle.wrench.database.WrenchConfigurationDao;
import com.izettle.wrench.database.WrenchConfigurationWithValues;
import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.database.WrenchScopeDao;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ConfigurationViewModel extends ViewModel {
    private final MutableLiveData<String> queryLiveData;
    private final WrenchApplicationDao applicationDao;
    private final LiveData<List<WrenchScope>> scopesLiveData;
    private final MediatorLiveData<List<WrenchConfigurationWithValues>> configurationListLiveData;
    private final WrenchScopeDao scopeDao;
    private final LiveData<WrenchApplication> wrenchApplicationLiveData;
    private final MutableLiveData<Long> applicationIdLiveData;
    private final LiveData<WrenchScope> selectedScopeLiveData;
    private final LiveData<WrenchScope> defaultScopeLiveData;
    private final MutableLiveData<Boolean> listEmpty;

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

        listEmpty = new MutableLiveData<>();

        LiveData<List<WrenchConfigurationWithValues>> configurationsLiveData = Transformations.switchMap(queryLiveData, query -> {
            if (TextUtils.isEmpty(query)) {
                return configurationDao.getApplicationConfigurations(applicationIdLiveData.getValue());
            } else {
                return configurationDao.getApplicationConfigurations(applicationIdLiveData.getValue(), "%" + query + "%");
            }
        });

        configurationListLiveData = new MediatorLiveData<>();
        configurationListLiveData.addSource(configurationsLiveData, wrenchConfigurationWithValues -> {
            listEmpty.setValue(wrenchConfigurationWithValues == null || wrenchConfigurationWithValues.size() == 0);
            configurationListLiveData.setValue(wrenchConfigurationWithValues);
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

    LiveData<Boolean> isListEmpty() {
        return listEmpty;
    }

}
