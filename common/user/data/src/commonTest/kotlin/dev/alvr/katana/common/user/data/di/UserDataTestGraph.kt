package dev.alvr.katana.common.user.data.di

import dev.alvr.katana.common.user.data.managers.UserIdManagerImpl
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface UserDataTestGraph : TestAppGraph {
    val userIdManager: UserIdManager

    @Provides fun userIdManager(getUserIdUseCase: GetUserIdUseCase): UserIdManager = UserIdManagerImpl(getUserIdUseCase)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides userRepository: UserRepository): UserDataTestGraph
    }
}

internal fun createUserDataTestGraph(userRepository: UserRepository) =
    createGraphFactory<UserDataTestGraph.Factory>().create(userRepository)
