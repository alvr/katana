package dev.alvr.katana.domain.session.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface SessionFailure : Failure {
    data object CheckingActiveSession : SessionFailure
    data object DeletingToken : SessionFailure
    data object ClearingSession : SessionFailure
    data object SavingSession : SessionFailure
}
