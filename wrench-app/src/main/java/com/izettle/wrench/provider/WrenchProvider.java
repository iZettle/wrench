package com.izettle.wrench.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.izettle.wrench.BuildConfig;
import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.ConfigProviderContract;
import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchDatabase;
import com.izettle.wrench.database.WrenchPredefinedConfigurationValue;
import com.izettle.wrench.database.WrenchScope;

import java.util.Date;


public class WrenchProvider extends ContentProvider {

    private static final int CURRENT_CONFIGURATION_ID = 1;
    private static final int CURRENT_CONFIGURATION_KEY = 2;
    private static final int CURRENT_CONFIGURATIONS = 3;
    private static final int PREDEFINED_CONFIGURATION_VALUE = 4;
    private static final int PREDEFINED_CONFIGURATION_VALUES = 5;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sUriMatcher.addURI(ConfigProviderContract.WRENCH_AUTHORITY, "currentConfiguration/#", CURRENT_CONFIGURATION_ID);
        sUriMatcher.addURI(ConfigProviderContract.WRENCH_AUTHORITY, "currentConfiguration/*", CURRENT_CONFIGURATION_KEY);
        sUriMatcher.addURI(ConfigProviderContract.WRENCH_AUTHORITY, "currentConfiguration", CURRENT_CONFIGURATIONS);
        sUriMatcher.addURI(ConfigProviderContract.WRENCH_AUTHORITY, "predefinedConfigurationValue/*", PREDEFINED_CONFIGURATION_VALUE);
        sUriMatcher.addURI(ConfigProviderContract.WRENCH_AUTHORITY, "predefinedConfigurationValue", PREDEFINED_CONFIGURATION_VALUES);
    }

    private WrenchDatabase wrenchDatabase;

    public WrenchProvider() {
    }

    private static synchronized WrenchScope getDefaultScope(@Nullable Context context, WrenchDatabase wrenchDatabase, long applicationId) {
        if (context == null) {
            return null;
        }

        WrenchScope scope = wrenchDatabase.scopeDao().getDefaultScope(applicationId);

        if (scope == null) {
            scope = new WrenchScope();
            scope.setApplicationId(applicationId);
            long id = wrenchDatabase.scopeDao().insert(scope);
            scope.setId(id);
        }
        return scope;
    }

    private static synchronized WrenchScope getSelectedScope(@Nullable Context context, WrenchDatabase wrenchDatabase, long applicationId) {
        if (context == null) {
            return null;
        }

        WrenchScope scope = wrenchDatabase.scopeDao().getSelectedScope(applicationId);

        if (scope == null) {
            WrenchScope defaultScope = new WrenchScope();
            defaultScope.setApplicationId(applicationId);
            defaultScope.setId(wrenchDatabase.scopeDao().insert(defaultScope));

            WrenchScope customScope = new WrenchScope();
            customScope.setApplicationId(applicationId);
            customScope.setTimeStamp(new Date(defaultScope.getTimeStamp().getTime() + 1000));
            customScope.setName(WrenchScope.SCOPE_USER);
            customScope.setId(wrenchDatabase.scopeDao().insert(customScope));

            scope = customScope;
        }
        return scope;
    }


    @Nullable
    private static synchronized WrenchApplication getCallingApplication(@Nullable Context context, WrenchDatabase wrenchDatabase) {
        if (context == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        String packageName = packageManager.getNameForUid(Binder.getCallingUid());

        WrenchApplication wrenchApplication = wrenchDatabase.applicationDao().loadByPackageName(packageName);

        if (wrenchApplication == null) {
            wrenchApplication = new WrenchApplication();
            wrenchApplication.setPackageName(packageName);
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                wrenchApplication.setApplicationLabel(String.valueOf(applicationInfo.loadLabel(packageManager)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            wrenchApplication.setId(wrenchDatabase.applicationDao().insert(wrenchApplication));
        }

        return wrenchApplication;
    }

    @Override
    public boolean onCreate() {
        wrenchDatabase = WrenchDatabase.getDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        WrenchApplication callingApplication = getCallingApplication(getContext(), wrenchDatabase);
        if (callingApplication == null) {
            return null; // for security reason we need to know the callingApplication
        }

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CURRENT_CONFIGURATION_ID: {
                WrenchScope scope = getSelectedScope(getContext(), wrenchDatabase, callingApplication.id());
                cursor = wrenchDatabase.configurationDao().getBolt(Long.valueOf(uri.getLastPathSegment()), scope.id());

                if (cursor.getCount() == 0) {
                    cursor.close();

                    WrenchScope defaultScope = getDefaultScope(getContext(), wrenchDatabase, callingApplication.id());
                    cursor = wrenchDatabase.configurationDao().getBolt(Long.valueOf(uri.getLastPathSegment()), defaultScope.id());
                }

                break;
            }
            case CURRENT_CONFIGURATION_KEY: {
                WrenchScope scope = getSelectedScope(getContext(), wrenchDatabase, callingApplication.id());
                cursor = wrenchDatabase.configurationDao().getBolt(uri.getLastPathSegment(), scope.id());

                if (cursor.getCount() == 0) {
                    cursor.close();

                    WrenchScope defaultScope = getDefaultScope(getContext(), wrenchDatabase, callingApplication.id());
                    cursor = wrenchDatabase.configurationDao().getBolt(Uri.decode(uri.getLastPathSegment()), defaultScope.id());
                }

                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented " + uri.toString());
            }
        }

        if (cursor != null && getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        WrenchApplication callingApplication = getCallingApplication(getContext(), wrenchDatabase);
        if (callingApplication == null) {
            return null; // for security reason we need to know the callingApplication
        }

        long insertId;
        switch (sUriMatcher.match(uri)) {
            case CURRENT_CONFIGURATIONS: {
                Bolt bolt = Bolt.fromContentValues(values);

                WrenchConfiguration wrenchConfiguration = wrenchDatabase.configurationDao().getWrenchConfiguration(callingApplication.id(), bolt.key);

                if (wrenchConfiguration == null) {
                    wrenchConfiguration = new WrenchConfiguration();
                    wrenchConfiguration.setApplicationId(callingApplication.id());
                    wrenchConfiguration.setKey(bolt.key);
                    wrenchConfiguration.setType(bolt.type);

                    wrenchConfiguration.setId(wrenchDatabase.configurationDao().insert(wrenchConfiguration));
                }

                WrenchScope defaultScope = getDefaultScope(getContext(), wrenchDatabase, callingApplication.id());

                WrenchConfigurationValue wrenchConfigurationValue = new WrenchConfigurationValue();
                wrenchConfigurationValue.setConfigurationId(wrenchConfiguration.id());
                wrenchConfigurationValue.setValue(bolt.value);
                wrenchConfigurationValue.setScope(defaultScope.id());

                wrenchConfigurationValue.setId(wrenchDatabase.configurationValueDao().insert(wrenchConfigurationValue));

                insertId = wrenchConfiguration.id();
                break;
            }
            case PREDEFINED_CONFIGURATION_VALUES: {
                WrenchPredefinedConfigurationValue fullConfig = WrenchPredefinedConfigurationValue.fromContentValues(values);
                insertId = wrenchDatabase.predefinedConfigurationValueDao().insert(fullConfig);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(Uri.withAppendedPath(uri, String.valueOf(insertId)), null, false);

        return ContentUris.withAppendedId(uri, insertId);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        WrenchApplication callingApplication = getCallingApplication(getContext(), wrenchDatabase);
        if (callingApplication == null) {
            return 0; // for security reason we need to know the callingApplication
        }

        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case CURRENT_CONFIGURATION_ID: {
                Bolt bolt = Bolt.fromContentValues(values);
                WrenchScope scope = getSelectedScope(getContext(), wrenchDatabase, callingApplication.id());
                updatedRows = wrenchDatabase.configurationValueDao().updateConfigurationValue(Long.parseLong(uri.getLastPathSegment()), scope.id(), bolt.value);
                if (updatedRows == 0) {
                    WrenchConfigurationValue wrenchConfigurationValue = new WrenchConfigurationValue();
                    wrenchConfigurationValue.setConfigurationId(Long.parseLong(uri.getLastPathSegment()));
                    wrenchConfigurationValue.setScope(scope.id());
                    wrenchConfigurationValue.setValue(bolt.value);
                    wrenchDatabase.configurationValueDao().insert(wrenchConfigurationValue);
                }

                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented " + uri);
            }
        }

        if (updatedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null, false);
        }

        return updatedRows;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CURRENT_CONFIGURATIONS: {
                return "vnd.android.cursor.dir/vnd." + BuildConfig.APPLICATION_ID + ".currentConfiguration";
            }
            case CURRENT_CONFIGURATION_ID: {
                return "vnd.android.cursor.item/vnd." + BuildConfig.APPLICATION_ID + ".currentConfiguration";
            }
            case CURRENT_CONFIGURATION_KEY: {
                return "vnd.android.cursor.dir/vnd." + BuildConfig.APPLICATION_ID + ".currentConfiguration";
            }
            case PREDEFINED_CONFIGURATION_VALUE: {
                return "vnd.android.cursor.item/vnd." + BuildConfig.APPLICATION_ID + ".predefinedConfigurationValue";
            }
            case PREDEFINED_CONFIGURATION_VALUES: {
                return "vnd.android.cursor.dir/vnd." + BuildConfig.APPLICATION_ID + ".predefinedConfigurationValue";
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
    }

}
