package dev.alvr.katana.ui.lists.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.alvr.katana.ui.base.decompose.state.StatefulComponent
import dev.alvr.katana.ui.lists.component.listselector.ListSelectorComponent
import dev.alvr.katana.ui.lists.entities.UserList
import dev.alvr.katana.ui.lists.entities.item.MediaListItem
import dev.alvr.katana.ui.lists.viewmodel.ListState

sealed interface BaseListComponent<out T : MediaListItem> : StatefulComponent<ListState<T>> {
    val bottomSheet: Value<ChildSlot<*, ListSelectorComponent>>

    val userLists: List<UserList>

    fun refreshList()
    fun addPlusOne(id: Int)
    fun selectList(name: String)
    fun search(search: String)

    fun dismissListSelector()
    fun showListSelectorBottomSheet(lists: List<UserList>, current: String)
}
