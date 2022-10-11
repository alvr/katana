package dev.alvr.katana.ui.lists.navigation

import dev.alvr.katana.ui.base.navigation.BaseNavigator

interface ListsNavigator : BaseNavigator {
    fun openEditEntry(id: Int, from: From)
    fun toMediaDetails(id: Int, from: From)
    fun openListSelector(lists: Array<String>, from: From)

    enum class From {
        ANIME,
        MANGA,
    }
}
