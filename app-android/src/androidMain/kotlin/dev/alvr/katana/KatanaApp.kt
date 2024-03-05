package dev.alvr.katana

import android.app.Application
import android.os.Build
import android.os.StrictMode
import dev.alvr.katana.shared.di.AndroidApplicationComponent
import dev.alvr.katana.shared.di.create
import dev.alvr.katana.shared.di.katanaModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

internal class KatanaApp : Application() {

    internal val component by lazy(LazyThreadSafetyMode.NONE) {
        AndroidApplicationComponent.create(this)
    }

    override fun onCreate() {
        super.onCreate()
        setupStrictMode()

        startKoin {
            androidContext(this@KatanaApp)
            androidLogger(if (KatanaBuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            modules(katanaModule)
        }
    }

    private fun setupStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build(),
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectActivityLeaks()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                .detectFileUriExposure()
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        detectCleartextNetwork()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        detectContentUriWithoutPermission()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        detectCredentialProtectedWhileLocked()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        detectIncorrectContextUse()
                        detectUnsafeIntentLaunch()
                    }
                }
                .penaltyLog()
                .build(),
        )
    }
}
