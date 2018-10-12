package com.izettle.wrench.service.helper

import com.izettle.wrench.service.DefaultValue
import com.izettle.wrench.service.Key

interface KTestInterface {

    /* Constants default value */

    val stringReturnType: String
        @Key("getStringReturnType")
        @DefaultValue.String("getStringReturnType")
        get

    val intReturnType: Int
        @Key("getIntReturnType")
        @DefaultValue.Int(1)
        get

    val booleanReturnType: Boolean
        @Key("getBooleanReturnType")
        @DefaultValue.Boolean(true)
        get

    /* Constants default value with missing key */

    val stringReturnTypeMissingKey: String
        @DefaultValue.String("getStringReturnTypeMissingKey")
        get

    val intReturnTypeMissingKey: Int
        @DefaultValue.Int(1)
        get

    val booleanReturnTypeMissingKey: Boolean
        @DefaultValue.Boolean(true)
        get

    /* Constants default value with missing default value */

    val stringReturnTypeMissingDefaultValue: String
        @Key("getStringReturnTypeMissingDefaultValue")
        get

    val intReturnTypeMissingDefaultValue: Int
        @Key("getIntReturnTypeMissingDefaultValue")
        get

    val booleanReturnTypeMissingDefaultValue: Boolean
        @Key("getBooleanReturnTypeMissingDefaultValue")
        get
}