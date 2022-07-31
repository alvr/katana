package dev.alvr.katana.ui.lists.navigation

import dev.alvr.katana.ui.base.navigation.BaseNavigator

interface ListsNavigator : BaseNavigator {
    fun openEditEntry(id: Int)
    fun toMediaDetails(id: Int)
}
