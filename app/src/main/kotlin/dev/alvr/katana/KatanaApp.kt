package dev.alvr.katana

import android.app.Application
import dev.alvr.katana.ui.main.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

internal class KatanaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KatanaApp)

            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }

            modules(appModule)
        }
    }
}
