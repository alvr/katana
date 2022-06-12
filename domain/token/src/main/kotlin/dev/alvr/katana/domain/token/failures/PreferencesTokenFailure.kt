package dev.alvr.katana.domain.token.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface PreferencesTokenFailure : Failure {
    object DeletingFailure : PreferencesTokenFailure
    object SavingFailure : PreferencesTokenFailure
}
