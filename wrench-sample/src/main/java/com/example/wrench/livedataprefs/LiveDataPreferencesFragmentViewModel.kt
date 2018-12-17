package com.example.wrench.livedataprefs

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.wrench.MyEnum
import com.example.wrench.R
import com.izettle.wrench.livedata.WrenchLiveData

class LiveDataPreferencesFragmentViewModel constructor(private val context: Context) : ViewModel() {

    private val stringConfig by lazy {
        WrenchLiveData.create(context, context.resources.getString(R.string.string_configuration), "string1")
    }

    fun getStringConfiguration(): LiveData<String> {
        return stringConfig
    }

    private val intConfig by lazy {
        WrenchLiveData.create(context, context.resources.getString(R.string.int_configuration), 1)
    }

    fun getIntConfiguration(): LiveData<Int> {
        return intConfig
    }

    private val booleanConfig by lazy {
        WrenchLiveData.create(context, context.resources.getString(R.string.boolean_configuration), true)
    }

    fun getBooleanConfiguration(): LiveData<Boolean> {
        return booleanConfig
    }

    private val urlConfig by lazy {
        WrenchLiveData.create(context, context.resources.getString(R.string.url_configuration), "http://www.example.com/path?param=value")
    }

    fun getUrlConfiguration(): LiveData<String> {
        return urlConfig
    }

    private val enumConfig by lazy {
        WrenchLiveData.create(context, context.resources.getString(R.string.enum_configuration), MyEnum::class.java, MyEnum.FIRST)
    }

    fun getEnumConfiguration(): LiveData<MyEnum> {
        return enumConfig
    }
}