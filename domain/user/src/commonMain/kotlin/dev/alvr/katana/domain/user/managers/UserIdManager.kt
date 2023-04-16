package dev.alvr.katana.domain.user.managers

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure

interface UserIdManager {
    suspend fun getId(): Either<Failure, Int>
}
