package dev.alvr.katana

import android.app.Application
import dev.alvr.katana.di.AppGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroApplication

internal class KatanaApp : Application(), MetroApplication {
    override val appComponentProviders by lazy { createGraphFactory<AppGraph.Factory>().create(this) }
}
