package dev.alvr.katana.features.lists.ui.viewmodel

import androidx.compose.runtime.Immutable
import dev.alvr.katana.core.common.annotations.CoverageExcluded
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.ui.viewmodel.UiState
import dev.alvr.katana.features.lists.ui.entities.ListsCollection
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.mappers.toUserList
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

@Immutable
internal data class ListsState(
    val selectedList: String = String.empty,
    val error: Boolean = false,
    val loading: Boolean = true,
    private val collection: ListsCollection<MediaListItem> = persistentMapOf(),
    private val searchQuery: String = String.empty,
) : UiState {
    private val originalEntries = collection.getOrElse(selectedList) { persistentMapOf() }

    val entries =
        if (searchQuery.isEmpty()) {
            originalEntries
        } else {
            originalEntries.filter { (_, item) -> item.title.contains(searchQuery, ignoreCase = true) }.toImmutableMap()
        }

    val items = entries.values.toImmutableList()
    val empty = items.isEmpty()
    val lists = collection.toUserList()

    @CoverageExcluded
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ListsState

        if (error != other.error) return false
        if (loading != other.loading) return false
        if (selectedList != other.selectedList) return false
        if (collection != other.collection) return false
        if (items != other.items) return false

        return true
    }

    @CoverageExcluded
    override fun hashCode(): Int {
        var result = error.hashCode()
        result = 31 * result + loading.hashCode()
        result = 31 * result + selectedList.hashCode()
        result = 31 * result + collection.hashCode()
        result = 31 * result + items.hashCode()
        return result
    }
}
