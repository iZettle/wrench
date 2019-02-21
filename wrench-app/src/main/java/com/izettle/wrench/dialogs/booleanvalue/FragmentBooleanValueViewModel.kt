package com.izettle.wrench.dialogs.booleanvalue

import androidx.lifecycle.*
import com.izettle.wrench.Event
import com.izettle.wrench.database.WrenchConfiguration
import com.izettle.wrench.database.WrenchConfigurationDao
import com.izettle.wrench.database.WrenchConfigurationValue
import com.izettle.wrench.database.WrenchConfigurationValueDao
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import java.util.*

class FragmentBooleanValueViewModel
constructor(private val configurationDao: WrenchConfigurationDao, private val configurationValueDao: WrenchConfigurationValueDao) : ViewModel() {

    private val configuration: LiveData<WrenchConfiguration> by lazy {
        configurationDao.getConfiguration(configurationId)
    }

    private val selectedConfigurationValueLiveData: LiveData<WrenchConfigurationValue> by lazy {
        configurationValueDao.getConfigurationValue(configurationId, scopeId)
    }

    private var configurationId: Long = 0
    private var scopeId: Long = 0
    private var selectedConfigurationValue: WrenchConfigurationValue? = null

    val viewState = MediatorLiveData<ViewState>().apply {
        value = ViewState.Empty
    }

    val viewEffects = MutableLiveData<Event<ViewEffect>>()

    @ExperimentalCoroutinesApi
    private val channel = ConflatedBroadcastChannel<ViewAction>().apply {
        viewModelScope.launch {
            for (viewAction in openSubscription()) {
                when (viewAction) {
                    is ViewAction.SaveAction -> {
                        viewState.value = ViewState.Loading
                        updateConfigurationValue(viewAction.value).join()
                        viewEffects.value = Event(ViewEffect.Dismiss)
                    }
                    ViewAction.RevertAction -> {
                        viewState.value = ViewState.Loading
                        deleteConfigurationValue()
                        viewEffects.value = Event(ViewEffect.Dismiss)
                    }
                }
            }
        }
    }

    internal fun init(configurationId: Long, scopeId: Long) {
        this.configurationId = configurationId
        this.scopeId = scopeId

        viewState.addSource(configuration) { wrenchConfig -> viewState.value = ViewState.NewConfiguration(wrenchConfig.key) }
        viewState.addSource(selectedConfigurationValueLiveData) { wrenchConfigurationValue ->
            if (wrenchConfigurationValue != null) {
                selectedConfigurationValue = wrenchConfigurationValue
                viewState.value = ViewState.NewConfigurationValue(wrenchConfigurationValue.value!!.toBoolean())
            }
        }
    }


    @ExperimentalCoroutinesApi
    internal fun saveClick(value: String) {
        viewModelScope.launch {
            channel.send(ViewAction.SaveAction(value))
        }
    }

    @ExperimentalCoroutinesApi
    internal fun revertClick() {
        viewModelScope.launch {
            channel.send(ViewAction.RevertAction)
        }
    }

    private suspend fun updateConfigurationValue(value: String): Job = coroutineScope {
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedConfigurationValue != null) {
                configurationValueDao.updateConfigurationValue(configurationId, scopeId, value)

            } else {
                val wrenchConfigurationValue = WrenchConfigurationValue(0, configurationId, value, scopeId)
                wrenchConfigurationValue.id = configurationValueDao.insert(wrenchConfigurationValue)
            }

            configurationDao.touch(configurationId, Date())
        }
    }


    private suspend fun deleteConfigurationValue() = coroutineScope {
        configurationValueDao.delete(selectedConfigurationValue!!)
    }
}

sealed class ViewAction {
    data class SaveAction(val value: String) : ViewAction()
    object RevertAction : ViewAction()
}

sealed class ViewEffect {
    object Dismiss : ViewEffect()
}

sealed class ViewState {
    object Empty : ViewState()
    data class NewConfiguration(val title: String?) : ViewState()
    data class NewConfigurationValue(val enabled: Boolean) : ViewState()
    object Loading : ViewState()
}


