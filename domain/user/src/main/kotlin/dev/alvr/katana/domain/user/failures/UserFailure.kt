package dev.alvr.katana.domain.user.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed class UserFailure : Failure() {
    object FetchingUser : UserFailure()
    object SavingUser : UserFailure()
    object GettingUserId : UserFailure()
}
