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

import com.izettle.wrench.applicationlist.ApplicationsFragment
import com.izettle.wrench.configurationlist.ConfigurationsFragment
import com.izettle.wrench.dialogs.booleanvalue.BooleanValueFragment
import com.izettle.wrench.dialogs.enumvalue.EnumValueFragment
import com.izettle.wrench.dialogs.integervalue.IntegerValueFragment
import com.izettle.wrench.dialogs.scope.ScopeFragment
import com.izettle.wrench.dialogs.stringvalue.StringValueFragment
import com.izettle.wrench.oss.detail.OssDetailFragment
import com.izettle.wrench.oss.list.OssFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributeApplicationsFragment(): ApplicationsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeConfigurationsFragment(): ConfigurationsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeStringValueFragment(): StringValueFragment

    @ContributesAndroidInjector
    internal abstract fun contributeIntegerValueFragment(): IntegerValueFragment

    @ContributesAndroidInjector
    internal abstract fun contributeEnumValueFragment(): EnumValueFragment

    @ContributesAndroidInjector
    internal abstract fun contributeBooleanValueFragment(): BooleanValueFragment

    @ContributesAndroidInjector
    internal abstract fun contributeScopeFragment(): ScopeFragment

    @ContributesAndroidInjector
    internal abstract fun contributeOssFragment(): OssFragment

    @ContributesAndroidInjector
    internal abstract fun contributeOssDetailFragment(): OssDetailFragment

}
