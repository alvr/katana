package dev.alvr.katana.shared.di

import android.content.Context
import dev.alvr.katana.core.common.di.AppContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
interface AppGraph : KatanaGraph {

    @DependencyGraph.Factory
    interface Factory {
        fun create(@AppContext @Provides context: Context): AppGraph
    }
}
