package dev.alvr.katana.domain.session.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface SessionFailure : Failure {
    object CheckingActiveSession : SessionFailure
    object DeletingToken : SessionFailure
    object ClearingSession : SessionFailure
    object SavingSession : SessionFailure
}
