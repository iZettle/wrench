package com.izettle.wrench.preferences

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.izettle.wrench.core.Bolt
import com.izettle.wrench.core.Nut
import com.izettle.wrench.core.WrenchProviderContract

open class WrenchPreferences(context: Context) {
    private val contentResolver: ContentResolver = context.contentResolver

    private fun insertNut(contentResolver: ContentResolver, nut: Nut) {
        contentResolver.insert(WrenchProviderContract.nutUri(), nut.toContentValues())
    }

    private fun getBolt(contentResolver: ContentResolver, @Bolt.BoltType boltType: String, key: String): Bolt? {
        val cursor = contentResolver.query(WrenchProviderContract.boltUri(key), null, null, null, null)
        cursor.use {
            if (cursor == null) {
                return null
            }

            if (cursor.moveToFirst()) {
                return Bolt.fromCursor(cursor)
            }
        }

        return Bolt(type = boltType, key = key)
    }

    private fun insertBolt(contentResolver: ContentResolver, bolt: Bolt): Uri? {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues())
    }

    fun <T : Enum<T>> getEnum(key: String, type: Class<T>, defValue: T): T {
        var bolt = getBolt(contentResolver, Bolt.TYPE.ENUM, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt = bolt.copy(key = key, type = Bolt.TYPE.ENUM, value = defValue.toString())
            val uri = insertBolt(contentResolver, bolt)
            bolt.id = uri!!.lastPathSegment.toLong()

            for (enumConstant in type.enumConstants) {
                insertNut(contentResolver, Nut(bolt.id, enumConstant.toString()))
            }
        }

        return java.lang.Enum.valueOf<T>(type, bolt.value!!)
    }

    fun getString(key: String, defValue: String?): String? {

        var bolt = getBolt(contentResolver, Bolt.TYPE.STRING, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt = bolt.copy(key = key, type = Bolt.TYPE.STRING, value = defValue)
            insertBolt(contentResolver, bolt)
        }

        return bolt.value
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        var bolt = getBolt(contentResolver, Bolt.TYPE.BOOLEAN, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt = bolt.copy(key = key, type = Bolt.TYPE.BOOLEAN, value = defValue.toString())
            insertBolt(contentResolver, bolt)
        }

        return java.lang.Boolean.valueOf(bolt.value)!!
    }

    fun getInt(key: String, defValue: Int): Int {
        var bolt = getBolt(contentResolver, Bolt.TYPE.INTEGER, key) ?: return defValue

        if (bolt.id == 0L) {
            bolt = bolt.copy(key = key, type = Bolt.TYPE.INTEGER, value = defValue.toString())
            insertBolt(contentResolver, bolt)
        }

        return Integer.valueOf(bolt.value)!!
    }
}
