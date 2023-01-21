package dev.alvr.katana.ui.lists.navigation

import dev.alvr.katana.ui.base.navigation.BaseNavigator
import dev.alvr.katana.ui.lists.entities.UserList

interface ListsNavigator : BaseNavigator {
    fun editEntry(id: Int, from: From)
    fun entryDetails(id: Int, from: From)
    fun listSelector(lists: Array<UserList>, selectedList: String, from: From)

    enum class From {
        ANIME,
        MANGA,
    }
}
