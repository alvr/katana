package dev.alvr.katana.domain.session.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface TokenPreferencesFailure : Failure {
    object DeletingFailure : TokenPreferencesFailure
    object SavingFailure : TokenPreferencesFailure
}
