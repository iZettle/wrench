package com.example.wrench.wrenchprefs

import android.app.Application
import android.arch.lifecycle.ViewModel
import com.example.wrench.MainActivity
import com.example.wrench.R
import com.izettle.wrench.preferences.WrenchPreferences
import javax.inject.Inject

class WrenchPreferencesFragmentViewModel @Inject constructor(val application: Application, val wrenchPreferences: WrenchPreferences) : ViewModel() {

    fun getStringConfiguration(): String? {
        return wrenchPreferences.getString(application.resources.getString(R.string.string_configuration), "string1")
    }

    fun getUrlConfiguration(): String? {
        return wrenchPreferences.getString(application.resources.getString(R.string.url_configuration), "http://www.example.com/path?param=value")
    }

    fun getBooleanConfiguration(): Boolean? {
        return wrenchPreferences.getBoolean(application.resources.getString(R.string.boolean_configuration), true)
    }

    fun getIntConfiguration(): Int? {
        return wrenchPreferences.getInt(application.resources.getString(R.string.int_configuration), 1)
    }

    fun getEnumConfiguration(): MainActivity.MyEnum? {
        return wrenchPreferences.getEnum(application.resources.getString(R.string.enum_configuration), MainActivity.MyEnum::class.java, MainActivity.MyEnum.SECOND)
    }

    fun getServiceStringConfiguration(): String? {
        return wrenchPreferences.getString(application.resources.getString(R.string.service_configuration), null)
    }
}
