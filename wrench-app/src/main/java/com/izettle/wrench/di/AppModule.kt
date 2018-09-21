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

package com.izettle.wrench.di

import android.app.Application
import androidx.room.Room
import com.izettle.wrench.database.*
import com.izettle.wrench.database.migrations.Migrations
import com.izettle.wrench.preferences.WrenchPreferences
import com.izettle.wrench.provider.IPackageManagerWrapper
import com.izettle.wrench.provider.PackageManagerWrapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
internal class AppModule {

    @Provides
    fun providePackageManagerWrapper(app: Application): IPackageManagerWrapper {
        return PackageManagerWrapper(app.packageManager)
    }

    @Provides
    @Singleton
    fun provideWrenchPreferences(app: Application): WrenchPreferences {
        return WrenchPreferences(app)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): WrenchDatabase {
        return Room.databaseBuilder(app, WrenchDatabase::class.java, "wrench_database.db")
                .addMigrations(Migrations.MIGRATION_1_2)
                .addMigrations(Migrations.MIGRATION_2_3)
                .build()
    }

    @Provides
    fun provideWrenchApplicationDao(db: WrenchDatabase): WrenchApplicationDao {
        return db.applicationDao()
    }

    @Provides
    fun provideWrenchConfigurationDao(db: WrenchDatabase): WrenchConfigurationDao {
        return db.configurationDao()
    }

    @Provides
    fun provideWrenchConfigurationValueDao(db: WrenchDatabase): WrenchConfigurationValueDao {
        return db.configurationValueDao()
    }

    @Provides
    fun provideWrenchPredefinedConfigurationValueDao(db: WrenchDatabase): WrenchPredefinedConfigurationValueDao {
        return db.predefinedConfigurationValueDao()
    }

    @Provides
    fun provideWrenchScopeDao(db: WrenchDatabase): WrenchScopeDao {
        return db.scopeDao()
    }

}
