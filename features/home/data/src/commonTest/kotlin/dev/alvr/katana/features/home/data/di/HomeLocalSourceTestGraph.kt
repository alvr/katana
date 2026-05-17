package dev.alvr.katana.features.home.data.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.data.sources.HomeLocalSource
import dev.alvr.katana.features.home.data.sources.HomeLocalSourceImpl
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory
import eu.anifantakis.lib.ksafe.KSafe

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface HomeLocalSourceTestGraph : TestAppGraph {
    val homeLocalSource: HomeLocalSource

    @Provides fun homeLocalSource(@HomePreferences safe: KSafe): HomeLocalSource = HomeLocalSourceImpl(safe)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides @HomePreferences safe: KSafe): HomeLocalSourceTestGraph
    }
}

internal fun createHomeLocalSourceTestGraph(safe: KSafe) =
    createGraphFactory<HomeLocalSourceTestGraph.Factory>().create(safe)
