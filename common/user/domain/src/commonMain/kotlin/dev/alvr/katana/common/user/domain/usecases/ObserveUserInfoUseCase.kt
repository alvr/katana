package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase

class ObserveUserInfoUseCase(
    private val repository: UserRepository,
) : FlowEitherUseCase<Unit, UserInfo>() {
    override fun createFlow(params: Unit) = repository.userInfo
}
