package dev.alvr.katana.domain.account.usecases

import arrow.core.Either
import dev.alvr.katana.domain.account.repositories.AccountRepository
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.EitherUseCase

class GetAccountInfoUseCase(
    private val repository: AccountRepository,
) : EitherUseCase<Unit, Unit> {
    override suspend fun invoke(params: Unit): Either<Failure, Unit> {
        TODO("Not yet implemented")
    }
}
