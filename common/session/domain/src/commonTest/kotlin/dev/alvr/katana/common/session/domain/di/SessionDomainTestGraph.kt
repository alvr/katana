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

    /**
     * Provides a ClearActiveSessionUseCase implementation wired with the given dispatcher and repository.
     *
     * @param dispatcher Dispatcher used to execute use case work on appropriate coroutine contexts.
     * @param repository Repository that manages session persistence.
     * @return A ClearActiveSessionUseCase that clears the active session from the repository.
     */
    @Provides
    fun provideClearActiveSessionUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): ClearActiveSessionUseCase = ClearActiveSessionUseCaseImpl(dispatcher, repository)

    /**
     * Provides a use case that deletes the AniList token from the session repository.
     *
     * @return A `DeleteAnilistTokenUseCase` that removes the stored AniList token.
     */
    @Provides
    fun provideDeleteAnilistTokenUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): DeleteAnilistTokenUseCase = DeleteAnilistTokenUseCaseImpl(dispatcher, repository)

    /**
     * Provides a GetAnilistTokenUseCase implementation wired with the given dispatcher and session repository.
     *
     * @return A GetAnilistTokenUseCase that retrieves the Anilist token from the session repository.
     */
    @Provides
    fun provideGetAnilistTokenUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): GetAnilistTokenUseCase = GetAnilistTokenUseCaseImpl(dispatcher, repository)

    /**
         * Provides a LogOutUseCase implementation for the test dependency graph.
         *
         * @return A LogOutUseCase that performs logout operations using the configured dispatcher and session repository.
         */
        @Provides
    fun provideLogOutUseCase(dispatcher: KatanaDispatcher, repository: SessionRepository): LogOutUseCase =
        LogOutUseCaseImpl(dispatcher, repository)

    /**
     * Provides an ObserveActiveSessionUseCase for observing the current active session.
     *
     * @return An ObserveActiveSessionUseCase that observes active session state using the provided dispatcher and repository.
     */
    @Provides
    fun provideObserveActiveSessionUseCase(
        dispatcher: KatanaDispatcher,
        repository: SessionRepository,
    ): ObserveActiveSessionUseCase = ObserveActiveSessionUseCaseImpl(dispatcher, repository)

    /**
         * Creates a SaveSessionUseCase configured to execute work on the provided dispatcher and persist sessions using the provided repository.
         *
         * @param dispatcher Dispatcher used to schedule use case execution.
         * @param repository Repository used to save session data.
         * @return A SaveSessionUseCase instance that saves sessions via the provided repository using the given dispatcher.
         */
        @Provides
    fun provideSaveSessionUseCase(dispatcher: KatanaDispatcher, repository: SessionRepository): SaveSessionUseCase =
        SaveSessionUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        /**
 * Creates a SessionDomainTestGraph wired with the supplied SessionRepository.
 *
 * @param sessionRepository The repository instance to be provided into the graph.
 * @return A fully constructed SessionDomainTestGraph that exposes session-domain use cases bound to the provided repository.
 */
fun create(@Provides sessionRepository: SessionRepository): SessionDomainTestGraph
    }
}

internal fun createSessionDomainTestGraph(sessionRepository: SessionRepository) =
    createGraphFactory<SessionDomainTestGraph.Factory>().create(sessionRepository)
