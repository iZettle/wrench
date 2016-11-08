package com.izettle.localconfiguration;

import android.net.Uri;

public class ConfigProviderHelper {
    public static final String AUTHORITY = BuildConfig.AUTHORITY;

    protected static final Uri configurationValueUri = Uri.parse("content://" + AUTHORITY + "/configurationValue");
    protected static final Uri configurationUri = Uri.parse("content://" + AUTHORITY + "/configuration");

    public static Uri configurationUri() {
        return configurationUri;
    }

    public static Uri configurationValueUri() {
        return configurationValueUri;
    }
}
