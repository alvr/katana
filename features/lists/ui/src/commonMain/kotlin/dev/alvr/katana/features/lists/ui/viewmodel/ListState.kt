package dev.alvr.katana.features.lists.ui.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiState
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class ListState<T : MediaListItem>(
    val items: ImmutableList<T> = persistentListOf(),
    val name: String? = null,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) : UiState
