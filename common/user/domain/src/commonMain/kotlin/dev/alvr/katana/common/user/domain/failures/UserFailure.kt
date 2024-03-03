package dev.alvr.katana.common.user.domain.failures

import dev.alvr.katana.core.domain.failures.Failure

sealed interface UserFailure : Failure {
    data object FetchingUser : UserFailure
    data object SavingUser : UserFailure
    data object GettingUserId : UserFailure
    data object GettingUserInfo : UserFailure
}
