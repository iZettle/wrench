package com.izettle.wrench.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {WrenchApplication.class, WrenchConfiguration.class, WrenchConfigurationValue.class, WrenchPredefinedConfigurationValue.class, WrenchScope.class}, version = 1)
@TypeConverters({RoomDateConverter.class})
public abstract class WrenchDatabase extends RoomDatabase {

    public static WrenchDatabase getDatabase(Context context) {
        if (Singleton.INSTANCE.wrenchDatabase == null) {
            Singleton.INSTANCE.wrenchDatabase = Room.databaseBuilder(context.getApplicationContext(), WrenchDatabase.class, "wrench_database.db").build();
        }

        return Singleton.INSTANCE.wrenchDatabase;
    }

    public abstract WrenchApplicationDao applicationDao();

    public abstract WrenchConfigurationDao configurationDao();

    public abstract WrenchConfigurationValueDao configurationValueDao();

    public abstract WrenchPredefinedConfigurationValueDao predefinedConfigurationValueDao();

    public abstract WrenchScopeDao scopeDao();

    private enum Singleton {
        INSTANCE;
        WrenchDatabase wrenchDatabase;
    }

}
