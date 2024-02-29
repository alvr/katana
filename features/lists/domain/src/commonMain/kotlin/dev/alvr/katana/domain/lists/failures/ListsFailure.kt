package dev.alvr.katana.domain.lists.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed interface ListsFailure : Failure {
    data object GetMediaCollection : ListsFailure
    data object UpdatingList : ListsFailure
}
