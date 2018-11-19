package com.example.wrench

import android.app.Application
import com.example.wrench.di.sampleAppModule
import org.koin.android.ext.android.startKoin

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(sampleAppModule))
    }

}
