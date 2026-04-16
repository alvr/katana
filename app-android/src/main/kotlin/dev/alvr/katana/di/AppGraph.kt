package dev.alvr.katana.di

import android.content.Context
import dev.alvr.katana.core.common.di.AppContext
import dev.alvr.katana.shared.di.KatanaGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.android.MetroAppComponentProviders

@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
internal interface AppGraph : KatanaGraph, MetroAppComponentProviders {
    @DependencyGraph.Factory
    interface Factory {
        fun create(@AppContext @Provides context: Context): AppGraph
    }
}
