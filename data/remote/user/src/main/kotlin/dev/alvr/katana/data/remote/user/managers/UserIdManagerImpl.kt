package dev.alvr.katana.data.remote.user.managers

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.usecases.GetUserIdUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserIdManagerImpl @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
) : UserIdManager {
    override suspend fun getId(): Int = getUserIdUseCase().id
}
