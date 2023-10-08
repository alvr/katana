package dev.alvr.katana.ui.lists.navigation

import dev.alvr.katana.ui.base.navigation.BaseNavigator
import dev.alvr.katana.ui.lists.entities.UserList

interface ListsNavigator : BaseNavigator {
    fun editEntry(id: Int)
    fun entryDetails(id: Int)
    fun listSelector(lists: Array<UserList>, selectedList: String)
}
