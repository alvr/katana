package dev.alvr.katana.data.remote.user.managers

import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.usecases.GetUserIdUseCase

internal class UserIdManagerImpl(
    private val getUserIdUseCase: GetUserIdUseCase,
) : UserIdManager {
    override suspend fun getId() = getUserIdUseCase().map { it.id }
}
