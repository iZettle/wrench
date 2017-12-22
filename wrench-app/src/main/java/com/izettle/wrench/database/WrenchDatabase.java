package com.izettle.wrench.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {WrenchApplication.class, WrenchConfiguration.class, WrenchConfigurationValue.class, WrenchPredefinedConfigurationValue.class, WrenchScope.class}, version = 3)
@TypeConverters({RoomDateConverter.class})
public abstract class WrenchDatabase extends RoomDatabase {

    public abstract WrenchApplicationDao applicationDao();

    public abstract WrenchConfigurationDao configurationDao();

    public abstract WrenchConfigurationValueDao configurationValueDao();

    public abstract WrenchPredefinedConfigurationValueDao predefinedConfigurationValueDao();

    public abstract WrenchScopeDao scopeDao();


}
