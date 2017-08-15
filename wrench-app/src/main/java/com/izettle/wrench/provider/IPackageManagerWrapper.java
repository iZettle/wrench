package com.izettle.wrench.provider;

import android.content.pm.PackageManager;

interface IPackageManagerWrapper {
    String getApplicationLabel() throws PackageManager.NameNotFoundException;

    String getCallingApplicationPackageName();
}
