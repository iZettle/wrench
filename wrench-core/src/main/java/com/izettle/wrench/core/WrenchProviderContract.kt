package com.izettle.wrench.core

import android.net.Uri

object WrenchProviderContract {
    const val WRENCH_AUTHORITY = BuildConfig.WRENCH_AUTHORITY

    private val boltUri = Uri.parse("content://$WRENCH_AUTHORITY/currentConfiguration")
    private val nutUri = Uri.parse("content://$WRENCH_AUTHORITY/predefinedConfigurationValue")

    @JvmStatic
    fun boltUri(id: Long): Uri {
        return Uri.withAppendedPath(boltUri, id.toString())
    }

    @JvmStatic
    fun boltUri(key: String): Uri {
        return Uri.withAppendedPath(boltUri, Uri.encode(key))
    }

    @JvmStatic
    fun boltUri(): Uri {
        return boltUri
    }

    @JvmStatic
    fun nutUri(): Uri {
        return nutUri
    }
}
