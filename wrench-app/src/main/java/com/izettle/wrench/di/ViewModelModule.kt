package com.izettle.wrench.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.izettle.wrench.applicationlist.ApplicationViewModel
import com.izettle.wrench.configurationlist.ConfigurationViewModel
import com.izettle.wrench.dialogs.booleanvalue.FragmentBooleanValueViewModel
import com.izettle.wrench.dialogs.enumvalue.FragmentEnumValueViewModel
import com.izettle.wrench.dialogs.integervalue.FragmentIntegerValueViewModel
import com.izettle.wrench.dialogs.scope.ScopeFragmentViewModel
import com.izettle.wrench.dialogs.stringvalue.FragmentStringValueViewModel
import com.izettle.wrench.oss.detail.OssDetailViewModel
import com.izettle.wrench.oss.list.OssListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ApplicationViewModel::class)
    internal abstract fun bindApplicationViewModel(applicationViewModel: ApplicationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfigurationViewModel::class)
    internal abstract fun bindConfigurationViewModel(configurationViewModel: ConfigurationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentStringValueViewModel::class)
    internal abstract fun bindFragmentStringValueViewModel(fragmentStringValueViewModel: FragmentStringValueViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScopeFragmentViewModel::class)
    internal abstract fun bindScopeFragmentViewModel(scopeFragmentViewModel: ScopeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentIntegerValueViewModel::class)
    internal abstract fun bindFragmentIntegerValueViewModel(fragmentIntegerValueViewModel: FragmentIntegerValueViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentEnumValueViewModel::class)
    internal abstract fun bindFragmentEnumValueViewModel(fragmentEnumValueViewModel: FragmentEnumValueViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentBooleanValueViewModel::class)
    internal abstract fun bindFragmentBooleanValueViewModel(fragmentBooleanValueViewModel: FragmentBooleanValueViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OssListViewModel::class)
    internal abstract fun bindOssViewModel(ossListViewModel: OssListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OssDetailViewModel::class)
    internal abstract fun bindOssDetailViewModel(ossDetailViewModel: OssDetailViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
