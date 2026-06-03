package dev.alvr.katana.common.session.domain.di

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.common.session.domain.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.ClearActiveSessionUseCaseImpl
import dev.alvr.katana.common.session.domain.usecases.DeleteAnilistTokenUseCase
import dev.alvr.katana.common.session.domain.usecases.DeleteAnilistTokenUseCaseImpl
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.common.session.domain.usecases.GetAnilistTokenUseCaseImpl
import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.session.domain.usecases.LogOutUseCaseImpl
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCaseImpl
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCaseImpl
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@Suppress("ComplexInterface")
@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface SessionDomainTestGraph : TestAppGraph {
    val clearActiveSessionUseCase: ClearActiveSessionUseCase
    val deleteAnilistTokenUseCase: DeleteAnilistTokenUseCase
    val getAnilistTokenUseCase: GetAnilistTokenUseCase
    val logOutUseCase: LogOutUseCase
    val observeActiveSessionUseCase: ObserveActiveSessionUseCase
    val saveSessionUseCase: SaveSessionUseCase

    @Provides
    fun provideClearActiveSessionUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): ClearActiveSessionUseCase = ClearActiveSessionUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideDeleteAnilistTokenUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): DeleteAnilistTokenUseCase = DeleteAnilistTokenUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideGetAnilistTokenUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): GetAnilistTokenUseCase = GetAnilistTokenUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideLogOutUseCase(dispatcher: KatanaDispatcher, repository: SessionRepository): LogOutUseCase =
        LogOutUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideObserveActiveSessionUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): ObserveActiveSessionUseCase = ObserveActiveSessionUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideSaveSessionUseCase(dispatcher: KatanaDispatcher, repository: SessionRepository): SaveSessionUseCase =
        SaveSessionUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides sessionRepository: SessionRepository): SessionDomainTestGraph
    }
}

internal fun createSessionDomainTestGraph(sessionRepository: SessionRepository) =
    createGraphFactory<SessionDomainTestGraph.Factory>().create(sessionRepository)
