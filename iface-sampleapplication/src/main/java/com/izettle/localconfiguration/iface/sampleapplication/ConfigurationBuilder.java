package com.izettle.localconfiguration.iface.sampleapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Proxy;

public class ConfigurationBuilder {
    ContentResolver contentResolver;

    public <T> T create(Context context, final Class<T> configuration) {
        this.contentResolver = context.getApplicationContext().getContentResolver();

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(configuration.getClassLoader(), new Class[]{configuration}, (proxy, method, args) -> {

            ConfigurationMethod configurationMethod = new ConfigurationMethod(method);

            Log.e("ohlala", method.toString());
            return null;
        });
    }
}
