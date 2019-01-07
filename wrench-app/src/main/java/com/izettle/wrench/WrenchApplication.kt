package com.izettle.wrench

import android.app.Application
import com.facebook.stetho.Stetho
import com.izettle.wrench.di.sampleAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

class WrenchApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)

            /*
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
             */
        }

        startKoin(koinApplication {
            modules(listOf(sampleAppModule))
            androidContext(this@WrenchApplication)
        })
    }
}
