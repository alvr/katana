package dev.alvr.katana.common.user.domain.di

import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCaseImpl
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCaseImpl
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCaseImpl
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface UserDomainTestGraph : TestAppGraph {
    val getUserIdUseCase: GetUserIdUseCase
    val observeUserInfoUseCase: ObserveUserInfoUseCase
    val saveUserIdUseCase: SaveUserIdUseCase

    /**
         * Creates a GetUserIdUseCase instance configured with the given dispatcher and repository.
         *
         * @param dispatcher Dispatcher used by the use case for coroutine execution.
         * @param repository Repository used to access and retrieve user data.
         * @return A configured `GetUserIdUseCase` implementation.
         */
        @Provides
    fun provideGetUserIdUseCase(dispatcher: KatanaDispatcher, repository: UserRepository): GetUserIdUseCase =
        GetUserIdUseCaseImpl(dispatcher, repository)

    /**
     * Provides an ObserveUserInfoUseCase implemented by ObserveUserInfoUseCaseImpl.
     *
     * @return An ObserveUserInfoUseCase that observes user information using the supplied dispatcher and repository.
     */
    @Provides
    fun provideObserveUserInfoUseCase(
        dispatcher: KatanaDispatcher,
        repository: UserRepository,
    ): ObserveUserInfoUseCase = ObserveUserInfoUseCaseImpl(dispatcher, repository)

    /**
         * Provides a SaveUserIdUseCase configured with the given dispatcher and repository.
         *
         * @param dispatcher Dispatcher used to execute the use case's coroutine work.
         * @param repository Repository used to persist user IDs.
         * @return A SaveUserIdUseCase instance that saves user IDs using the provided dispatcher and repository.
         */
        @Provides
    fun provideSaveUserIdUseCase(dispatcher: KatanaDispatcher, repository: UserRepository): SaveUserIdUseCase =
        SaveUserIdUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        /**
 * Constructs a UserDomainTestGraph using the supplied UserRepository.
 *
 * @param userRepository The repository instance to be provided into the created graph.
 * @return The created UserDomainTestGraph.
 */
fun create(@Provides userRepository: UserRepository): UserDomainTestGraph
    }
}

internal fun createUserDomainTestGraph(userRepository: UserRepository) =
    createGraphFactory<UserDomainTestGraph.Factory>().create(userRepository)
