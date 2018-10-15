package com.izettle.wrench.dialogs.enumvalue

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.izettle.wrench.database.*
import java.util.*
import javax.inject.Inject

class FragmentEnumValueViewModel @Inject
constructor(
        private val configurationDao: WrenchConfigurationDao,
        private val configurationValueDao: WrenchConfigurationValueDao,
        private val predefinedConfigurationValueDao: WrenchPredefinedConfigurationValueDao)
    : ViewModel() {

    internal val configuration: LiveData<WrenchConfiguration> by lazy {
        configurationDao.getConfiguration(configurationId)
    }

    internal val selectedConfigurationValueLiveData: LiveData<WrenchConfigurationValue>  by lazy {
        configurationValueDao.getConfigurationValue(configurationId, scopeId)
    }

    internal val predefinedValues: LiveData<List<WrenchPredefinedConfigurationValue>> by lazy {
        predefinedConfigurationValueDao.getByConfigurationId(configurationId)
    }

    private var configurationId: Long = 0
    private var scopeId: Long = 0

    internal var selectedConfigurationValue: WrenchConfigurationValue? = null

    internal fun init(configurationId: Long, scopeId: Long) {
        this.configurationId = configurationId
        this.scopeId = scopeId
    }

    fun updateConfigurationValue(value: String) {
        if (selectedConfigurationValue != null) {
            configurationValueDao.updateConfigurationValue(configurationId, scopeId, value)
        } else {
            val wrenchConfigurationValue = WrenchConfigurationValue(0, configurationId, value, scopeId)
            wrenchConfigurationValue.id = configurationValueDao.insert(wrenchConfigurationValue)
        }
        configurationDao.touch(configurationId, Date())
    }

    internal fun deleteConfigurationValue() {
        configurationValueDao.delete(selectedConfigurationValue!!)
    }
}
