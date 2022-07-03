package dev.alvr.katana.domain.session.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface SessionPreferencesFailure : Failure {
    object DeletingTokenFailure : SessionPreferencesFailure
    object ClearingSessionFailure : SessionPreferencesFailure
    object SavingFailure : SessionPreferencesFailure
}
