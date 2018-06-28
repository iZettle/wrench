package com.example.wrench.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.example.wrench.livedataprefs.LiveDataPreferencesFragmentViewModel
import com.example.wrench.wrenchprefs.WrenchPreferencesFragmentViewModel

import dagger.Binds
import dagger.Module
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
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
