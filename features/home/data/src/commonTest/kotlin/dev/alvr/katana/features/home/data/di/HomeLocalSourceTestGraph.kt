package dev.alvr.katana.features.home.data.di

import dev.alvr.katana.core.preferences.di.store.KatanaStore
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.data.entities.HomePreferences
import dev.alvr.katana.features.home.data.sources.HomeLocalSource
import dev.alvr.katana.features.home.data.sources.HomeLocalSourceImpl
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface HomeLocalSourceTestGraph : TestAppGraph {
    val homeLocalSource: HomeLocalSource

    @Provides fun homeLocalSource(store: KatanaStore<HomePreferences>): HomeLocalSource = HomeLocalSourceImpl(store)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides store: KatanaStore<HomePreferences>): HomeLocalSourceTestGraph
    }
}

internal fun createHomeLocalSourceTestGraph(store: KatanaStore<HomePreferences>) =
    createGraphFactory<HomeLocalSourceTestGraph.Factory>().create(store)
