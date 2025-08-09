package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.compose.runtime.Immutable
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.ui.viewmodel.UiState
import dev.alvr.katana.features.lists.ui.entities.ListsCollection
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toUserList
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

@Immutable
internal data class ListsState<T : MediaListItem>(
    val type: ListType,
    val selectedList: String = String.empty,
    private val collection: ListsCollection<T> = persistentMapOf(),
    private val searchQuery: String = String.empty,
    val error: Boolean = false,
    val loading: Boolean = true,
) : UiState {
    private val originalEntries get() = collection.getOrElse(selectedList) { persistentMapOf() }

    val entries get() = if (searchQuery.isEmpty()) {
        originalEntries
    } else {
        originalEntries.filter { (_, item) ->
            item.title.contains(searchQuery, ignoreCase = true)
        }.toImmutableMap()
    }

    val items get() = entries.values.toImmutableList()
    val empty get() = entries.isEmpty()
    val lists get() = collection.toUserList()

    enum class ListType {
        Anime,
        Manga,
    }
}
