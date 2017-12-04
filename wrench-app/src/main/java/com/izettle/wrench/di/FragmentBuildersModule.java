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

package com.izettle.wrench.di;

import com.izettle.wrench.ConfigurationsFragment;
import com.izettle.wrench.applicationlist.ApplicationsFragment;
import com.izettle.wrench.dialogs.booleanvalue.BooleanValueFragment;
import com.izettle.wrench.dialogs.enumvalue.EnumValueFragment;
import com.izettle.wrench.dialogs.integervalue.IntegerValueFragment;
import com.izettle.wrench.dialogs.scope.ScopeFragment;
import com.izettle.wrench.dialogs.stringvalue.StringValueFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract ApplicationsFragment contributeApplicationsFragment();

    @ContributesAndroidInjector
    abstract ConfigurationsFragment contributeConfigurationsFragment();

    @ContributesAndroidInjector
    abstract StringValueFragment contributeStringValueFragment();

    @ContributesAndroidInjector
    abstract IntegerValueFragment contributeIntegerValueFragment();

    @ContributesAndroidInjector
    abstract EnumValueFragment contributeEnumValueFragment();

    @ContributesAndroidInjector
    abstract BooleanValueFragment contributeBooleanValueFragment();

    @ContributesAndroidInjector
    abstract ScopeFragment contributeScopeFragment();

}
