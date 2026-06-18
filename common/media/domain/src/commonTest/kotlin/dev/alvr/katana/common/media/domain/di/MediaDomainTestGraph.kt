package dev.alvr.katana.common.media.domain.di

import dev.alvr.katana.common.media.domain.repositories.MediaCollectionRepository
import dev.alvr.katana.common.media.domain.usecases.ObserveMediaCollectionUseCase
import dev.alvr.katana.common.media.domain.usecases.ObserveMediaCollectionUseCaseImpl
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface MediaDomainTestGraph : TestAppGraph {
    val observeMediaCollectionUseCase: ObserveMediaCollectionUseCase

    @Provides
    fun provideObserveMediaCollectionUseCase(
        dispatcher: KatanaDispatcher,
        repository: MediaCollectionRepository,
    ): ObserveMediaCollectionUseCase = ObserveMediaCollectionUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides mediaCollectionRepository: MediaCollectionRepository): MediaDomainTestGraph
    }
}

internal fun createMediaDomainTestGraph(mediaCollectionRepository: MediaCollectionRepository) =
    createGraphFactory<MediaDomainTestGraph.Factory>().create(mediaCollectionRepository)
