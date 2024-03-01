package dev.alvr.katana.features.lists.ui.entities.mappers

import dev.alvr.katana.features.lists.ui.entities.ListsCollection
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.UserList

internal fun <I : MediaListItem> ListsCollection<I>.toUserList() = Array(size) { position ->
    val entry = entries.elementAt(position)
    UserList(entry.key to entry.value.size)
}
