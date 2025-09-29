package com.minesweeper

import android.app.Application
import com.minesweeper.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber


class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree());

        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels / displayMetrics.density
        val width = displayMetrics.widthPixels / displayMetrics.density

        Timber.i("Application $width $height")
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MainApplication)
            // Load modules
            modules(androidModule)
        }
    }
}