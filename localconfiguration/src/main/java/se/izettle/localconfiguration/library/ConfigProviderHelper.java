package se.izettle.localconfiguration.library;

import android.net.Uri;

import com.izettle.localconfiguration.BuildConfig;

public class ConfigProviderHelper {
    public static final String AUTHORITY = BuildConfig.AUTHORITY;

    private static final Uri configurationUri = Uri.parse("content://" + AUTHORITY + "/configuration");

    public static Uri configurationUri() {
        return configurationUri;
    }
}
