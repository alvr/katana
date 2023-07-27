package dev.alvr.katana.domain.user.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface UserFailure : Failure {
    data object FetchingUser : UserFailure
    data object SavingUser : UserFailure
    data object GettingUserId : UserFailure
}
