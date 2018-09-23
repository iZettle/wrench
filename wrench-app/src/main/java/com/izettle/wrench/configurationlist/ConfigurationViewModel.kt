package com.izettle.wrench.configurationlist

import android.text.TextUtils
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import com.izettle.wrench.database.*
import javax.inject.Inject

class ConfigurationViewModel @Inject
internal constructor(private val applicationDao: WrenchApplicationDao, configurationDao: WrenchConfigurationDao, private val scopeDao: WrenchScopeDao) : ViewModel() {
    private val queryLiveData: MutableLiveData<String>
    private val scopes: LiveData<List<WrenchScope>>
    private val configurationListLiveData: MediatorLiveData<List<WrenchConfigurationWithValues>>
    internal val wrenchApplication: LiveData<WrenchApplication>
    private val applicationIdLiveData: MutableLiveData<Long> = MutableLiveData()
    internal val selectedScopeLiveData: LiveData<WrenchScope>
    internal val defaultScopeLiveData: LiveData<WrenchScope>
    private val listEmpty: MutableLiveData<Boolean>

    internal val configurations: LiveData<List<WrenchConfigurationWithValues>>
        get() = configurationListLiveData

    internal val isListEmpty: LiveData<Boolean>
        get() = listEmpty

    init {

        wrenchApplication = Transformations.switchMap(applicationIdLiveData) { applicationId: Long -> applicationDao.getApplication(applicationId) }

        selectedScopeLiveData = Transformations.switchMap(applicationIdLiveData) { applicationId: Long -> scopeDao.getSelectedScopeLiveData(applicationId) }

        defaultScopeLiveData = Transformations.switchMap(applicationIdLiveData) { applicationId: Long -> scopeDao.getDefaultScopeLiveData(applicationId) }

        scopes = Transformations.switchMap(applicationIdLiveData) { applicationId: Long -> scopeDao.getScopes(applicationId) }

        queryLiveData = MutableLiveData()

        setQuery("")

        listEmpty = MutableLiveData()

        val configurationsLiveData = Transformations.switchMap<String, List<WrenchConfigurationWithValues>>(queryLiveData) { query ->
            if (TextUtils.isEmpty(query)) {
                configurationDao.getApplicationConfigurations(applicationIdLiveData.value!!)
            } else {
                configurationDao.getApplicationConfigurations(applicationIdLiveData.value!!, "%$query%")
            }
        }

        configurationListLiveData = MediatorLiveData()
        configurationListLiveData.addSource(configurationsLiveData) { wrenchConfigurationWithValues ->
            listEmpty.value = wrenchConfigurationWithValues == null || wrenchConfigurationWithValues.isEmpty()
            configurationListLiveData.setValue(wrenchConfigurationWithValues)
        }
    }

    internal fun setApplicationId(applicationIdLiveData: Long) {
        this.applicationIdLiveData.value = applicationIdLiveData
    }

    fun setQuery(query: String) {
        queryLiveData.value = query
    }

    internal fun deleteApplication(wrenchApplication: WrenchApplication) {
        applicationDao.delete(wrenchApplication)
    }

    @WorkerThread
    internal fun createScope(scopeName: String): WrenchScope {
        val wrenchScope = WrenchScope()
        wrenchScope.name = scopeName
        wrenchScope.applicationId = applicationIdLiveData.value!!
        wrenchScope.id = scopeDao.insert(wrenchScope)

        return wrenchScope
    }

}
