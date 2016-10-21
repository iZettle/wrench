package se.eelde.localconfig.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import se.eelde.localconfig.BuildConfig;
import se.eelde.localconfig.database.ConfigDatabaseHelper;
import se.eelde.localconfig.database.SelectionBuilder;
import se.eelde.localconfig.database.tables.ApplicationTable;
import se.eelde.localconfig.database.tables.ConfigurationTable;
import se.eelde.localconfiguration.library.Application;
import se.eelde.localconfiguration.library.ConfigProviderHelper;
import se.eelde.localconfiguration.library.Configuration;

public class ConfigProvider extends ContentProvider {

    private static final int APPLICATION = 1;
    private static final int APPLICATIONS = 2;
    private static final int CONFIGURATION = 3;
    private static final int CONFIGURATIONS = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/application/#", APPLICATION);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/application", APPLICATIONS);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/configuration/#", CONFIGURATION);
        sUriMatcher.addURI(ConfigProviderHelper.AUTHORITY, "/configuration", CONFIGURATIONS);
    }

    private ConfigDatabaseHelper configDatabaseHelper;

    public ConfigProvider() {
    }

    private static Application getCallingApplication(Context context, SQLiteDatabase writableDatabase) {
        String packageName = context.getPackageManager().getNameForUid(Binder.getCallingUid());
        Cursor cursor = null;
        try {

            cursor = new SelectionBuilder()
                    .table(ApplicationTable.TABLE_NAME)
                    .where(Application.Columns.APPLICATION_NAME + " = ?", packageName)
                    .query(writableDatabase, Application.PROJECTION, null);

            if (cursor.moveToFirst()) {
                return Application.applicationFromCursor(cursor);
            } else {
                Application application = new Application();
                application.applicationName = packageName;
                application._id = writableDatabase.insert(ApplicationTable.TABLE_NAME, null, Application.toContentValues(application));
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
        SelectionBuilder selectionBuilder = new SelectionBuilder();
        SQLiteDatabase writableDatabase = configDatabaseHelper.getWritableDatabase();

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case APPLICATION: {
                cursor = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(se.eelde.localconfiguration.library.Application.Columns._ID + " = ?", new String[]{String.valueOf(uri.getLastPathSegment())})
                        .where(selection, selectionArgs)
                        .query(writableDatabase, projection, sortOrder);
                break;
            }
            case APPLICATIONS: {
                cursor = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .query(writableDatabase, projection, sortOrder);
                break;
            }
            case CONFIGURATION: {
                selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(Configuration.Columns._ID + " = ?", String.valueOf(uri.getLastPathSegment()))
                        .where(selection, selectionArgs);

                Application callingApplication = getCallingApplication(getContext(), writableDatabase);
                if (!TextUtils.equals(callingApplication.applicationName, BuildConfig.APPLICATION_ID)) {
                    selectionBuilder.where(Configuration.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                cursor = selectionBuilder.query(writableDatabase, projection, sortOrder);
                break;
            }
            case CONFIGURATIONS: {
                selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(selection, selectionArgs);

                Application callingApplication = getCallingApplication(getContext(), writableDatabase);
                if (!TextUtils.equals(callingApplication.applicationName, BuildConfig.APPLICATION_ID)) {
                    selectionBuilder.where(Configuration.Columns.APPLICATION_ID + " = ?", String.valueOf(callingApplication._id));
                }

                cursor = selectionBuilder.query(writableDatabase, projection, sortOrder);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
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

        long insertId;
        switch (sUriMatcher.match(uri)) {
            case CONFIGURATIONS: {

                values.put(Configuration.Columns.APPLICATION_ID, getCallingApplication(getContext(), writableDatabase)._id);

                insertId = writableDatabase.insert(ConfigurationTable.TABLE_NAME, null, values);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null, false);

        return ContentUris.withAppendedId(uri, insertId);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder selectionBuilder = new SelectionBuilder();
        SQLiteDatabase writableDatabase = configDatabaseHelper.getWritableDatabase();

        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case APPLICATION: {
                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(Application.Columns._ID + " = ?", new String[]{String.valueOf(uri.getLastPathSegment())})
                        .where(selection, selectionArgs)
                        .update(writableDatabase, values);

                break;
            }
            case APPLICATIONS: {
                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(writableDatabase, values);
                break;
            }
            case CONFIGURATION: {
                updatedRows = selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(Configuration.Columns._ID + " = ?", new String[]{String.valueOf(uri.getLastPathSegment())})
                        .where(selection, selectionArgs)
                        .update(writableDatabase, values);
                break;
            }
            case CONFIGURATIONS: {
                updatedRows = selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(writableDatabase, values);
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
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder selectionBuilder = new SelectionBuilder();
        SQLiteDatabase writableDatabase = configDatabaseHelper.getWritableDatabase();

        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case APPLICATION: {
                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(Application.Columns._ID + " = ?", new String[]{String.valueOf(uri.getLastPathSegment())})
                        .where(selection, selectionArgs)
                        .delete(writableDatabase);

                break;
            }
            case APPLICATIONS: {
                updatedRows = selectionBuilder.table(ApplicationTable.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(writableDatabase);
                break;
            }
            case CONFIGURATION: {
                updatedRows = selectionBuilder.table(ConfigurationTable.TABLE_NAME)
                        .where(Configuration.Columns._ID + " = ?", new String[]{String.valueOf(uri.getLastPathSegment())})
                        .where(selection, selectionArgs)
                        .delete(writableDatabase);
                break;
            }
            case CONFIGURATIONS: {
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
