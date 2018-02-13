package com.example.wrench.livedataprefs

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.wrench.BoltLiveData.BoltLiveData
import com.example.wrench.MainActivity
import com.example.wrench.R
import com.izettle.wrench.core.Bolt
import javax.inject.Inject

class LiveDataPreferencesFragmentViewModel @Inject constructor(val application: Application) : ViewModel() {

    internal fun getStringBolt(): LiveData<Bolt> {
        return BoltLiveData.create(application, application.resources.getString(R.string.string_configuration), "string1")
    }

    internal fun getIntBolt(): LiveData<Bolt> {
        return BoltLiveData.create(application, application.resources.getString(R.string.int_configuration), 1)
    }

    internal fun getBooleanBolt(): LiveData<Bolt> {
        return BoltLiveData.create(application, application.resources.getString(R.string.boolean_configuration), true)
    }

    internal fun getUrlBolt(): LiveData<Bolt> {
        return BoltLiveData.create(application, application.resources.getString(R.string.url_configuration), "http://www.example.com/path?param=value")
    }

    internal fun getEnumBolt(): LiveData<Bolt> {
        return BoltLiveData.create(application, application.resources.getString(R.string.enum_configuration), MainActivity.MyEnum::class.java, MainActivity.MyEnum.FIRST)
    }

    internal fun getServiceStringBolt(): LiveData<Bolt> {
        return BoltLiveData.create(application, application.resources.getString(R.string.service_configuration), null)
    }
}