package dev.alvr.katana.features.lists.ui.navigation

import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.alvr.katana.features.lists.ui.entities.UserList

sealed interface ListsNavigator : KatanaNavigator {
    fun navigateToEntryDetails(id: Int)
    fun showEditEntry(id: Int)
    fun showListSelector(lists: Array<UserList>, selectedList: String)
}
