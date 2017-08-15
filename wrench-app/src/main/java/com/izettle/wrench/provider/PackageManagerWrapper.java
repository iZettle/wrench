package com.izettle.wrench.provider;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;

public class PackageManagerWrapper implements IPackageManagerWrapper {
    private final PackageManager packageManager;

    PackageManagerWrapper(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public String getApplicationLabel() throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getCallingApplicationPackageName(), PackageManager.GET_META_DATA);
        return String.valueOf(applicationInfo.loadLabel(packageManager));
    }

    public String getCallingApplicationPackageName() {
        return packageManager.getNameForUid(Binder.getCallingUid());
    }
}
