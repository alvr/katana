package dev.alvr.katana.features.lists.domain.failures

import dev.alvr.katana.core.domain.failures.Failure

sealed interface ListsFailure : Failure {
    data object GetMediaCollection : ListsFailure
    data object UpdatingList : ListsFailure
}
