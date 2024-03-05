package dev.alvr.katana.common.user.data.managers

import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import me.tatarka.inject.annotations.Inject

@Inject
class UserIdManagerImpl internal constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
) : UserIdManager {
    override suspend fun getId() = getUserIdUseCase().map { it.id }
}
