package dev.alvr.katana.features.lists.ui.screen

import androidx.navigation.NavGraphBuilder
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator

fun NavGraphBuilder.lists(listsNavigator: ListsNavigator) {
    animeLists(listsNavigator)
    mangaLists(listsNavigator)
}
