package se.eelde.localconfiguration.library;

import android.content.ContentUris;
import android.net.Uri;

public class ConfigProviderHelper {
    public static final String AUTHORITY = BuildConfig.AUTHORITY;

    private static final Uri applicationUri = Uri.parse("content://" + AUTHORITY + "/application");
    private static final Uri configurationUri = Uri.parse("content://" + AUTHORITY + "/configuration");

    public static Uri applicationUri() {
        return applicationUri;
    }

    public static Uri applicationUri(long id) {
        return ContentUris.withAppendedId(applicationUri, id);
    }

    public static Uri configurationUri() {
        return configurationUri;
    }

    public static Uri configurationUri(long id) {
        return ContentUris.withAppendedId(configurationUri, id);
    }
}
