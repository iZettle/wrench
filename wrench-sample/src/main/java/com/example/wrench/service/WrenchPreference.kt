package com.example.wrench.service

import com.example.wrench.MyEnum
import com.izettle.wrench.service.DefaultValue
import com.izettle.wrench.service.Key

interface WrenchPreference {

    @Key("String configuration:")
    @DefaultValue.String("string1")
    fun getStringConfiguration(): String

    @Key("Url configuration (http://www.example.com/path?param=value):")
    fun getUrlConfiguration(@DefaultValue value: String = "http://www.example.com/path?param=value"): String

    @Key("boolean configuration:")
    @DefaultValue.Boolean(true)
    fun getBooleanConfiguration(): Boolean

    @Key("int configuration:")
    @DefaultValue.Int(1)
    fun getIntConfiguration(): Int

    @Key("enum configuration:")
    fun getEnumConfiguration(@DefaultValue value: MyEnum = MyEnum.SECOND): MyEnum

    @Key("service_configuration")
    fun getServiceStringConfiguration(@DefaultValue value: String? = null): String
}