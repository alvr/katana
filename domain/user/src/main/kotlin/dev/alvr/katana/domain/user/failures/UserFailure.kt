package dev.alvr.katana.domain.user.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface UserFailure : Failure {
    object FetchingFailure : UserFailure
    object SavingFailure : UserFailure
    object UserIdFailure : UserFailure
}
