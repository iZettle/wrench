package com.izettle.wrench.preferences

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri

import com.izettle.wrench.core.Bolt
import com.izettle.wrench.core.Nut
import com.izettle.wrench.core.WrenchProviderContract

class WrenchPreferences(context: Context) {
    private val contentResolver: ContentResolver = context.contentResolver

    private fun insertNut(contentResolver: ContentResolver, nut: Nut) {
        contentResolver.insert(WrenchProviderContract.nutUri(), nut.toContentValues())
    }

    private fun getBolt(contentResolver: ContentResolver, key: String): Bolt? {
        val cursor: Cursor? = contentResolver.query(WrenchProviderContract.boltUri(key), null, null, null, null)
        cursor.use {
            if (cursor == null) {
                return null
            }

            if (cursor.moveToFirst()) {
                return Bolt.fromCursor(cursor)
            }
        }

        return Bolt()
    }

    private fun insertBolt(contentResolver: ContentResolver, bolt: Bolt): Uri? {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues())
    }

    fun <T : Enum<T>> getEnum(key: String, type: Class<T>, defValue: T): T {
        val bolt = getBolt(contentResolver, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt.copy(key = key, type = Enum::class.java.name, value = defValue.toString())
            val uri = insertBolt(contentResolver, bolt)
            bolt.id = java.lang.Long.parseLong(uri!!.lastPathSegment)

            for (enumConstant in type.enumConstants) {
                insertNut(contentResolver, Nut(bolt.id, enumConstant.toString()))
            }
        }

        return java.lang.Enum.valueOf<T>(type, bolt.value!!)
    }

    fun getString(key: String, defValue: String?): String? {

        val bolt = getBolt(contentResolver, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt.copy(key = key, type = String::class.java.name, value = defValue)
            insertBolt(contentResolver, bolt)
        }

        return bolt.value
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val bolt = getBolt(contentResolver, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt.copy(key = key, type = Boolean::class.java.name, value = defValue.toString())
            insertBolt(contentResolver, bolt)
        }

        return java.lang.Boolean.valueOf(bolt.value)!!
    }

    fun getInt(key: String, defValue: Int): Int {
        val bolt = getBolt(contentResolver, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt.copy(key = key, type = Int::class.java.name, value = defValue.toString())
            insertBolt(contentResolver, bolt)
        }

        return Integer.valueOf(bolt.value)!!
    }
}
