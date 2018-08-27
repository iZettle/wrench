package com.izettle.wrench.di;

import com.izettle.wrench.applicationlist.ApplicationViewModel;
import com.izettle.wrench.configurationlist.ConfigurationViewModel;
import com.izettle.wrench.dialogs.booleanvalue.FragmentBooleanValueViewModel;
import com.izettle.wrench.dialogs.enumvalue.FragmentEnumValueViewModel;
import com.izettle.wrench.dialogs.integervalue.FragmentIntegerValueViewModel;
import com.izettle.wrench.dialogs.scope.ScopeFragmentViewModel;
import com.izettle.wrench.dialogs.stringvalue.FragmentStringValueViewModel;
import com.izettle.wrench.oss.detail.OssDetailViewModel;
import com.izettle.wrench.oss.list.OssListViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ApplicationViewModel.class)
    abstract ViewModel bindApplicationViewModel(ApplicationViewModel applicationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ConfigurationViewModel.class)
    abstract ViewModel bindConfigurationViewModel(ConfigurationViewModel configurationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FragmentStringValueViewModel.class)
    abstract ViewModel bindFragmentStringValueViewModel(FragmentStringValueViewModel fragmentStringValueViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ScopeFragmentViewModel.class)
    abstract ViewModel bindScopeFragmentViewModel(ScopeFragmentViewModel scopeFragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FragmentIntegerValueViewModel.class)
    abstract ViewModel bindFragmentIntegerValueViewModel(FragmentIntegerValueViewModel fragmentIntegerValueViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FragmentEnumValueViewModel.class)
    abstract ViewModel bindFragmentEnumValueViewModel(FragmentEnumValueViewModel fragmentEnumValueViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FragmentBooleanValueViewModel.class)
    abstract ViewModel bindFragmentBooleanValueViewModel(FragmentBooleanValueViewModel fragmentBooleanValueViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OssListViewModel.class)
    abstract ViewModel bindOssViewModel(OssListViewModel ossListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OssDetailViewModel.class)
    abstract ViewModel bindOssDetailViewModel(OssDetailViewModel ossDetailViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
