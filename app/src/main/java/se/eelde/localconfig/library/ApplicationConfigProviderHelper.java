package se.eelde.localconfig.library;


import android.content.ContentUris;
import android.net.Uri;

import se.eelde.localconfiguration.library.ConfigProviderHelper;

public class ApplicationConfigProviderHelper extends ConfigProviderHelper {
    private static final Uri applicationUri = Uri.parse("content://" + AUTHORITY + "/application");

    public static Uri applicationUri() {
        return applicationUri;
    }

    public static Uri applicationUri(long id) {
        return ContentUris.withAppendedId(applicationUri, id);
    }


}
