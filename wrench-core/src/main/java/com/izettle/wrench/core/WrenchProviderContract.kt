package com.izettle.wrench.core

import android.net.Uri

object WrenchProviderContract {
    const val WRENCH_AUTHORITY = BuildConfig.WRENCH_AUTHORITY

    const val WRENCH_API_VERSION = "API_VERSION"

    private val boltUri = Uri.parse("content://$WRENCH_AUTHORITY/currentConfiguration")
    private val nutUri = Uri.parse("content://$WRENCH_AUTHORITY/predefinedConfigurationValue")

    @JvmStatic
    fun boltUri(id: Long): Uri {
        return boltUri
                .buildUpon()
                .appendPath(id.toString())
                .appendQueryParameter(WRENCH_API_VERSION, BuildConfig.WRENCH_API_VERSION.toString())
                .build()

    }

    @JvmStatic
    fun boltUri(key: String): Uri {
        return boltUri
                .buildUpon()
                .appendPath(key)
                .appendQueryParameter(WRENCH_API_VERSION, BuildConfig.WRENCH_API_VERSION.toString())
                .build()
    }

    @JvmStatic
    fun boltUri(): Uri {
        return boltUri
                .buildUpon()
                .appendQueryParameter(WRENCH_API_VERSION, BuildConfig.WRENCH_API_VERSION.toString())
                .build()
    }

    @JvmStatic
    fun nutUri(): Uri {
        return nutUri
                .buildUpon()
                .appendQueryParameter(WRENCH_API_VERSION, BuildConfig.WRENCH_API_VERSION.toString())
                .build()
    }
}
