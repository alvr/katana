package dev.alvr.katana.features.lists.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.alvr.katana.core.ui.destinations.HomeDestination
import dev.alvr.katana.features.lists.ui.screens.AnimeScreen
import dev.alvr.katana.features.lists.ui.screens.MangaScreen
import dev.alvr.katana.features.lists.ui.screens.changeListBottomSheet

fun NavGraphBuilder.lists(listsNavigator: ListsNavigator) {
    animeLists(listsNavigator)
    mangaLists(listsNavigator)

    changeListBottomSheet(listsNavigator)
}

private fun NavGraphBuilder.animeLists(navigator: ListsNavigator) {
    composable<HomeDestination.AnimeLists> {
        AnimeScreen(navigator)
    }
}

private fun NavGraphBuilder.mangaLists(navigator: ListsNavigator) {
    composable<HomeDestination.MangaLists> {
        MangaScreen(navigator)
    }
}
