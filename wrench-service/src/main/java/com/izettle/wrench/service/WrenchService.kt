package com.izettle.wrench.service

import android.app.IntentService
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import com.izettle.wrench.core.Bolt
import com.izettle.wrench.core.WrenchProviderContract

class WrenchService : IntentService("WrenchService") {

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val extras = it.extras
            for (key in extras.keySet()) {
                val value = extras.get(key)
                when (value) {
                    is Int -> updateInteger(contentResolver, key, value)
                    is String -> updateString(contentResolver, key, value)
                    is Boolean -> updateBoolean(contentResolver, key, value)
                }
            }
        }
    }

    private fun updateInteger(contentResolver: ContentResolver, key: String, value: Int) {
        var bolt = getBolt(contentResolver, key) ?: return

        if (bolt.id == 0L) {
            bolt = bolt.copy(bolt.id, Bolt.TYPE.INTEGER, key, value.toString())
            val uri = insertBolt(contentResolver, bolt)
            bolt.id = uri!!.lastPathSegment.toLong()
        } else {
            bolt = bolt.copy(bolt.id, bolt.type, bolt.key, value.toString())
            updateBolt(contentResolver, bolt)
        }
    }

    private fun updateBoolean(contentResolver: ContentResolver, key: String, value: Boolean) {
        var bolt = getBolt(contentResolver, key) ?: return

        if (bolt.id == 0L) {
            bolt = bolt.copy(bolt.id, Bolt.TYPE.BOOLEAN, key, value.toString())
            val uri = insertBolt(contentResolver, bolt)
            bolt.id = uri!!.lastPathSegment.toLong()
        } else {
            bolt = bolt.copy(bolt.id, bolt.type, bolt.key, value.toString())
            updateBolt(contentResolver, bolt)
        }
    }

    private fun updateString(contentResolver: ContentResolver, key: String, value: String) {
        var bolt = getBolt(contentResolver, key) ?: return

        if (bolt.id == 0L) {
            bolt = bolt.copy(bolt.id, Bolt.TYPE.STRING, key, value)
            val uri = insertBolt(contentResolver, bolt)
            bolt.id = uri!!.lastPathSegment.toLong()
        } else {
            bolt = bolt.copy(bolt.id, bolt.type, bolt.key, value)
            updateBolt(contentResolver, bolt)
        }
    }

    private fun insertBolt(contentResolver: ContentResolver, bolt: Bolt): Uri? {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues())
    }

    private fun updateBolt(contentResolver: ContentResolver, bolt: Bolt) {
        contentResolver.update(WrenchProviderContract.boltUri(bolt.id), bolt.toContentValues(), null, null)
    }

    private fun getBolt(contentResolver: ContentResolver, key: String): Bolt? {
        val cursor = contentResolver.query(WrenchProviderContract.boltUri(key), null, null, null, null)
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
}
