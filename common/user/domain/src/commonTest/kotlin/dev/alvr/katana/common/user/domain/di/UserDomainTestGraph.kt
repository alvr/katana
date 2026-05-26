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

    @Provides
    fun provideGetUserIdUseCase(dispatcher: KatanaDispatcher, repository: UserRepository): GetUserIdUseCase =
        GetUserIdUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideObserveUserInfoUseCase(
        dispatcher: KatanaDispatcher,
        repository: UserRepository,
    ): ObserveUserInfoUseCase = ObserveUserInfoUseCaseImpl(dispatcher, repository)

    @Provides
    fun provideSaveUserIdUseCase(dispatcher: KatanaDispatcher, repository: UserRepository): SaveUserIdUseCase =
        SaveUserIdUseCaseImpl(dispatcher, repository)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides userRepository: UserRepository): UserDomainTestGraph
    }
}

internal fun createUserDomainTestGraph(userRepository: UserRepository) =
    createGraphFactory<UserDomainTestGraph.Factory>().create(userRepository)
