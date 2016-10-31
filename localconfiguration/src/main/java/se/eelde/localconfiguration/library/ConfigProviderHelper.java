package se.eelde.localconfiguration.library;

import android.net.Uri;

public class ConfigProviderHelper {
    public static final String AUTHORITY = BuildConfig.AUTHORITY;

    private static final Uri configurationUri = Uri.parse("content://" + AUTHORITY + "/configuration");

    public static Uri configurationUri() {
        return configurationUri;
    }
}
