package dev.alvr.katana.domain.user.usecases

import dev.alvr.katana.domain.base.usecases.EitherUseCase
import dev.alvr.katana.domain.user.models.UserInfo
import dev.alvr.katana.domain.user.repositories.UserRepository

class GetUserInfoUseCase(
    private val repository: UserRepository,
) : EitherUseCase<Unit, UserInfo> {
    override suspend fun invoke(params: Unit) = repository.getUserInfo()
}
