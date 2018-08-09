package com.example.wrench.service

import android.arch.lifecycle.ViewModel
import com.example.wrench.MyEnum
import com.izettle.wrench.service.WrenchService
import javax.inject.Inject

class WrenchServiceFragmentViewModel @Inject constructor(service: WrenchService) : ViewModel(){

    private val wrenchPreference = service.create(WrenchPreference::class.java)

    val stringConfiguration: String = wrenchPreference.getStringConfiguration()

    val urlConfiguration: String = wrenchPreference.getUrlConfiguration()

    val booleanConfiguration: Boolean = wrenchPreference.getBooleanConfiguration()

    val intConfiguration: Int = wrenchPreference.getIntConfiguration()

    val enumConfiguration: MyEnum = wrenchPreference.getEnumConfiguration()

    val serviceStringConfiguration: String = wrenchPreference.getServiceStringConfiguration()
}
