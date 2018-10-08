package com.example.wrench

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.example.wrench.di.sampleAppModule
import org.koin.android.ext.android.startKoin

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(sampleAppModule))
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

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

}
