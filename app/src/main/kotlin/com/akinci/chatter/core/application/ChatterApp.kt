package com.akinci.chatter.core.application

import android.app.Application
import com.akinci.chatter.core.logger.LoggerInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ChatterApp : Application() {
    @Inject
    lateinit var loggerInitializer: LoggerInitializer

    override fun onCreate() {
        super.onCreate()

        // initialize timber trees
        loggerInitializer.initialize()
    }
}