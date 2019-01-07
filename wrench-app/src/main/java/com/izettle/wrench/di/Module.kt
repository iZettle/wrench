package com.izettle.wrench.di

import androidx.room.Room
import com.izettle.wrench.applicationlist.ApplicationViewModel
import com.izettle.wrench.configurationlist.ConfigurationViewModel
import com.izettle.wrench.database.WrenchDatabase
import com.izettle.wrench.database.migrations.Migrations
import com.izettle.wrench.dialogs.booleanvalue.FragmentBooleanValueViewModel
import com.izettle.wrench.dialogs.enumvalue.FragmentEnumValueViewModel
import com.izettle.wrench.dialogs.integervalue.FragmentIntegerValueViewModel
import com.izettle.wrench.dialogs.scope.ScopeFragmentViewModel
import com.izettle.wrench.dialogs.stringvalue.FragmentStringValueViewModel
import com.izettle.wrench.oss.detail.OssDetailViewModel
import com.izettle.wrench.oss.list.OssListViewModel
import com.izettle.wrench.preferences.WrenchPreferences
import com.izettle.wrench.provider.IPackageManagerWrapper
import com.izettle.wrench.provider.PackageManagerWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sampleAppModule = module {
    single { PackageManagerWrapper(androidContext().packageManager) as IPackageManagerWrapper }
    single { WrenchPreferences(androidContext()) }
    single {
        Room.databaseBuilder(androidContext(), WrenchDatabase::class.java, "wrench_database.db")
                .addMigrations(Migrations.MIGRATION_1_2)
                .addMigrations(Migrations.MIGRATION_2_3)
                .build()
    }

    single { (get() as WrenchDatabase).applicationDao() }
    single { (get() as WrenchDatabase).configurationDao() }
    single { (get() as WrenchDatabase).configurationValueDao() }
    single { (get() as WrenchDatabase).predefinedConfigurationValueDao() }
    single { (get() as WrenchDatabase).scopeDao() }

    viewModel { ApplicationViewModel(get()) }
    viewModel { ConfigurationViewModel(get(), get(), get()) }
    viewModel { FragmentStringValueViewModel(get(), get()) }
    viewModel { ScopeFragmentViewModel(get()) }
    viewModel { FragmentIntegerValueViewModel(get(), get()) }
    viewModel { FragmentEnumValueViewModel(get(), get(), get()) }
    viewModel { FragmentBooleanValueViewModel(get(), get()) }
    viewModel { OssListViewModel(androidContext()) }
    viewModel { OssDetailViewModel(androidContext()) }
}