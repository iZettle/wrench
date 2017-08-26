package com.izettle.wrench.provider;

import android.content.pm.PackageManager;

public class TestPackageManagerWrapper implements IPackageManagerWrapper {
    private final String applicationLabel;
    private final String packageName;

    TestPackageManagerWrapper(String applicationLabel, String packageName) {
        this.applicationLabel = applicationLabel;
        this.packageName = packageName;
    }

    public String getApplicationLabel() throws PackageManager.NameNotFoundException {
        return applicationLabel;
    }

    public String getCallingApplicationPackageName() {
        return packageName;
    }
}
