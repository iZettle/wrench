package com.example.wrench.di

import com.example.wrench.livedataprefs.LiveDataPreferencesFragmentViewModel
import com.example.wrench.wrenchprefs.WrenchPreferencesFragmentViewModel
import com.izettle.wrench.preferences.WrenchPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val sampleAppModule = module {
    factory { WrenchPreferences(androidContext()) }
    factory { androidContext().resources }
    viewModel { LiveDataPreferencesFragmentViewModel(androidContext()) }
    viewModel { WrenchPreferencesFragmentViewModel(get(), get()) }
}