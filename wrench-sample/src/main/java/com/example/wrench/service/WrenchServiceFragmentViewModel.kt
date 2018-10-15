package com.example.wrench.service

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrench.MyEnum
import com.izettle.wrench.service.WrenchService

class WrenchServiceFragmentViewModel constructor(service: WrenchService) : ViewModel() {

    private val wrenchPreference = service.create(WrenchPreference::class.java)

    private val stringConfig: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getStringConfiguration(): LiveData<String> {
        AsyncTask.execute {
            stringConfig.postValue(wrenchPreference.getStringConfiguration())
        }
        return stringConfig
    }

    private val urlConfig: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getUrlConfiguration(): LiveData<String> {
        AsyncTask.execute {
            urlConfig.postValue(wrenchPreference.getUrlConfiguration())
        }
        return urlConfig
    }

    private val booleanConfig: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getBooleanConfiguration(): LiveData<Boolean> {
        AsyncTask.execute {
            booleanConfig.postValue(wrenchPreference.getBooleanConfiguration())
        }
        return booleanConfig
    }

    private val intConfig: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun getIntConfiguration(): LiveData<Int> {
        AsyncTask.execute {
            intConfig.postValue(wrenchPreference.getIntConfiguration())
        }
        return intConfig
    }

    private val enumConfig: MutableLiveData<MyEnum> by lazy {
        MutableLiveData<MyEnum>()
    }

    fun getEnumConfiguration(): LiveData<MyEnum> {
        AsyncTask.execute {
            enumConfig.postValue(wrenchPreference.getEnumConfiguration())
        }
        return enumConfig
    }

}
