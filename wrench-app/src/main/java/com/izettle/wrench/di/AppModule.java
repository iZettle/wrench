/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izettle.wrench.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.izettle.wrench.database.WrenchApplicationDao;
import com.izettle.wrench.database.WrenchConfigurationDao;
import com.izettle.wrench.database.WrenchConfigurationValueDao;
import com.izettle.wrench.database.WrenchDatabase;
import com.izettle.wrench.database.WrenchPredefinedConfigurationValueDao;
import com.izettle.wrench.database.WrenchScopeDao;
import com.izettle.wrench.database.migrations.Migrations;
import com.izettle.wrench.preferences.WrenchPreferences;
import com.izettle.wrench.provider.IPackageManagerWrapper;
import com.izettle.wrench.provider.PackageManagerWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
class AppModule {

    @Provides
    IPackageManagerWrapper providePackageManagerWrapper(Application app) {
        return new PackageManagerWrapper(app.getPackageManager());
    }

    @Provides
    @Singleton
    WrenchPreferences provideWrenchPreferences(Application app) {
        return new WrenchPreferences(app);
    }

    @Singleton
    @Provides
    WrenchDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, WrenchDatabase.class, "wrench_database.db")
                .addMigrations(Migrations.MIGRATION_1_2)
                .build();
    }

    @Singleton
    @Provides
    WrenchApplicationDao provideWrenchApplicationDao(WrenchDatabase db) {
        return db.applicationDao();
    }

    @Singleton
    @Provides
    WrenchConfigurationDao provideWrenchConfigurationDao(WrenchDatabase db) {
        return db.configurationDao();
    }

    @Singleton
    @Provides
    WrenchConfigurationValueDao provideWrenchConfigurationValueDao(WrenchDatabase db) {
        return db.configurationValueDao();
    }

    @Singleton
    @Provides
    WrenchPredefinedConfigurationValueDao provideWrenchPredefinedConfigurationValueDao(WrenchDatabase db) {
        return db.predefinedConfigurationValueDao();
    }

    @Singleton
    @Provides
    WrenchScopeDao provideWrenchScopeDao(WrenchDatabase db) {
        return db.scopeDao();
    }

}
