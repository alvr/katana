package dev.alvr.katana.common.user.domain.usecases

import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.FlowEitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaFlowEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface ObserveUserInfoUseCase : KatanaFlowEitherUseCase<Unit, UserInfo>

@ContributesBinding(AppScope::class, binding = binding<ObserveUserInfoUseCase>())
internal class ObserveUserInfoUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: UserRepository) :
    FlowEitherUseCase<Unit, UserInfo>(dispatcher), ObserveUserInfoUseCase {
    override fun createFlow(params: Unit) = repository.userInfo
}
