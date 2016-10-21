package se.eelde.localconfig;

import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

public class LocalConfigurationApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
