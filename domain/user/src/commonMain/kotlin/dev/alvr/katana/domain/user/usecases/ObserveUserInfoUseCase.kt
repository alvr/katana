package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.usecases.FlowEitherUseCase
import dev.alvr.katana.domain.user.models.UserInfo
import dev.alvr.katana.domain.user.repositories.UserRepository

class ObserveUserInfoUseCase(
    private val repository: UserRepository,
) : FlowEitherUseCase<Unit, UserInfo>() {
    override fun createFlow(params: Unit) = repository.userInfo
}
