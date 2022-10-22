package dev.alvr.katana.domain.lists.failures

import dev.alvr.katana.domain.base.failures.Failure

sealed class ListsFailure : Failure() {
    object GetMediaCollection : ListsFailure()
    object UpdatingList : ListsFailure()
}
