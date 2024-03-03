package dev.alvr.katana.features.lists.ui.navigation

import dev.alvr.katana.core.ui.navigation.BaseNavigator
import dev.alvr.katana.features.lists.ui.entities.UserList

interface ListsNavigator : BaseNavigator {
    fun editEntry(id: Int)
    fun entryDetails(id: Int)
    fun listSelector(lists: Array<UserList>, selectedList: String)
}
