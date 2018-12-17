package com.example.wrench

import android.app.Application
import com.example.wrench.di.sampleAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(koinApplication {
            modules(listOf(sampleAppModule))
            androidContext(this@SampleApplication)
        })
    }
}