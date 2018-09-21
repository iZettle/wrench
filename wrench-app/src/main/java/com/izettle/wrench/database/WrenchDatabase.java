package com.izettle.wrench.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {WrenchApplication.class, WrenchConfiguration.class, WrenchConfigurationValue.class, WrenchPredefinedConfigurationValue.class, WrenchScope.class}, version = 3)
@TypeConverters({RoomDateConverter.class})
public abstract class WrenchDatabase extends RoomDatabase {

    public abstract WrenchApplicationDao applicationDao();

    public abstract WrenchConfigurationDao configurationDao();

    public abstract WrenchConfigurationValueDao configurationValueDao();

    public abstract WrenchPredefinedConfigurationValueDao predefinedConfigurationValueDao();

    public abstract WrenchScopeDao scopeDao();


}
