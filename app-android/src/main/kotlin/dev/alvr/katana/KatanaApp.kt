package dev.alvr.katana

import android.app.Application
import android.os.StrictMode
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.di.AppGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroApplication

internal class KatanaApp : Application(), MetroApplication {
    override val appComponentProviders by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

    override fun onCreate() {
        super.onCreate()

        if (KatanaBuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build())
        }
    }
}
