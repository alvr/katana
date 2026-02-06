package dev.alvr.katana.features.home.domain.failures

import dev.alvr.katana.core.domain.failures.Failure

sealed interface HomeFailure : Failure {
    data object GettingWelcomeCardVisibility : HomeFailure

    data object HidingWelcomeCard : HomeFailure
}
