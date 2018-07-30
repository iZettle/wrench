package com.example.wrench.service

import android.arch.lifecycle.ViewModel
import com.example.wrench.MyEnum
import com.example.wrench.R
import com.izettle.wrench.service.WrenchServiceFactory
import javax.inject.Inject

class WrenchServiceFragmentViewModel @Inject constructor(factory: WrenchServiceFactory) : ViewModel(){

    private val wrenchPreference = factory.create(WrenchPreference::class.java)

    val stringConfiguration: String = wrenchPreference.getStringConfiguration()

    val urlConfiguration: String = wrenchPreference.getUrlConfiguration()

    val booleanConfiguration: Boolean = wrenchPreference.getBooleanConfiguration()

    val intConfiguration: Int = wrenchPreference.getIntConfiguration()

    val enumConfiguration: MyEnum = wrenchPreference.getEnumConfiguration()

    val serviceStringConfiguration: String = wrenchPreference.getServiceStringConfiguration()
}
