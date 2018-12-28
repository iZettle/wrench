package com.example.wrench.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrench.MyEnum
import com.izettle.wrench.service.WrenchService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WrenchServiceFragmentViewModel constructor(service: WrenchService) : ViewModel() {

    private val wrenchPreference = service.create(WrenchPreference::class.java)

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private val stringConfig: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getStringConfiguration(): LiveData<String> {
        scope.launch {
            stringConfig.postValue(wrenchPreference.getStringConfiguration())
        }
        return stringConfig
    }

    private val urlConfig: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getUrlConfiguration(): LiveData<String> {
        scope.launch {
            urlConfig.postValue(wrenchPreference.getUrlConfiguration())
        }
        return urlConfig
    }

    private val booleanConfig: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getBooleanConfiguration(): LiveData<Boolean> {
        scope.launch {
            booleanConfig.postValue(wrenchPreference.getBooleanConfiguration())
        }
        return booleanConfig
    }

    private val intConfig: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun getIntConfiguration(): LiveData<Int> {
        scope.launch {
            intConfig.postValue(wrenchPreference.getIntConfiguration())
        }
        return intConfig
    }

    private val enumConfig: MutableLiveData<MyEnum> by lazy {
        MutableLiveData<MyEnum>()
    }

    fun getEnumConfiguration(): LiveData<MyEnum> {
        scope.launch {
            enumConfig.postValue(wrenchPreference.getEnumConfiguration())
        }
        return enumConfig
    }

}
