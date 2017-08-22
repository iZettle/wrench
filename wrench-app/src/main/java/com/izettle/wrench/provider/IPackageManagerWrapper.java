package com.izettle.wrench.provider;

import android.content.pm.PackageManager;

public interface IPackageManagerWrapper {
    String getApplicationLabel() throws PackageManager.NameNotFoundException;

    String getCallingApplicationPackageName();
}
