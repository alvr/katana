package dev.alvr.katana.ui.lists.entities.mappers

import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.UserList
import dev.alvr.katana.ui.lists.viewmodel.ListsCollection

internal fun <I : MediaListItem> ListsCollection<I>.toUserList() = Array(size) { position ->
    val entry = entries.elementAt(position)
    UserList(entry.key to entry.value.size)
}
