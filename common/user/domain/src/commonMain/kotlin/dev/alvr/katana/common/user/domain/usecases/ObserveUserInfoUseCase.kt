package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.zacsweers.metro.Inject

@Inject
class ObserveUserInfoUseCase
internal constructor(dispatcher: KatanaDispatcher, private val repository: UserRepository) :
    FlowEitherUseCase<Unit, UserInfo>(dispatcher) {
    override fun createFlow(params: Unit) = repository.userInfo
}
