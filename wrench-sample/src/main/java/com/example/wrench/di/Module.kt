package com.example.wrench.di

import com.example.wrench.livedataprefs.LiveDataPreferencesFragmentViewModel
import com.example.wrench.service.WrenchServiceFragmentViewModel
import com.example.wrench.wrenchprefs.WrenchPreferencesFragmentViewModel
import com.izettle.wrench.preferences.WrenchPreferences
import com.izettle.wrench.service.WrenchPreferenceProvider
import com.izettle.wrench.service.WrenchService
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val sampleAppModule = module {
    factory { WrenchPreferences(androidContext()) }
    factory { androidContext().resources }
    factory { WrenchService.with(get()) }
    factory { WrenchPreferenceProvider(get<WrenchPreferences>()) }
    viewModel { LiveDataPreferencesFragmentViewModel(androidContext()) }
    viewModel { WrenchPreferencesFragmentViewModel(get(), get()) }
    viewModel { WrenchServiceFragmentViewModel(get()) }
}