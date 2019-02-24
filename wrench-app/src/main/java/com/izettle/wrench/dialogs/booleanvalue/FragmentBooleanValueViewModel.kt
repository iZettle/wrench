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

@ExperimentalCoroutinesApi
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
        value = reduce(ViewState(), PartialViewState.Empty)
    }

    val viewEffects = MutableLiveData<Event<ViewEffect>>()

    @ExperimentalCoroutinesApi
    private val channel = ConflatedBroadcastChannel<ViewAction>().apply {
        viewModelScope.launch {
            for (viewAction in openSubscription()) {
                when (viewAction) {
                    is ViewAction.SaveAction -> {
                        viewState.value = reduce(viewState.value!!, PartialViewState.Saving)
                        updateConfigurationValue(viewAction.value).join()
                        viewEffects.value = Event(ViewEffect.Dismiss)
                    }
                    ViewAction.RevertAction -> {
                        viewState.value = reduce(viewState.value!!, PartialViewState.Reverting)
                        deleteConfigurationValue()
                        viewEffects.value = Event(ViewEffect.Dismiss)
                    }
                    is ViewAction.CheckedChanged -> {
                        viewState.value = reduce(viewState.value!!, PartialViewState.CheckChanged(viewAction.checked))
                    }
                }
            }
        }
    }

    internal fun init(configurationId: Long, scopeId: Long) {
        this.configurationId = configurationId
        this.scopeId = scopeId

        viewState.addSource(configuration) { wrenchConfig -> viewState.value = reduce(viewState.value!!, PartialViewState.NewConfiguration(wrenchConfig.key)) }
        viewState.addSource(selectedConfigurationValueLiveData) { wrenchConfigurationValue ->
            if (wrenchConfigurationValue != null) {
                selectedConfigurationValue = wrenchConfigurationValue
                viewState.value = reduce(viewState.value!!, PartialViewState.NewConfigurationValue(wrenchConfigurationValue.value!!.toBoolean()))
            }
        }
    }

    private fun reduce(previousState: ViewState, partialViewState: PartialViewState): ViewState {
        return when (partialViewState) {
            is PartialViewState.NewConfiguration -> {
                previousState.copy(title = partialViewState.title)
            }
            is PartialViewState.NewConfigurationValue -> {
                previousState.copy(enabled = partialViewState.enabled)
            }
            is PartialViewState.Empty -> {
                previousState
            }
            is PartialViewState.Saving -> {
                previousState.copy(saving = true)
            }
            is PartialViewState.Reverting -> {
                previousState.copy(reverting = true)
            }
            is PartialViewState.CheckChanged -> {
                previousState.copy(enabled = partialViewState.checked)
            }
        }
    }

    internal fun saveClick(value: String) {
        viewModelScope.launch {
            channel.send(ViewAction.SaveAction(value))
        }
    }

    internal fun revertClick() {
        viewModelScope.launch {
            channel.send(ViewAction.RevertAction)
        }
    }

    fun checkedChanged(checked: Boolean) {
        viewModelScope.launch {
            channel.send(ViewAction.CheckedChanged(checked))
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
    data class CheckedChanged(val checked: Boolean) : ViewAction()
}

sealed class ViewEffect {
    object Dismiss : ViewEffect()
}

data class ViewState(val title: String? = null,
                     val enabled: Boolean? = null,
                     val saving: Boolean = false,
                     val reverting: Boolean = false)

private sealed class PartialViewState {
    object Empty : PartialViewState()
    data class NewConfiguration(val title: String?) : PartialViewState()
    data class NewConfigurationValue(val enabled: Boolean) : PartialViewState()
    data class CheckChanged(val checked: Boolean) : PartialViewState()

    object Saving : PartialViewState()
    object Reverting : PartialViewState()
}


