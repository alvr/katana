package dev.alvr.katana.common.media.domain.failures

import dev.alvr.katana.core.domain.failures.Failure

sealed interface MediaFailure : Failure {
    data object GetMediaCollection : MediaFailure
}
