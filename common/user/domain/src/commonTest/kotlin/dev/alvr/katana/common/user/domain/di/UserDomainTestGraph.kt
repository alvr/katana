package dev.alvr.katana.common.user.domain.di

import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
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

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides userRepository: UserRepository): UserDomainTestGraph
    }
}

internal fun createUserDomainTestGraph(userRepository: UserRepository) =
    createGraphFactory<UserDomainTestGraph.Factory>().create(userRepository)
