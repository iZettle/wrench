package com.example.wrench.wrenchprefs

import android.content.res.Resources
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrench.MyEnum
import com.example.wrench.R
import com.izettle.wrench.preferences.WrenchPreferences

class WrenchPreferencesFragmentViewModel constructor(val resources: Resources, private val wrenchPreferences: WrenchPreferences) : ViewModel() {

    private val stringConfig: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getStringConfiguration(): LiveData<String> {
        AsyncTask.execute {
            stringConfig.postValue(wrenchPreferences.getString(resources.getString(R.string.string_configuration), "string1"))
        }
        return stringConfig
    }

    private val urlConfig: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getUrlConfiguration(): LiveData<String> {
        AsyncTask.execute {
            urlConfig.postValue(wrenchPreferences.getString(resources.getString(R.string.url_configuration), "http://www.example.com/path?param=value"))
        }
        return urlConfig
    }

    private val booleanConfig: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getBooleanConfiguration(): LiveData<Boolean> {
        AsyncTask.execute {
            booleanConfig.postValue(wrenchPreferences.getBoolean(resources.getString(R.string.boolean_configuration), true))
        }
        return booleanConfig
    }

    private val intConfig: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun getIntConfiguration(): LiveData<Int> {
        AsyncTask.execute {
            intConfig.postValue(wrenchPreferences.getInt(resources.getString(R.string.int_configuration), 1))
        }
        return intConfig

    }

    private val enumConfig: MutableLiveData<MyEnum> by lazy {
        MutableLiveData<MyEnum>()
    }

    fun getEnumConfiguration(): LiveData<MyEnum> {
        AsyncTask.execute {
            enumConfig.postValue(wrenchPreferences.getEnum(resources.getString(R.string.enum_configuration), MyEnum::class.java, MyEnum.SECOND))
        }
        return enumConfig
    }
}
