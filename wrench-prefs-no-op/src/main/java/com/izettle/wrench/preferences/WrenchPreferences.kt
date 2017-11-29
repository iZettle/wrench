package com.izettle.wrench.preferences


import android.content.Context

@Suppress("unused", "UNUSED_PARAMETER")
class WrenchPreferences(context: Context) {

    fun <T : Enum<T>> getEnum(key: String, type: Class<T>, defValue: T): T {
        return defValue
    }

    fun getString(key: String, defValue: String): String {
        return defValue
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return defValue
    }

    fun getInt(key: String, defValue: Int): Int {
        return defValue
    }
}
