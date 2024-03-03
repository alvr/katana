package dev.alvr.katana.common.user.domain.managers

import arrow.core.Either
import dev.alvr.katana.core.domain.failures.Failure

interface UserIdManager {
    suspend fun getId(): Either<Failure, Int>
}
