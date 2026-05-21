package dev.alvr.katana.common.session.data.di

import dev.alvr.katana.common.session.data.repositories.SessionRepositoryImpl
import dev.alvr.katana.common.session.data.sources.SessionLocalSource
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface SessionRepositoryTestGraph : TestAppGraph {
    val sessionRepository: SessionRepository

    @Provides
    fun sessionRepository(localSource: SessionLocalSource): SessionRepository = SessionRepositoryImpl(localSource)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides localSource: SessionLocalSource): SessionRepositoryTestGraph
    }
}

internal fun createSessionRepositoryTestGraph(localSource: SessionLocalSource) =
    createGraphFactory<SessionRepositoryTestGraph.Factory>().create(localSource)
