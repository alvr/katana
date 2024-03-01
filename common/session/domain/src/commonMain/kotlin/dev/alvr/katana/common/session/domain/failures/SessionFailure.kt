package dev.alvr.katana.common.session.domain.failures

import dev.alvr.katana.core.domain.failures.Failure

sealed interface SessionFailure : Failure {
    data object CheckingActiveSession : SessionFailure
    data object ClearingSession : SessionFailure
    data object DeletingToken : SessionFailure
    data object LoggingOut : SessionFailure
    data object SavingSession : SessionFailure
}
