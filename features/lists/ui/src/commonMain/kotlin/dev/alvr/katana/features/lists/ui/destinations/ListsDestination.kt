package dev.alvr.katana.features.lists.ui.destinations

import dev.alvr.katana.core.ui.destinations.KatanaDestination
import dev.alvr.katana.features.lists.ui.entities.UserList
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed interface ListsDestination : KatanaDestination {
    @Serializable
    data class ChangeList(
        internal val lists: List<UserList>,
        internal val selectedList: String,
    ) : ListsDestination {
        companion object {
            private const val LISTS = "{lists}"
            private const val SELECTED_LIST = "{selectedList}"

            internal const val ROUTE = "change_list/$LISTS/$SELECTED_LIST"

            fun createRoute(lists: List<UserList>, selectedList: String) = ROUTE
                .replace(LISTS, Json.encodeToString(lists))
                .replace(SELECTED_LIST, selectedList)
        }
    }
}
