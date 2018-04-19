package com.example.wrench.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.wrench.WrenchSampleViewModel;
import com.example.wrench.livedataprefs.LiveDataPreferencesFragmentViewModel;
import com.example.wrench.wrenchprefs.WrenchPreferencesFragmentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(WrenchSampleViewModel.class)
    abstract ViewModel bindSampleViewModel(WrenchSampleViewModel sampleViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LiveDataPreferencesFragmentViewModel.class)
    abstract ViewModel bindLiveDataPreferencesFragmentViewModel(LiveDataPreferencesFragmentViewModel liveDataPreferencesFragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WrenchPreferencesFragmentViewModel.class)
    abstract ViewModel bindWrenchPreferencesFragmentViewModel(WrenchPreferencesFragmentViewModel wrenchPreferencesFragmentViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
