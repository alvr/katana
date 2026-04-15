package dev.alvr.katana

import android.app.Application
import dev.alvr.katana.shared.di.AppGraph
import dev.zacsweers.metro.createGraphFactory

internal class KatanaApp : Application() {
    val appGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }
}
