package com.izettle.wrench.core;

import android.net.Uri;

import androidx.annotation.NonNull;

public final class WrenchProviderContract {
    public static final String WRENCH_AUTHORITY = BuildConfig.WRENCH_AUTHORITY;

    public static final String WRENCH_API_VERSION = "API_VERSION";

    private static Uri boltUri = Uri.parse("content://" + WRENCH_AUTHORITY + "/currentConfiguration");
    private static Uri nutUri = Uri.parse("content://" + WRENCH_AUTHORITY + "/predefinedConfigurationValue");

    @NonNull
    public static Uri boltUri(long id) {
        return boltUri
                .buildUpon()
                .appendPath(String.valueOf(id))
                .appendQueryParameter(WRENCH_API_VERSION, String.valueOf(BuildConfig.WRENCH_API_VERSION))
                .build();
    }

    @NonNull
    public static Uri boltUri(String key) {
        return boltUri
                .buildUpon()
                .appendPath(key)
                .appendQueryParameter(WRENCH_API_VERSION, String.valueOf(BuildConfig.WRENCH_API_VERSION))
                .build();
    }

    @NonNull
    public static Uri boltUri() {
        return boltUri
                .buildUpon()
                .appendQueryParameter(WRENCH_API_VERSION, String.valueOf(BuildConfig.WRENCH_API_VERSION))
                .build();
    }

    @NonNull
    public static Uri nutUri() {
        return nutUri
                .buildUpon()
                .appendQueryParameter(WRENCH_API_VERSION, String.valueOf(BuildConfig.WRENCH_API_VERSION))
                .build();
    }
}
