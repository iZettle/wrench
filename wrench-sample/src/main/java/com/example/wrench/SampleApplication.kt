package com.example.wrench

import android.content.Context
import android.os.StrictMode
import com.example.wrench.di.AppInjector
import com.example.wrench.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class SampleApplication : DaggerApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        AppInjector.init(this)

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build())

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
