package dev.alvr.katana.common.session.data.di

import dev.alvr.katana.common.session.data.sources.SessionLocalSource
import dev.alvr.katana.common.session.data.sources.SessionLocalSourceImpl
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory
import eu.anifantakis.lib.ksafe.KSafe

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface SessionLocalSourceTestGraph : TestAppGraph {
    val sessionLocalSource: SessionLocalSource

    @Provides fun sessionLocalSource(@SessionPreferences safe: KSafe): SessionLocalSource = SessionLocalSourceImpl(safe)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides @SessionPreferences safe: KSafe): SessionLocalSourceTestGraph
    }
}

internal fun createSessionLocalSourceTestGraph(safe: KSafe) =
    createGraphFactory<SessionLocalSourceTestGraph.Factory>().create(safe)
