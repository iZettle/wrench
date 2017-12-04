package com.izettle.wrench.provider;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static com.izettle.wrench.provider.WrenchApiVersion.API_1;
import static com.izettle.wrench.provider.WrenchApiVersion.API_INVALID;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({API_INVALID, API_1})
@interface WrenchApiVersion {
    int API_INVALID = 0;
    int API_1 = 1;
}
