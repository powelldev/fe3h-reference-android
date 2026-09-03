package com.mrp.fe3hreference

import android.app.Application
import com.mrp.fe3hreference.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FE3HReferenceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FE3HReferenceApplication)
            modules(appModule)
        }
    }
}
