package dev.alvr.katana.domain.session.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface SessionPreferencesFailure : Failure {
    object DeletingFailure : SessionPreferencesFailure
    object SavingFailure : SessionPreferencesFailure
}
