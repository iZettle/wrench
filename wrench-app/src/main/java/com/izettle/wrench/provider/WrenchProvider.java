package com.izettle.wrench.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.izettle.wrench.BuildConfig;
import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.WrenchProviderContract;
import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchApplicationDao;
import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.database.WrenchConfigurationDao;
import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchConfigurationValueDao;
import com.izettle.wrench.database.WrenchPredefinedConfigurationValue;
import com.izettle.wrench.database.WrenchPredefinedConfigurationValueDao;
import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.database.WrenchScopeDao;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.HasContentProviderInjector;

import static com.izettle.wrench.provider.WrenchApiVersion.API_1;
import static com.izettle.wrench.provider.WrenchApiVersion.API_INVALID;


public class WrenchProvider extends ContentProvider {

    private static final int CURRENT_CONFIGURATION_ID = 1;
    private static final int CURRENT_CONFIGURATION_KEY = 2;
    private static final int CURRENT_CONFIGURATIONS = 3;
    private static final int PREDEFINED_CONFIGURATION_VALUES = 5;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "currentConfiguration/#", CURRENT_CONFIGURATION_ID);
        sUriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "currentConfiguration/*", CURRENT_CONFIGURATION_KEY);
        sUriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "currentConfiguration", CURRENT_CONFIGURATIONS);
        sUriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "predefinedConfigurationValue", PREDEFINED_CONFIGURATION_VALUES);
    }

    @Inject
    WrenchApplicationDao applicationDao;

    @Inject
    WrenchScopeDao scopeDao;

    @Inject
    WrenchConfigurationDao configurationDao;

    @Inject
    WrenchConfigurationValueDao configurationValueDao;

    @Inject
    WrenchPredefinedConfigurationValueDao predefinedConfigurationDao;

    @Inject
    IPackageManagerWrapper packageManagerWrapper;

    public WrenchProvider() {
    }

    private static synchronized WrenchScope getDefaultScope(@Nullable Context context, WrenchScopeDao scopeDao, long applicationId) {
        if (context == null) {
            return null;
        }

        WrenchScope scope = scopeDao.getDefaultScope(applicationId);

        if (scope == null) {
            scope = new WrenchScope();
            scope.setApplicationId(applicationId);
            long id = scopeDao.insert(scope);
            scope.setId(id);
        }
        return scope;
    }

    private static synchronized WrenchScope getSelectedScope(@Nullable Context context, WrenchScopeDao scopeDao, long applicationId) {
        if (context == null) {
            return null;
        }

        WrenchScope scope = scopeDao.getSelectedScope(applicationId);

        if (scope == null) {
            WrenchScope defaultScope = new WrenchScope();
            defaultScope.setApplicationId(applicationId);
            defaultScope.setId(scopeDao.insert(defaultScope));

            WrenchScope customScope = new WrenchScope();
            customScope.setApplicationId(applicationId);
            customScope.setTimeStamp(new Date(defaultScope.getTimeStamp().getTime() + 1000));
            customScope.setName(WrenchScope.SCOPE_USER);
            customScope.setId(scopeDao.insert(customScope));

            scope = customScope;
        }
        return scope;
    }

    private static void assertValidApiVersion(Uri uri) {
        switch (getApiVersion(uri)) {
            case API_1: {
                return;
            }
            case WrenchApiVersion.API_INVALID:
            default: {
                throw new IllegalArgumentException("This content provider requires you to provide a valid api-version in a queryParameter");
            }
        }
    }

    @WrenchApiVersion
    private static int getApiVersion(Uri uri) {
        String queryParameter = uri.getQueryParameter(WrenchProviderContract.WRENCH_API_VERSION);
        if (queryParameter != null) {
            return Integer.valueOf(queryParameter);
        } else {
            return API_INVALID;
        }
    }

    @Nullable
    private synchronized WrenchApplication getCallingApplication(@Nullable Context context, WrenchApplicationDao applicationDao) {
        if (context == null) {
            return null;
        }

        WrenchApplication wrenchApplication = applicationDao.loadByPackageName(packageManagerWrapper.getCallingApplicationPackageName());

        if (wrenchApplication == null) {
            wrenchApplication = new WrenchApplication();
            wrenchApplication.setPackageName(packageManagerWrapper.getCallingApplicationPackageName());
            try {
                wrenchApplication.setApplicationLabel(packageManagerWrapper.getApplicationLabel());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            wrenchApplication.setId(applicationDao.insert(wrenchApplication));
        }

        return wrenchApplication;
    }

    @Override
    public boolean onCreate() {

        if (getContext().getApplicationContext() instanceof HasContentProviderInjector) {
            AndroidInjection.inject(this);
        }

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        assertValidApiVersion(uri);

        WrenchApplication callingApplication = getCallingApplication(getContext(), applicationDao);
        if (callingApplication == null) {
            return null; // for security reason we need to know the callingApplication
        }

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CURRENT_CONFIGURATION_ID: {
                WrenchScope scope = getSelectedScope(getContext(), scopeDao, callingApplication.id());
                cursor = configurationDao.getBolt(Long.valueOf(uri.getLastPathSegment()), scope.id());

                if (cursor.getCount() == 0) {
                    cursor.close();

                    WrenchScope defaultScope = getDefaultScope(getContext(), scopeDao, callingApplication.id());
                    cursor = configurationDao.getBolt(Long.valueOf(uri.getLastPathSegment()), defaultScope.id());
                }

                break;
            }
            case CURRENT_CONFIGURATION_KEY: {
                WrenchScope scope = getSelectedScope(getContext(), scopeDao, callingApplication.id());
                cursor = configurationDao.getBolt(uri.getLastPathSegment(), scope.id());

                if (cursor.getCount() == 0) {
                    cursor.close();

                    WrenchScope defaultScope = getDefaultScope(getContext(), scopeDao, callingApplication.id());
                    cursor = configurationDao.getBolt(uri.getLastPathSegment(), defaultScope.id());
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
        assertValidApiVersion(uri);

        WrenchApplication callingApplication = getCallingApplication(getContext(), applicationDao);
        if (callingApplication == null) {
            return null; // for security reason we need to know the callingApplication
        }

        long insertId;
        switch (sUriMatcher.match(uri)) {
            case CURRENT_CONFIGURATIONS: {
                Bolt bolt = Bolt.fromContentValues(values);

                WrenchConfiguration wrenchConfiguration = configurationDao.getWrenchConfiguration(callingApplication.id(), bolt.getKey());

                if (wrenchConfiguration == null) {
                    wrenchConfiguration = new WrenchConfiguration();
                    wrenchConfiguration.setApplicationId(callingApplication.id());
                    wrenchConfiguration.setKey(bolt.getKey());
                    wrenchConfiguration.setType(bolt.getType());

                    wrenchConfiguration.setId(configurationDao.insert(wrenchConfiguration));
                }

                WrenchScope defaultScope = getDefaultScope(getContext(), scopeDao, callingApplication.id());

                WrenchConfigurationValue wrenchConfigurationValue = new WrenchConfigurationValue();
                wrenchConfigurationValue.setConfigurationId(wrenchConfiguration.id());
                wrenchConfigurationValue.setValue(bolt.getValue());
                wrenchConfigurationValue.setScope(defaultScope.id());

                wrenchConfigurationValue.setId(configurationValueDao.insert(wrenchConfigurationValue));

                insertId = wrenchConfiguration.id();
                break;
            }
            case PREDEFINED_CONFIGURATION_VALUES: {
                WrenchPredefinedConfigurationValue fullConfig = WrenchPredefinedConfigurationValue.fromContentValues(values);
                insertId = predefinedConfigurationDao.insert(fullConfig);
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
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        assertValidApiVersion(uri);

        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        assertValidApiVersion(uri);

        WrenchApplication callingApplication = getCallingApplication(getContext(), applicationDao);
        if (callingApplication == null) {
            return 0; // for security reason we need to know the callingApplication
        }

        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case CURRENT_CONFIGURATION_ID: {
                Bolt bolt = Bolt.fromContentValues(values);
                WrenchScope scope = getSelectedScope(getContext(), scopeDao, callingApplication.id());
                updatedRows = configurationValueDao.updateConfigurationValue(Long.parseLong(uri.getLastPathSegment()), scope.id(), bolt.getValue());
                if (updatedRows == 0) {
                    WrenchConfigurationValue wrenchConfigurationValue = new WrenchConfigurationValue();
                    wrenchConfigurationValue.setConfigurationId(Long.parseLong(uri.getLastPathSegment()));
                    wrenchConfigurationValue.setScope(scope.id());
                    wrenchConfigurationValue.setValue(bolt.getValue());
                    configurationValueDao.insert(wrenchConfigurationValue);
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
        assertValidApiVersion(uri);

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        assertValidApiVersion(uri);

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
            case PREDEFINED_CONFIGURATION_VALUES: {
                return "vnd.android.cursor.dir/vnd." + BuildConfig.APPLICATION_ID + ".predefinedConfigurationValue";
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
    }

}
