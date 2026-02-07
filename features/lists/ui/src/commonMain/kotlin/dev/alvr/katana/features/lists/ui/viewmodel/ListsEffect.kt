package dev.alvr.katana.features.lists.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiEffect

internal sealed interface ListsEffect : UiEffect {
    data object LoadingListsFailure : ListsEffect

    data object AddPlusOneFailure : ListsEffect

    data object AddPlusOneSuccess : ListsEffect
}
