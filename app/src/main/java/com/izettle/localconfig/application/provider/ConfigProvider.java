package com.izettle.localconfig.application.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.izettle.localconfig.application.database.ConfigDatabaseHelper;
import com.izettle.localconfig.application.database.SelectionBuilder;
import com.izettle.localconfig.application.database.tables.ApplicationTable;
import com.izettle.localconfig.application.database.tables.ConfigurationTable;
import com.izettle.localconfig.application.database.tables.ConfigurationValueTable;
import com.izettle.localconfig.application.library.Application;
import com.izettle.localconfig.application.library.ApplicationConfigProviderHelper;
import com.izettle.localconfig.application.library.ApplicationCursorParser;
import com.izettle.localconfig.application.library.ConfigurationFullCursorParser;
import com.izettle.localconfiguration.ConfigProviderHelper;
import com.izettle.localconfiguration.util.ConfigurationCursorParser;


public class ConfigProvider extends ContentProvider {

    private static final int APPLICATION = 1;
    private static final int APPLICATIONS = 2;
    private static final int CONFIGURATION = 3;
    private static final int CONFIGURATIONS = 4;
    private static final int CONFIGURATION_VALUE = 5;
    private static final int CONFIGURATION_VALUES = 6;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/application/#", APPLICATION);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/application", APPLICATIONS);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/configuration/#", CONFIGURATION);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/configuration", CONFIGURATIONS);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/configurationValue/#", CONFIGURATION_VALUE);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/configurationValue", CONFIGURATION_VALUES);
    }

    private ConfigDatabaseHelper configDatabaseHelper;

    public ConfigProvider() {
    }

    private static Application getCallingApplication(@Nullable Context context, SQLiteDatabase writableDatabase) {
        if (context == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        String packageName = packageManager.getNameForUid(Binder.getCallingUid());

        Cursor cursor = null;
        try {

            cursor = new SelectionBuilder()
                    .table(ApplicationTable.TABLE_NAME)
                    .where(ApplicationCursorParser.Columns.APPLICATION_NAME + " = ?", packageName)
                    .query(writableDatabase, ApplicationCursorParser.PROJECTION, null);

            if (cursor.moveToFirst()) {
                return new ApplicationCursorParser().populateFromCursor(new Application(), cursor);
            } else {
                Application application = new Application();
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                    application.label = String.valueOf(applicationInfo.loadLabel(packageManager));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                application.applicationName = packageName;
                if (!application.isConfigApplication()) {
                    application._id = writableDatabase.insert(ApplicationTable.TABLE_NAME, null, Application.toContentValues(application));
                    context.getContentResolver().notifyChange(ApplicationConfigProviderHelper.applicationUri(application._id), null, false);
                }

                return application;
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    @Override
    public boolean onCreate() {
        configDatabaseHelper = new ConfigDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase writableDatabase = configDatabaseHelper.getWritableDatabase();

        Application callingApplication = getCallingApplication(getContext(), writableDatabase);
        if (callingApplication == null) {
            return null; // for security reason we need to know the callingApplication
        }

        SelectionBuilder selectionBuilder = new SelectionBuilder().where(selection, selectionArgs);

        Cursor cursor;


        switch (sUriMatcher.match(uri)) {
            case APPLICATION: {


                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(callingApplication._id));
                }

                cursor = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(ApplicationCursorParser.Columns._ID + " = ?", uri.getLastPathSegment())
                        .query(writableDatabase, projection, sortOrder);

                break;
            }
            case APPLICATIONS: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(callingApplication._id));
                }

                cursor = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .query(writableDatabase, projection, sortOrder);
                break;
            }
            case CONFIGURATION: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                cursor = selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(ConfigurationCursorParser.Columns._ID + " = ?", String.valueOf(uri.getLastPathSegment()))
                        .query(writableDatabase, projection, sortOrder);
                break;
            }
            case CONFIGURATIONS: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                cursor = selectionBuilder.table(ConfigurationTable.TABLE_NAME).query(writableDatabase, projection, sortOrder);
                break;
            }
            case CONFIGURATION_VALUES: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                cursor = selectionBuilder.table(ConfigurationValueTable.TABLE_NAME).query(writableDatabase, projection, sortOrder);
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
        SQLiteDatabase writableDatabase = configDatabaseHelper.getWritableDatabase();

        Application callingApplication = getCallingApplication(getContext(), writableDatabase);
        if (callingApplication == null) {
            return null; // for security reason we need to know the callingApplication
        }

        long insertId;
        switch (sUriMatcher.match(uri)) {
            case CONFIGURATIONS: {
                values.put(ConfigurationFullCursorParser.Columns.APPLICATION_ID, callingApplication._id);
                insertId = writableDatabase.insert(ConfigurationTable.TABLE_NAME, null, values);
                break;
            }
            case CONFIGURATION_VALUES: {
                insertId = writableDatabase.insert(ConfigurationValueTable.TABLE_NAME, null, values);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        getContext().getContentResolver().notifyChange(Uri.withAppendedPath(uri, String.valueOf(insertId)), null, false);

        return ContentUris.withAppendedId(uri, insertId);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase writableDatabase = configDatabaseHelper.getWritableDatabase();

        Application callingApplication = getCallingApplication(getContext(), writableDatabase);
        if (callingApplication == null) {
            return 0; // for security reason we need to know the callingApplication
        }

        SelectionBuilder selectionBuilder = new SelectionBuilder().where(selection, selectionArgs);

        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case APPLICATION: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(uri.getLastPathSegment())).update(writableDatabase, values);

                break;
            }
            case APPLICATIONS: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME).update(writableDatabase, values);
                break;
            }
            case CONFIGURATION: {
                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(ConfigurationCursorParser.Columns._ID + " = ?", String.valueOf(uri.getLastPathSegment())).update(writableDatabase, values);
                break;
            }
            case CONFIGURATIONS: {
                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ConfigurationTable.TABLE_NAME).update(writableDatabase, values);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        if (updatedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null, false);
        }

        return updatedRows;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase writableDatabase = configDatabaseHelper.getWritableDatabase();

        Application callingApplication = getCallingApplication(getContext(), writableDatabase);
        if (callingApplication == null) {
            return 0; // for security reason we need to know the callingApplication
        }

        SelectionBuilder selectionBuilder = new SelectionBuilder().where(selection, selectionArgs);

        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case APPLICATION: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(uri.getLastPathSegment())).delete(writableDatabase);

                break;
            }
            case APPLICATIONS: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ApplicationCursorParser.Columns._ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME).delete(writableDatabase);
                break;
            }
            case CONFIGURATION: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(ConfigurationCursorParser.Columns._ID + " = ?", String.valueOf(uri.getLastPathSegment()))
                        .delete(writableDatabase);
                break;
            }
            case CONFIGURATIONS: {

                if (!callingApplication.isConfigApplication()) {
                    selectionBuilder.where(ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                updatedRows = selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(writableDatabase);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null, false);

        return updatedRows;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
