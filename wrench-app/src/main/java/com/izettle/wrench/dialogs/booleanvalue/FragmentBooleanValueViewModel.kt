package com.izettle.wrench.dialogs.booleanvalue

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.izettle.wrench.database.WrenchConfiguration
import com.izettle.wrench.database.WrenchConfigurationDao
import com.izettle.wrench.database.WrenchConfigurationValue
import com.izettle.wrench.database.WrenchConfigurationValueDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FragmentBooleanValueViewModel @Inject
constructor(private val configurationDao: WrenchConfigurationDao, private val configurationValueDao: WrenchConfigurationValueDao) : ViewModel() {

    internal val configuration: LiveData<WrenchConfiguration> by lazy {
        configurationDao.getConfiguration(configurationId)
    }

    internal val selectedConfigurationValueLiveData: LiveData<WrenchConfigurationValue> by lazy {
        configurationValueDao.getConfigurationValue(configurationId, scopeId)
    }


    private var configurationId: Long = 0
    private var scopeId: Long = 0
    internal var selectedConfigurationValue: WrenchConfigurationValue? = null

    internal fun init(configurationId: Long, scopeId: Long) {
        this.configurationId = configurationId
        this.scopeId = scopeId
    }

    fun updateConfigurationValue(value: String) {
        GlobalScope.launch {
            if (selectedConfigurationValue != null) {
                configurationValueDao.updateConfigurationValue(configurationId, scopeId, value)
            } else {
                val wrenchConfigurationValue = WrenchConfigurationValue(0, configurationId, value, scopeId)
                wrenchConfigurationValue.id = configurationValueDao.insert(wrenchConfigurationValue)
            }
            configurationDao.touch(configurationId, Date())
        }
    }

    internal fun deleteConfigurationValue() {
        GlobalScope.launch {
            configurationValueDao.delete(selectedConfigurationValue!!)
        }
    }
}
