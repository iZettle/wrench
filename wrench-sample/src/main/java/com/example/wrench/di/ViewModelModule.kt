package com.example.wrench.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.example.wrench.livedataprefs.LiveDataPreferencesFragmentViewModel
import com.example.wrench.service.WrenchServiceFragmentViewModel
import com.example.wrench.wrenchprefs.WrenchPreferencesFragmentViewModel

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LiveDataPreferencesFragmentViewModel::class)
    internal abstract fun bindLiveDataPreferencesFragmentViewModel(liveDataPreferencesFragmentViewModel: LiveDataPreferencesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WrenchPreferencesFragmentViewModel::class)
    internal abstract fun bindWrenchPreferencesFragmentViewModel(wrenchPreferencesFragmentViewModel: WrenchPreferencesFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WrenchServiceFragmentViewModel::class)
    internal abstract fun bindWrenchServiceFragmentViewModel(wrenchServiceFragmentViewModel: WrenchServiceFragmentViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
