package dev.alvr.katana.domain.session.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface SessionFailure : Failure {
    object DeletingTokenFailure : SessionFailure
    object ClearingSessionFailure : SessionFailure
    object SavingFailure : SessionFailure
}
